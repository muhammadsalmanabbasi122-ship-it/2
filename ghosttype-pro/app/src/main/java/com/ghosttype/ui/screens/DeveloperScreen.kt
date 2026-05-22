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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import coil.compose.SubcomposeAsyncImage
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Orange  = Color(0xFFFF8C00)
private val GreenWa = Color(0xFF25D366)
private val RedYt   = Color(0xFFFF0000)
private val PurpleIg = Color(0xFFE1306C)

@Composable
fun DeveloperScreen() {
    val ctx = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        // ── Hero ─────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Orange.copy(alpha = 0.18f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(top = 40.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Avatar — loads logo from CDN, falls back to "CT" text
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Orange.copy(alpha = 0.35f), Orange.copy(alpha = 0.10f))
                            )
                        )
                        .border(2.5.dp, Orange.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = "https://cdn.jsdelivr.net/gh/chanddark/Image1/images/icon.png",
                        contentDescription = "CHAND TRICKER logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        loading = {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("CT", color = Orange, fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, fontFamily = FontFamily.Default)
                            }
                        },
                        error = {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("CT", color = Orange, fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, fontFamily = FontFamily.Default)
                            }
                        }
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "CHAND TRICKER",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        "Android Developer & Content Creator",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF4CAF50)))
                        Text("Active", color = Color(0xFF4CAF50), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Bio ───────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("About Me", color = Orange, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp)
                Text(
                    "I am CHAND TRICKER — an Android developer and content creator passionate about building tools that make everyday tasks easier. " +
                    "I created GhostType Pro to give people a smart, powerful keyboard with an Auto-Type engine that works seamlessly in messaging apps.",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                Text(
                    "My goal is to keep building useful apps for the community — free tools, smart automation, and creative solutions. " +
                    "GhostType Pro is one of many projects under the ATF Team banner.",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }

            // ── Stats row ─────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard("👻", "GhostType Pro", "Flagship App", Modifier.weight(1f))
                StatCard("🛠️", "ATF Team", "Developer", Modifier.weight(1f))
                StatCard("📱", "Android", "Platform", Modifier.weight(1f))
            }

            // ── Skills ────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Skills & Tech", color = Orange, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp)
                listOf(
                    "Kotlin" to 0.90f,
                    "Jetpack Compose" to 0.85f,
                    "Android IME" to 0.92f,
                    "Auto-Type Engine" to 0.95f,
                    "UI/UX Design" to 0.80f
                ).forEach { (skill, level) ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(skill, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text("${(level * 100).toInt()}%", color = Orange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        LinearProgressIndicator(
                            progress = { level },
                            color = Orange,
                            trackColor = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(3.dp))
                        )
                    }
                }
            }

            // ── Quote ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Orange.copy(alpha = 0.08f))
                    .border(1.dp, Orange.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("❝", color = Orange, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Text(
                        "Build tools that solve real problems. Keep it simple, keep it powerful.",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 22.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    Text("— CHAND TRICKER", color = Orange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // ── Social links ──────────────────────────────────
            Text(
                "FIND ME ON",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(start = 2.dp, top = 4.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DevSocialBtn(
                    emoji = "▶️", label = "YouTube",
                    handle = "@chandtricker",
                    color = RedYt,
                    url = "https://www.youtube.com/@chandtricker",
                    ctx = ctx
                )
                DevSocialBtn(
                    emoji = "📢", label = "WhatsApp Channel",
                    handle = "GhostType Pro Updates",
                    color = GreenWa,
                    url = "https://whatsapp.com/channel/0029VaZrEGYIN9ih4PxcFQ33",
                    ctx = ctx
                )
                DevSocialBtn(
                    emoji = "📸", label = "Instagram",
                    handle = "@urkashif_chand",
                    color = PurpleIg,
                    url = "https://instagram.com/urkashif_chand",
                    ctx = ctx
                )
            }

            // ── Footer ────────────────────────────────────────
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("Made with ❤️ by CHAND TRICKER", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, textAlign = TextAlign.Center)
                Text("ATF Team · GhostType Pro", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun StatCard(emoji: String, value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(emoji, fontSize = 22.sp)
        Text(value, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center, maxLines = 1)
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun DevSocialBtn(
    emoji: String, label: String, handle: String,
    color: Color, url: String, ctx: android.content.Context
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .padding(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = {
                try { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
                catch (_: Exception) {}
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = color),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier.size(42.dp).clip(RoundedCornerShape(10.dp)).background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) { Text(emoji, fontSize = 20.sp) }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(label, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(handle, color = color.copy(alpha = 0.7f), fontSize = 12.sp)
                }
                Text("→", color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
