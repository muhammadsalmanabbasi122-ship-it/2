package com.ghosttype.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghosttype.security.ApprovalGate
import com.ghosttype.security.DeviceId
import com.ghosttype.security.Obf
import com.ghosttype.security.ObfConstants
import com.ghosttype.utils.SettingsStore
import kotlinx.coroutines.launch

private val Orange     = Color(0xFFFF8C00)
private val BgDark     = Color(0xFF0C0C0C)
private val CardBg     = Color(0xFF161616)
private val Divider    = Color(0xFF242424)
private val TextMuted  = Color(0xFF888888)
private val GreenWa    = Color(0xFF25D366)

private data class LockPlan(
    val emoji: String, val tag: String?, val name: String,
    val duration: String, val price: String, val accent: Color, val featured: Boolean = false
)

private val LOCK_PLANS = listOf(
    LockPlan("🎯", null,      "Trial",     "7 Days",   "FREE",    Color(0xFF66BBFF)),
    LockPlan("📅", null,      "Monthly",   "1 Month",  "Rs 50",   Orange),
    LockPlan("🔥", "POPULAR", "Quarterly", "3 Months", "Rs 120",  Orange,  true),
    LockPlan("⚡", null,      "Half Year", "6 Months", "Rs 250",  Orange),
    LockPlan("👑", "BEST",    "Lifetime",  "Forever",  "Rs 500",  Color(0xFFFFD700), true),
)

