package com.ghosttype.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ghosttype.security.ApprovalGate
import com.ghosttype.security.SecurityGuard
import com.ghosttype.ui.screens.LockScreen

/**
 * Wraps the entire main UI tree behind the security + approval gate.
 *
 *   1. SecurityGuard.verifyOrDie — signature pinning, anti-debug,
 *      production-build flag check. Failure → permanent lock screen
 *      (treated as Blocked).
 *   2. ApprovalGate.evaluate — fetches Users.json from GitHub (or
 *      uses a cached decision), decides whether this device's ID is
 *      on the approved list.
 *
 * Only when both pass do we render the real [content] (the existing
 * AppRoot with all the keyboard settings tabs).
 */
@Composable
fun GatedApp(content: @Composable () -> Unit) {
    val ctx = LocalContext.current
    var state by remember { mutableStateOf<ApprovalGate.State?>(null) }
    // v1.10 — bumped whenever the background ApprovalRefreshWorker tells
    // us approval was just revoked. Used as a key on LaunchedEffect to
    // re-run the gate evaluation, which now reads the freshly-updated
    // SharedPrefs cache and flips state to NotApproved/Blocked.
    var refreshTick by remember { mutableStateOf(0) }

    LaunchedEffect(refreshTick) {
        // 1. Tamper check — if this fails the lock screen is permanent
        //    for this APK install (the only "fix" is to reinstall a
        //    legitimately-signed copy). We surface it as Blocked so
        //    the user sees the strongest message.
        if (!SecurityGuard.verifyOrDie(ctx)) {
            state = ApprovalGate.State.Blocked
            return@LaunchedEffect
        }
        // 2. Approval check — hits GitHub once per 6 h, otherwise uses
        //    cached decision. On a refresh-tick re-run we force=true so
        //    the in-process state matches whatever the worker just wrote.
        state = ApprovalGate.evaluate(ctx, force = refreshTick > 0)
    }

    // v1.10 — listen for the worker's revocation broadcast while the
    // settings activity is foregrounded. If it fires we bump the tick
    // so the gate re-evaluates and the user sees the lock screen
    // immediately (instead of waiting for the next activity launch).
    DisposableEffect(Unit) {
        val receiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(c: android.content.Context?, i: android.content.Intent?) {
                if (i?.action == com.ghosttype.security.ApprovalRefreshWorker.ACTION_APPROVAL_REVOKED) {
                    refreshTick++
                }
            }
        }
        val filter = android.content.IntentFilter(
            com.ghosttype.security.ApprovalRefreshWorker.ACTION_APPROVAL_REVOKED
        )
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                ctx.registerReceiver(receiver, filter, android.content.Context.RECEIVER_NOT_EXPORTED)
            } else {
                @Suppress("UnspecifiedRegisterReceiverFlag")
                ctx.registerReceiver(receiver, filter)
            }
        } catch (_: Throwable) {}
        onDispose { try { ctx.unregisterReceiver(receiver) } catch (_: Throwable) {} }
    }

    when (val s = state) {
        null -> {
            // Initial loading splash. Brief — usually < 1 frame after
            // the cache hit, ~200 ms on a cold network fetch.
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        is ApprovalGate.State.Approved -> content()
        else -> LockScreen(
            state = s,
            onRecheck = {
                state = null
                state = ApprovalGate.evaluate(ctx, force = true)
            }
        )
    }
}
