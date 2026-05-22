package com.ghosttype.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghosttype.security.Obf
import com.ghosttype.security.ObfConstants

private const val APP_VERSION = "1.10.0"
private val Orange = Color(0xFFFF8C00)

@Composable
fun AboutScreen() {
    val ctx         = LocalContext.current
    val ownerName   = remember { Obf.decode(ctx, ObfConstants.OWNER_NAME).ifBlank { "CHAND" } }
    val ownerTeam   = remember { Obf.decode(ctx, ObfConstants.OWNER_TEAM).ifBlank { "ATF Team" } }
    val instaUrl    = remember { Obf.decode(ctx, ObfConstants.INSTAGRAM_URL) }
    val waChannel   = remember { Obf.decode(ctx, ObfConstants.WA_CHANNEL_URL) }
    val waCommunity = remember { Obf.decode(ctx, ObfConstants.WA_COMMUNITY_URL) }
    val licenseLine = remember { Obf.decode(ctx, ObfConstants.LICENSE_LINE).ifBlank { "CHAND · ATF Team. All rights reserved." } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {

        // ── Hero banner ──────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            Orange.copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(top = 36.dp, bottom = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // App icon placeholder — orange ghost circle
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Orange.copy(alpha = 0.15f))
                        .border(2.dp, Orange.copy(alpha = 0.4f), RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👻", fontSize = 38.sp)
                }

                Text(
                    "GhostType Pro",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    letterSpacing = 0.3.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VersionChip("v$APP_VERSION")
                    VersionChip("Android 7.0+")
                }
                Text(
                    "Professional keyboard · Auto-Type engine",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ── Features grid ─────────────────────────────────
            SectionLabel("Features")
            val features = listOf(
                "⌨️" to "IME Keyboard Service",
                "🤖" to "Auto-Type Engine",
                "🎨" to "Custom Themes",
                "🔤" to "36 Built-in Fonts",
                "📋" to "Clipboard History",
                "🌐" to "Multi-language (EN/UR/AR)",
                "📡" to "Floating Send Pointer",
                "⚡" to "Live WPM Badge"
            )
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                features.chunked(2).forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        row.forEach { (emoji, label) ->
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 7.dp, horizontal = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(emoji, fontSize = 16.sp)
                                Text(
                                    label,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                    if (row != features.chunked(2).last()) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                    }
                }
            }

            // ── Developer card ─────────────────────────────────
            SectionLabel("Developer")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Orange.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        ownerName.take(1).uppercase(),
                        color = Orange,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(ownerName, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(ownerTeam, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50))
                        )
                        Text("Active developer", color = Color(0xFF4CAF50), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // ── Connect section ────────────────────────────────
            SectionLabel("Connect with us")

            // WhatsApp Channel
            SocialCard(
                iconEmoji = "📢",
                platformName = "WhatsApp Channel",
                handle = "GhostType Pro",
                tagline = "Updates & announcements",
                gradientStart = Color(0xFF25D366),
                gradientEnd   = Color(0xFF128C7E),
                badgeText = "CHANNEL",
                onClick = { openUrl(ctx, waChannel.ifBlank { "https://whatsapp.com" }) }
            )

            // WhatsApp Community
            SocialCard(
                iconEmoji = "💬",
                platformName = "WhatsApp Community",
                handle = "ATF Team",
                tagline = "Join the user community",
                gradientStart = Color(0xFF128C7E),
                gradientEnd   = Color(0xFF075E54),
                badgeText = "COMMUNITY",
                onClick = { openUrl(ctx, waCommunity.ifBlank { "https://whatsapp.com" }) }
            )

            // Instagram
            SocialCard(
                iconEmoji = "📸",
                platformName = "Instagram",
                handle = "@chand.tricker",
                tagline = "Follow for updates",
                gradientStart = Color(0xFFE1306C),
                gradientEnd   = Color(0xFF833AB4),
                badgeText = "FOLLOW",
                onClick = { openUrl(ctx, instaUrl.ifBlank { "https://instagram.com" }) }
            )

            // ── Legal ──────────────────────────────────────────
            Spacer(Modifier.height(4.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} $licenseLine",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Does not collect or transmit typing data",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text.uppercase(),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(start = 2.dp, top = 4.dp)
    )
}

@Composable
private fun VersionChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Orange.copy(alpha = 0.12f))
            .border(1.dp, Orange.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(text, color = Orange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SocialCard(
    iconEmoji: String,
    platformName: String,
    handle: String,
    tagline: String,
    gradientStart: Color,
    gradientEnd: Color,
    badgeText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(gradientStart.copy(alpha = 0.18f), gradientEnd.copy(alpha = 0.10f))
                )
            )
            .border(
                1.5.dp,
                Brush.horizontalGradient(listOf(gradientStart.copy(alpha = 0.6f), gradientEnd.copy(alpha = 0.3f))),
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon circle with gradient ring
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(gradientStart.copy(alpha = 0.30f), gradientEnd.copy(alpha = 0.20f))
                        )
                    )
                    .border(
                        1.dp,
                        Brush.verticalGradient(listOf(gradientStart.copy(alpha = 0.7f), gradientEnd.copy(alpha = 0.4f))),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(iconEmoji, fontSize = 26.sp)
            }

            // Text info
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        platformName,
                        color = gradientStart,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(gradientStart.copy(alpha = 0.18f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(badgeText, color = gradientStart, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
                Text(
                    handle,
                    color = Color.White.copy(alpha = 0.90f),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Text(
                    tagline,
                    color = Color.White.copy(alpha = 0.55f),
                    fontSize = 11.sp
                )
            }

            // Arrow
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(gradientStart.copy(alpha = 0.20f)),
                contentAlignment = Alignment.Center
            ) {
                Text("→", color = gradientStart, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

private fun openUrl(ctx: android.content.Context, url: String) {
    try { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
    catch (_: Exception) {}
}