@Composable
fun LockScreen(state: ApprovalGate.State, onRecheck: suspend () -> Unit) {
    val ctx    = LocalContext.current
    val prefs  = remember { SettingsStore.prefs(ctx) }
    val id     = remember { DeviceId.get(ctx) }
    val wa     = remember { Obf.decode(ctx, ObfConstants.WHATSAPP_NUMBER) }
    val owner  = remember { Obf.decode(ctx, ObfConstants.OWNER_NAME) }
    val scope  = rememberCoroutineScope()
    var busy   by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf(prefs.getString(SettingsStore.KEY_PLANS_USER_NAME, "") ?: "") }

    val statusColor = when (state) {
        ApprovalGate.State.Blocked             -> Color(0xFFFF4444)
        is ApprovalGate.State.OfflineUnknown   -> Color(0xFF66BBFF)
        else                                   -> Orange
    }
    val statusText = when (state) {
        ApprovalGate.State.Blocked           -> "Access Revoked"
        is ApprovalGate.State.OfflineUnknown -> "No Internet"
        ApprovalGate.State.NotApproved       -> "Awaiting Approval"
        else                                 -> "Locked"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        // ── App name + status ────────────────────────────────────
        Text("GhostType Pro", color = Orange, fontWeight = FontWeight.ExtraBold, fontSize = 26.sp)
        Text(statusText, color = statusColor, fontWeight = FontWeight.Medium, fontSize = 15.sp)

        Spacer(Modifier.height(4.dp))

        // ── Device ID ────────────────────────────────────────────
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Device ID", color = TextMuted, fontSize = 12.sp, letterSpacing = 0.8.sp)
                Text(
                    id,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = {
                            (ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                                .setPrimaryClip(ClipData.newPlainText("id", id))
                            Toast.makeText(ctx, "Copied", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Divider),
                        modifier = Modifier.weight(1f).height(44.dp)
                    ) { Text("Copy", fontWeight = FontWeight.SemiBold) }

                    Button(
                        onClick = {
                            val url = "https://wa.me/${wa.ifBlank { "923017787729" }}?text=Approval%20request%0AID%3A%20$id"
                            try { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
                            catch (_: Exception) { Toast.makeText(ctx, "WhatsApp not installed", Toast.LENGTH_SHORT).show() }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenWa, contentColor = Color.Black),
                        modifier = Modifier.weight(2f).height(44.dp)
                    ) { Text("Send to ${owner.ifBlank { "CHAND" }}", fontWeight = FontWeight.Bold) }
                }
            }
        }

        // ── Name input ───────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CardBg)
                .border(1.dp, if (userName.isNotBlank()) Orange.copy(alpha = 0.4f) else Divider, RoundedCornerShape(14.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("YOUR NAME", color = TextMuted, fontSize = 11.sp, letterSpacing = 0.8.sp)
            OutlinedTextField(
                value = userName,
                onValueChange = {
                    userName = it
                    prefs.edit().putString(SettingsStore.KEY_PLANS_USER_NAME, it).apply()
                },
                placeholder = { Text("Enter your name", color = TextMuted) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange,
                    unfocusedBorderColor = Divider,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Orange
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // ── Plans as cards ───────────────────────────────────────
        Text("CHOOSE A PLAN", color = TextMuted, fontSize = 11.sp, letterSpacing = 0.8.sp)

        LOCK_PLANS.forEach { plan ->
            val accentColor = plan.accent
            val isFeatured  = plan.featured
            val bgColor     = if (isFeatured) Color(0xFF1A1000) else CardBg
            val borderColor = if (isFeatured) accentColor.copy(alpha = 0.45f) else Divider

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(bgColor)
                    .border(if (isFeatured) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(16.dp))
                    .clickable {
                        val msg = buildString {
                            append("*GhostType Pro — Plan Request*\n\n")
                            if (userName.isNotBlank()) append("👤 *Name:* $userName\n")
                            append("🔑 *Key:* $id\n")
                            append("📦 *Plan:* ${plan.name} (${plan.duration}) — ${plan.price}")
                        }
                        val encoded = Uri.encode(msg)
                        try {
                            ctx.startActivity(
                                Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://wa.me/923017787729?text=$encoded"))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                        } catch (_: Exception) {}
                    }
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Top row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) { Text(plan.emoji, fontSize = 22.sp) }

                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(plan.name, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            if (plan.tag != null) {
                                Text(plan.tag, color = accentColor, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(accentColor.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text(plan.duration, color = TextMuted, fontSize = 12.sp)
                    }
                    Text(plan.price, color = accentColor, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                }

                // Name + Key preview row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0D0D0D))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("👤 ${if (userName.isNotBlank()) userName else "—"}", color = if (userName.isNotBlank()) Color.White else TextMuted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Box(modifier = Modifier.width(1.dp).height(20.dp).background(Divider))
                    Column(modifier = Modifier.weight(1.5f)) {
                        Text("🔑 $id", color = Color(0xFFAAAAAA), fontSize = 11.sp, maxLines = 1)
                    }
                }

                // Tap hint
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.10f))
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📲 Tap to Subscribe via WhatsApp", color = accentColor.copy(alpha = 0.9f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // ── Re-check ─────────────────────────────────────────────
        Button(
            onClick = { if (!busy) { busy = true; scope.launch { try { onRecheck() } finally { busy = false } } } },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Orange, contentColor = Color.Black),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (busy) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(color = Color.Black, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    Text("Checking...", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            } else {
                Text("Re-check Approval", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }

        // ── Contact ──────────────────────────────────────────────
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Contact", color = TextMuted, fontSize = 12.sp, letterSpacing = 0.8.sp)
                listOf(
                    Triple("📢  WhatsApp Channel",   GreenWa,             "https://whatsapp.com/channel/0029Va9UKCmAzNZFCsqkMf1O"),
                    Triple("💬  WhatsApp Community", GreenWa,             "https://chat.whatsapp.com/Eu1VZJfFpaz7gJxoGr4PvR"),
                    Triple("📸  @chand.tricker",     Color(0xFFE1306C),   "https://www.instagram.com/chand.tricker?igsh=c2dhbHFyZXdrZmpp"),
                ).forEach { (label, tint, url) ->
                    TextButton(
                        onClick = {
                            try { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
                            catch (_: Exception) {}
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = tint),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        Text(label, fontWeight = FontWeight.Medium, fontSize = 14.sp, modifier = Modifier.fillMaxWidth())
                    }
                    HorizontalDivider(color = Divider, thickness = 0.5.dp)
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}
