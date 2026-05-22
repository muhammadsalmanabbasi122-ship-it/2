package com.ghosttype.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghosttype.security.DeviceId
import com.ghosttype.utils.SettingsStore

private val Orange  = Color(0xFFFF8C00)
private val GreenWa = Color(0xFF25D366)
private val BlueTg  = Color(0xFF0088CC)
private val PinkIg  = Color(0xFFE1306C)
private val Gold    = Color(0xFFFFD700)
private val SkyBlue = Color(0xFF66BBFF)

private data class Plan(
    val emoji: String,
    val tag: String?,
    val name: String,
    val duration: String,
    val price: String,
    val accent: Color,
    val perks: List<String>,
    val featured: Boolean = false
)

private val PLANS = listOf(
    Plan("🎯", null,     "Trial",     "7 Days",   "FREE",    SkyBlue,
        listOf("Full keyboard access", "All themes", "Auto-Type basic")),
    Plan("📅", null,     "Monthly",   "1 Month",  "Rs 50",   Orange,
        listOf("Everything in Trial", "Priority support", "All features")),
    Plan("🔥", "POPULAR","Quarterly", "3 Months", "Rs 120",  Orange,
        listOf("Everything in Monthly", "Save Rs 30", "Best value"), featured = true),
    Plan("⚡", null,     "Half Year", "6 Months", "Rs 250",  Orange,
        listOf("Everything in Quarterly", "Save Rs 50", "Early access")),
    Plan("👑", "BEST",   "Lifetime",  "Forever",  "Rs 500",  Gold,
        listOf("Everything, forever", "One-time payment", "All updates free"), featured = true),
)

