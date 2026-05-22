package com.ghosttype.security

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Fetches the GitHub-hosted Users.json approval list and decides
 * whether the current device is allowed to use GhostType Pro.
 *
 * The URL itself is XOR-encrypted with a key derived from the APK
 * signing cert (see [Obf]) — so a thief can't repackage the app to
 * point at their own approval server, and editing the GitHub URL
 * inside the binary makes decryption produce garbage and the fetch
 * fails. Both attack paths land the user on the permanent lock
 * screen.
 *
 * State machine:
 *   Approved        — id is on the approved list
 *   Blocked         — id is on the blocked list (kill-switch wins)
 *   NotApproved     — id isn't on either list
 *   OfflineUnknown  — couldn't reach the server AND no fresh cache
 *
 * Caching rules (battery + network friendly):
 *   - Hits the network at most once per [CHECK_INTERVAL_MS] (6 h).
 *     If the last cached state was Approved, we return Approved
 *     without touching the network at all.
 *   - On any network failure, falls back to the cached state if the
 *     cache is < [MAX_OFFLINE_MS] (7 days) old. Otherwise OfflineUnknown.
 *   - Blocked always wins over Approved (so revocation can't be
 *     defeated by clearing app data).
 */
object ApprovalGate {

    sealed class State {
        object Approved : State()
        object Blocked : State()
        object NotApproved : State()
        data class OfflineUnknown(val reason: String) : State()
    }

    private const val PREFS = "ghosttype_gate"
    private const val K_LAST_CHECK = "last_check_at"
    private const val K_LAST_STATE = "last_state"

    private const val CHECK_INTERVAL_MS = 6L * 60L * 60L * 1000L      // 6 h
    private const val MAX_OFFLINE_MS    = 7L * 24L * 60L * 60L * 1000L // 7 days

    @Volatile private var http: OkHttpClient? = null
    private fun http(): OkHttpClient {
        http?.let { return it }
        val c = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(4, TimeUnit.SECONDS)
            .build()
        http = c
        return c
    }

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    /** Cheap synchronous read of the last cached gate decision. Used
     *  by the IME service (where blocking on the network is not OK)
     *  to decide between showing the keyboard and showing a lock
     *  view. The actual network refresh happens from MainActivity. */
    fun cachedState(ctx: Context): State {
        val name = prefs(ctx).getString(K_LAST_STATE, null)
            ?: return State.OfflineUnknown("never_checked")
        return when (name) {
            "Approved" -> State.Approved
            "Blocked" -> State.Blocked
            "NotApproved" -> State.NotApproved
            else -> State.OfflineUnknown("never_checked")
        }
    }

    fun isApprovedCached(ctx: Context): Boolean {
        val s = cachedState(ctx)
        if (s !is State.Approved) return false
        // An "Approved" cache also expires after 7 days offline so a
        // device whose approval was revoked while it had no internet
        // eventually gets locked out anyway.
        val last = prefs(ctx).getLong(K_LAST_CHECK, 0L)
        return System.currentTimeMillis() - last < MAX_OFFLINE_MS
    }

    /** Full evaluation. Hits the network unless a fresh cached
     *  Approved exists and `force` is false. */
    suspend fun evaluate(ctx: Context, force: Boolean = false): State =
        withContext(Dispatchers.IO) {
            val deviceId = DeviceId.get(ctx)
            val p = prefs(ctx)
            val now = System.currentTimeMillis()
            val lastCheck = p.getLong(K_LAST_CHECK, 0L)
            val lastStateName = p.getString(K_LAST_STATE, null)

            if (!force && lastStateName == "Approved" && now - lastCheck < CHECK_INTERVAL_MS) {
                return@withContext State.Approved
            }

            val urlStr = Obf.decode(ctx, ObfConstants.APPROVAL_URL)
            if (!urlStr.startsWith("https://")) {
                // Decryption produced garbage — almost always means the
                // APK was repackaged with a different signing cert.
                return@withContext fallback(p, now, lastStateName, "url_decrypt_failed")
            }

            try {
                val req = Request.Builder().url(urlStr)
                    .header("Accept", "application/json, text/plain, */*")
                    .header("User-Agent", "GhostTypePro")
                    .build()
                http().newCall(req).execute().use { resp ->
                    if (!resp.isSuccessful) {
                        return@withContext fallback(p, now, lastStateName, "http_${resp.code}")
                    }
                    val body = resp.body?.string()
                        ?: return@withContext fallback(p, now, lastStateName, "empty_body")
                    val state = decide(body, deviceId)
                    p.edit()
                        .putLong(K_LAST_CHECK, now)
                        .putString(K_LAST_STATE, name(state))
                        .apply()
                    return@withContext state
                }
            } catch (e: Exception) {
                return@withContext fallback(p, now, lastStateName, e.javaClass.simpleName)
            }
        }

    private fun decide(json: String, deviceId: String): State {
        return try {
            val root = JSONObject(json)

            // Optional kill-switch list — wins over approved.
            val blocked = root.optJSONArray("blocked") ?: JSONArray()
            for (i in 0 until blocked.length()) {
                val v = blocked.opt(i)
                val id = when (v) {
                    is JSONObject -> v.optString("android_id", "")
                    is String -> v
                    else -> ""
                }
                if (id.equals(deviceId, ignoreCase = true)) return State.Blocked
            }

            val approved = root.optJSONArray("approved") ?: JSONArray()
            for (i in 0 until approved.length()) {
                val v = approved.opt(i)
                val id = when (v) {
                    is JSONObject -> v.optString("android_id", "")
                    is String -> v
                    else -> ""
                }
                if (id.equals(deviceId, ignoreCase = true)) return State.Approved
            }
            State.NotApproved
        } catch (_: Throwable) {
            // Corrupt JSON — treat as NotApproved so we don't accidentally
            // grant access to everyone if the file is mangled.
            State.NotApproved
        }
    }

    private fun fallback(p: SharedPreferences, now: Long, lastStateName: String?, why: String): State {
        val lastCheck = p.getLong(K_LAST_CHECK, 0L)
        if (lastStateName == "Approved" && now - lastCheck < MAX_OFFLINE_MS) {
            return State.Approved
        }
        if (lastStateName == "Blocked") return State.Blocked
        if (lastStateName == "NotApproved") return State.NotApproved
        return State.OfflineUnknown(why)
    }

    private fun name(s: State): String = when (s) {
        State.Approved -> "Approved"
        State.Blocked -> "Blocked"
        State.NotApproved -> "NotApproved"
        is State.OfflineUnknown -> "OfflineUnknown"
    }
}
