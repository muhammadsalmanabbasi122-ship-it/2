package com.ghosttype.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ghosttype.utils.SettingsStore

private val Orange = Color(0xFFFF8C00)
private val GreenWa = Color(0xFF25D366)
private val RedYt   = Color(0xFFFF0000)

@Composable
fun TasksDialog(onUnlocked: () -> Unit) {
    val ctx   = LocalContext.current
    val prefs = SettingsStore.prefs(ctx)

    var waOpened  by remember { mutableStateOf(false) }
    var ytOpened  by remember { mutableStateOf(false) }
    var waDone    by remember { mutableStateOf(false) }
    var ytDone    by remember { mutableStateOf(false) }

    val bothDone = waDone && ytDone

    Dialog(
        onDismissRequest = { /* non-dismissible until unlocked */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── Header ─────────────────────────────────────
                Text("🎁", fontSize = 40.sp)
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Unlock GhostType Pro",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Complete 2 quick tasks to get free access",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)

                // ── Task 1 — WhatsApp ──────────────────────────
                TaskRow(
                    number = "1",
                    emoji = "📢",
                    title = "Follow on WhatsApp",
                    subtitle = "Join our official channel",
                    accentColor = GreenWa,
                    buttonLabel = if (waOpened) "Opened ✓" else "Follow",
                    isDone = waDone,
                    onAction = {
                        try {
                            ctx.startActivity(
                                Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://whatsapp.com/channel/0029VaZrEGYIN9ih4PxcFQ33"))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                        } catch (_: Exception) {}
                        waOpened = true
                    },
                    onToggleDone = { if (waOpened) waDone = !waDone }
                )

                // ── Task 2 — YouTube ───────────────────────────
                TaskRow(
                    number = "2",
                    emoji = "▶️",
                    title = "Subscribe on YouTube",
                    subtitle = "@chandtricker",
                    accentColor = RedYt,
                    buttonLabel = if (ytOpened) "Opened ✓" else "Subscribe",
                    isDone = ytDone,
                    onAction = {
                        try {
                            ctx.startActivity(
                                Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://www.youtube.com/@chandtricker"))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                        } catch (_: Exception) {}
                        ytOpened = true
                    },
                    onToggleDone = { if (ytOpened) ytDone = !ytDone }
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)

                // ── Progress indicator ─────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TaskPill(done = waDone, label = "WhatsApp", color = GreenWa, modifier = Modifier.weight(1f))
                    TaskPill(done = ytDone, label = "YouTube",  color = RedYt,   modifier = Modifier.weight(1f))
                }

                // ── Unlock button ──────────────────────────────
                AnimatedContent(targetState = bothDone, label = "unlock") { ready ->
                    Button(
                        onClick = {
                            if (ready) {
                                prefs.edit().putBoolean(SettingsStore.KEY_TASKS_UNLOCKED, true).apply()
                                onUnlocked()
                            }
                        },
                        enabled = ready,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange,
                            contentColor = Color.Black,
                            disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) {
                        Text(
                            if (ready) "🔓  Unlock App" else "Complete both tasks to unlock",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskRow(
    number: String,
    emoji: String,
    title: String,
    subtitle: String,
    accentColor: Color,
    buttonLabel: String,
    isDone: Boolean,
    onAction: () -> Unit,
    onToggleDone: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(accentColor.copy(alpha = if (isDone) 0.10f else 0.05f))
            .border(
                1.dp,
                if (isDone) accentColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(14.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Step number
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (isDone) "✓" else number,
                color = accentColor,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
        }

        // Info
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(emoji, fontSize = 14.sp)
                Text(title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }

        // Action column
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = onAction,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(buttonLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            if (!isDone) {
                TextButton(
                    onClick = onToggleDone,
                    enabled = !isDone,
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                    modifier = Modifier.height(22.dp)
                ) {
                    Text(
                        "Mark done",
                        color = accentColor.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
            } else {
                Text("Done ✓", color = accentColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun TaskPill(done: Boolean, label: String, color: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (done) color.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, if (done) color.copy(alpha = 0.4f) else Color.Transparent, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(if (done) "✓" else "○", color = if (done) color else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(label, color = if (done) color else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