@Composable
fun PlansScreen() {
    val ctx      = LocalContext.current
    val prefs    = remember { SettingsStore.prefs(ctx) }
    val deviceId = remember { DeviceId.get(ctx) }

    var selectedPlan by remember {
        mutableStateOf(
            prefs.getString(SettingsStore.KEY_ACTIVE_PLAN_NAME, "")?.let { saved ->
                PLANS.find { it.name == saved }
            }
        )
    }
    var userName by remember {
        mutableStateOf(prefs.getString(SettingsStore.KEY_PLANS_USER_NAME, "") ?: "")
    }
    var nameError by remember { mutableStateOf(false) }
    var planError  by remember { mutableStateOf(false) }

    val readyToSend = selectedPlan != null && userName.isNotBlank()

    val waMsg = if (readyToSend) buildString {
        append("*GhostType Pro — Plan Request*\n\n")
        append("👤 *Name:* $userName\n")
        append("🔑 *Key:* $deviceId\n")
        append("📦 *Plan:* ${selectedPlan!!.name} (${selectedPlan!!.duration}) — ${selectedPlan!!.price}")
    } else ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ── Title ─────────────────────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Plans", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.ExtraBold, fontSize = 26.sp)
            Text("Complete all steps below then send your request to CHAND.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        }

        // ══════════════════════════════════════════════════════
        // STEP 1 — Your Device Key
        // ══════════════════════════════════════════════════════
        StepHeader(number = "1", title = "Your Device Key", done = true)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, Orange.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🔑", fontSize = 22.sp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Device Key", color = Orange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(
                    deviceId,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Text("Send this key to CHAND for activation", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            }
        }

        // ══════════════════════════════════════════════════════
        // STEP 2 — Your Name (required)
        // ══════════════════════════════════════════════════════
        StepHeader(number = "2", title = "Your Name", done = userName.isNotBlank())

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    1.dp,
                    when {
                        nameError          -> Color(0xFFE53935).copy(alpha = 0.7f)
                        userName.isNotBlank() -> Color(0xFF4CAF50).copy(alpha = 0.5f)
                        else               -> MaterialTheme.colorScheme.outlineVariant
                    },
                    RoundedCornerShape(14.dp)
                )
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Your Name", color = Orange, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("*required*", color = Color(0xFFE53935), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
            OutlinedTextField(
                value = userName,
                onValueChange = {
                    userName = it
                    nameError = false
                    prefs.edit().putString(SettingsStore.KEY_PLANS_USER_NAME, it).apply()
                },
                placeholder = { Text("e.g. Ali Hassan", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                singleLine = true,
                isError = nameError,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    errorBorderColor = Color(0xFFE53935),
                    cursorColor = Orange
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )
            if (nameError) {
                Text("⚠ Please enter your name", color = Color(0xFFE53935), fontSize = 12.sp)
            }
        }

        // ══════════════════════════════════════════════════════
        // STEP 3 — Choose a Plan (required)
        // ══════════════════════════════════════════════════════
        StepHeader(number = "3", title = "Choose a Plan", done = selectedPlan != null)

        if (planError && selectedPlan == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE53935).copy(alpha = 0.10f))
                    .border(1.dp, Color(0xFFE53935).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("⚠ Please select a plan", color = Color(0xFFE53935), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        PLANS.forEach { plan ->
            val isSelected = selectedPlan?.name == plan.name
            PlanCard(
                plan = plan,
                isSelected = isSelected,
                onClick = {
                    selectedPlan = plan
                    planError = false
                    prefs.edit()
                        .putString(SettingsStore.KEY_ACTIVE_PLAN_NAME, plan.name)
                        .putString(SettingsStore.KEY_ACTIVE_PLAN_PRICE, plan.price)
                        .putString(SettingsStore.KEY_ACTIVE_PLAN_DURATION, plan.duration)
                        .apply()
                }
            )
        }

        // ══════════════════════════════════════════════════════
        // STEP 4 — Send to CHAND
        // ══════════════════════════════════════════════════════
        StepHeader(number = "4", title = "Send to CHAND", done = false)

        // Summary card — visible when both filled
        AnimatedVisibility(visible = readyToSend) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF4CAF50).copy(alpha = 0.08f))
                    .border(1.dp, Color(0xFF4CAF50).copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("✅ Ready to Send", color = Color(0xFF4CAF50), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                HorizontalDivider(color = Color(0xFF4CAF50).copy(alpha = 0.2f), thickness = 0.5.dp)
                InfoRow(label = "Name", value = userName)
                InfoRow(label = "Key", value = deviceId)
                InfoRow(label = "Plan", value = "${selectedPlan?.name} (${selectedPlan?.duration}) — ${selectedPlan?.price}")
            }
        }

        // Hint when not ready
        AnimatedVisibility(visible = !readyToSend) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    when {
                        userName.isBlank() && selectedPlan == null -> "Complete Step 2 (name) and Step 3 (plan) first"
                        userName.isBlank() -> "Complete Step 2 — enter your name"
                        else -> "Complete Step 3 — select a plan"
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 3 Send buttons
        SendButton(
            emoji = "📲",
            label = "Send on WhatsApp",
            subLabel = "+923017787729",
            color = GreenWa,
            enabled = readyToSend,
            onClick = {
                if (!readyToSend) { nameError = userName.isBlank(); planError = selectedPlan == null; return@SendButton }
                val encoded = Uri.encode(waMsg)
                ctx.openUrl("https://wa.me/923017787729?text=$encoded")
            }
        )

        SendButton(
            emoji = "✈️",
            label = "Send on Telegram",
            subLabel = "@CHANDTRICKER",
            color = BlueTg,
            enabled = readyToSend,
            onClick = {
                if (!readyToSend) { nameError = userName.isBlank(); planError = selectedPlan == null; return@SendButton }
                val encoded = Uri.encode(waMsg)
                ctx.openUrl("https://t.me/CHANDTRICKER?text=$encoded")
            }
        )

        SendButton(
            emoji = "📸",
            label = "Send on Instagram",
            subLabel = "@chand.tricker",
            color = PinkIg,
            enabled = readyToSend,
            onClick = {
                if (!readyToSend) { nameError = userName.isBlank(); planError = selectedPlan == null; return@SendButton }
                ctx.openUrl("https://www.instagram.com/chand.tricker?igsh=c2dhbHFyZXdrZmpp")
            }
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
        ) {
            Text(
                "ℹ After sending, CHAND will verify your request and activate your plan within a few hours.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("$label:", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(40.dp))
        Text(value, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

private fun android.content.Context.openUrl(url: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (_: Exception) {}
}

// ── Composables ───────────────────────────────────────────────

@Composable
private fun StepHeader(number: String, title: String, done: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (done) Color(0xFF4CAF50).copy(alpha = 0.15f) else Orange.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (done) "✓" else number,
                color = if (done) Color(0xFF4CAF50) else Orange,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp
            )
        }
        Text(
            "Step $number — $title",
            color = if (done) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun PlanCard(plan: Plan, isSelected: Boolean, onClick: () -> Unit) {
    val Green = Color(0xFF4CAF50)
    val borderColor = when {
        isSelected    -> Green.copy(alpha = 0.7f)
        plan.featured -> plan.accent.copy(alpha = 0.5f)
        else          -> MaterialTheme.colorScheme.outlineVariant
    }
    val bgBrush = when {
        isSelected    -> Brush.verticalGradient(listOf(Green.copy(alpha = 0.12f), Green.copy(alpha = 0.04f)))
        plan.featured -> Brush.verticalGradient(listOf(plan.accent.copy(alpha = 0.10f), plan.accent.copy(alpha = 0.03f)))
        else          -> Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(bgBrush)
            .border(if (isSelected || plan.featured) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) Green.copy(alpha = 0.18f) else plan.accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Text(if (isSelected) "✅" else plan.emoji, fontSize = 26.sp)
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(plan.name, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                    if (isSelected) {
                        Text("SELECTED", color = Green, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Green.copy(alpha = 0.14f)).padding(horizontal = 6.dp, vertical = 2.dp))
                    } else if (plan.tag != null) {
                        Text(plan.tag, color = if (plan.accent == Gold) Color(0xFF7A5F00) else plan.accent, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(plan.accent.copy(alpha = 0.15f)).padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                Text(plan.duration, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }

            Text(plan.price, color = if (isSelected) Green else plan.accent, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
        }

        HorizontalDivider(color = borderColor.copy(alpha = 0.4f), thickness = 0.5.dp)

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            plan.perks.forEach { perk ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("✓", color = if (isSelected) Green else plan.accent, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    Text(perk, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                }
            }
        }

        if (!isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(plan.accent.copy(alpha = 0.08f))
                    .padding(vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Tap to select this plan", color = plan.accent.copy(alpha = 0.8f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SendButton(
    emoji: String, label: String, subLabel: String,
    color: Color, enabled: Boolean, onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.fillMaxWidth().height(56.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(emoji, fontSize = 20.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subLabel, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
            }
            Text("→", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color.White.copy(alpha = 0.7f))
        }
    }
}
