package com.ghosttype.utils

import android.content.Context
import android.graphics.Color

data class KeyboardTheme(
    val id: String,
    val name: String,
    val keyboardBg: Int,
    val keyBg: Int,
    val keyText: Int,
    val suggestionBg: Int,
    val pressedKey: Int,
    val accent: Int
)

object ThemeManager {
    val BUILT_IN: List<KeyboardTheme> = listOf(
        // ===== ORIGINALS =====
        KeyboardTheme("dark", "Dark", Color.parseColor("#0D0D0D"), Color.parseColor("#1F1F1F"),
            Color.WHITE, Color.parseColor("#161616"), Color.parseColor("#FF8C00"), Color.parseColor("#FF8C00")),
        KeyboardTheme("oled", "OLED Black", Color.BLACK, Color.parseColor("#0A0A0A"),
            Color.WHITE, Color.BLACK, Color.parseColor("#FF8C00"), Color.parseColor("#FF8C00")),
        KeyboardTheme("orange_glow", "Orange Glow", Color.BLACK, Color.parseColor("#FF8C00"),
            Color.BLACK, Color.parseColor("#1A1A1A"), Color.parseColor("#FFB347"), Color.parseColor("#FF8C00")),
        KeyboardTheme("neon", "Neon Green", Color.parseColor("#0A0A0A"), Color.parseColor("#0F1F0F"),
            Color.parseColor("#39FF14"), Color.parseColor("#0A140A"), Color.parseColor("#39FF14"), Color.parseColor("#39FF14")),
        KeyboardTheme("ice", "Ice Blue", Color.parseColor("#0A1224"), Color.parseColor("#142244"),
            Color.parseColor("#9FD8FF"), Color.parseColor("#0E1A33"), Color.parseColor("#3FA9FF"), Color.parseColor("#3FA9FF")),
        KeyboardTheme("white", "Minimal White", Color.WHITE, Color.parseColor("#F0F0F0"),
            Color.BLACK, Color.parseColor("#FAFAFA"), Color.parseColor("#FF8C00"), Color.parseColor("#FF8C00")),
        // ===== GLASS (Gboard-style frosted look) =====
        // Translucent keyboard background + semi-transparent key tiles
        // so the wallpaper / chat behind the keyboard subtly shows through,
        // mimicking Gboard's "glass" / liquid theme. Hex alpha is the
        // FIRST byte: 0x66 ≈ 40% opacity. Works best when the user also
        // sets a background image, but stands on its own over the dark
        // keyboard surface too.
        KeyboardTheme(
            id = "glass",
            name = "Glass (Gboard-style)",
            keyboardBg = Color.parseColor("#66101010"),  // ~40% black wash
            keyBg      = Color.parseColor("#80FFFFFF"),  // ~50% white tiles
            keyText    = Color.WHITE,
            suggestionBg = Color.parseColor("#33000000"),
            pressedKey = Color.parseColor("#CCFFFFFF"),
            accent     = Color.parseColor("#FF8C00")
        ),

        // =====================================================================
        // CUSTOM THEMES — added so the user has a real catalog to pick from.
        // Color choices follow a few rules so every theme stays usable:
        //   • keyText must contrast strongly against keyBg (WCAG AA at least)
        //   • pressedKey is a noticeably different shade of keyBg so taps
        //     read clearly even with no haptic feedback
        //   • keyboardBg + suggestionBg sit one shade apart so the top strip
        //     doesn't blend into the keys
        //   • accent is the brand-style highlight used for ≡ menu, active
        //     pickers, "Start" buttons in Auto-Type, etc.
        // =====================================================================

        // 1) CANDY PASTEL — matches the cute pastel-blue Gboard reference
        // the user shared (light pastel blue surface, near-white keys with
        // a subtle blue tint, deep-navy text, soft pink accent). Default
        // recommendation for users who want a "soft / sweet" look.
        KeyboardTheme(
            id = "candy_pastel",
            name = "Candy Pastel",
            keyboardBg = Color.parseColor("#DCEEFF"),
            keyBg      = Color.parseColor("#F5FBFF"),
            keyText    = Color.parseColor("#2C3E66"),
            suggestionBg = Color.parseColor("#E8F4FF"),
            pressedKey = Color.parseColor("#B0DAFF"),
            accent     = Color.parseColor("#FF9FB6")
        ),
        // 2) BUBBLEGUM PINK — playful pink palette
        KeyboardTheme(
            id = "bubblegum",
            name = "Bubblegum Pink",
            keyboardBg = Color.parseColor("#FFE4EE"),
            keyBg      = Color.parseColor("#FFF4F8"),
            keyText    = Color.parseColor("#8A2A4A"),
            suggestionBg = Color.parseColor("#FFD6E4"),
            pressedKey = Color.parseColor("#FFB3CC"),
            accent     = Color.parseColor("#FF4D88")
        ),
        // 3) MINT CREAM — fresh greens with cream tiles
        KeyboardTheme(
            id = "mint_cream",
            name = "Mint Cream",
            keyboardBg = Color.parseColor("#D4F2E2"),
            keyBg      = Color.parseColor("#FFFFFF"),
            keyText    = Color.parseColor("#1F4D38"),
            suggestionBg = Color.parseColor("#E8F8F0"),
            pressedKey = Color.parseColor("#A8E0C2"),
            accent     = Color.parseColor("#2EB872")
        ),
        // 4) LAVENDER DREAM — calm purple/lavender
        KeyboardTheme(
            id = "lavender",
            name = "Lavender Dream",
            keyboardBg = Color.parseColor("#E7DEFF"),
            keyBg      = Color.parseColor("#F6F1FF"),
            keyText    = Color.parseColor("#3A2466"),
            suggestionBg = Color.parseColor("#EDE3FF"),
            pressedKey = Color.parseColor("#C9B7FF"),
            accent     = Color.parseColor("#8B5CF6")
        ),
        // 5) SUNSET ORANGE — warm orange/peach gradient feel
        KeyboardTheme(
            id = "sunset",
            name = "Sunset Orange",
            keyboardBg = Color.parseColor("#FFE0CC"),
            keyBg      = Color.parseColor("#FFF2E6"),
            keyText    = Color.parseColor("#5A2A0A"),
            suggestionBg = Color.parseColor("#FFD6BB"),
            pressedKey = Color.parseColor("#FFB07A"),
            accent     = Color.parseColor("#FF6B35")
        ),
        // 6) ROSE GOLD — soft rose & cream luxury feel
        KeyboardTheme(
            id = "rose_gold",
            name = "Rose Gold",
            keyboardBg = Color.parseColor("#F8E1DC"),
            keyBg      = Color.parseColor("#FBEFEC"),
            keyText    = Color.parseColor("#6B2A30"),
            suggestionBg = Color.parseColor("#F2D5CE"),
            pressedKey = Color.parseColor("#E5A99F"),
            accent     = Color.parseColor("#C77F70")
        ),
        // 7) OCEAN BLUE — bright daytime blue
        KeyboardTheme(
            id = "ocean",
            name = "Ocean Blue",
            keyboardBg = Color.parseColor("#0B5394"),
            keyBg      = Color.parseColor("#1B6CB3"),
            keyText    = Color.WHITE,
            suggestionBg = Color.parseColor("#094782"),
            pressedKey = Color.parseColor("#3A8EDB"),
            accent     = Color.parseColor("#FFD23F")
        ),
        // 8) FOREST GREEN — moody dark green
        KeyboardTheme(
            id = "forest",
            name = "Forest Green",
            keyboardBg = Color.parseColor("#0F2A1E"),
            keyBg      = Color.parseColor("#1B4332"),
            keyText    = Color.parseColor("#D8F3DC"),
            suggestionBg = Color.parseColor("#0A1F15"),
            pressedKey = Color.parseColor("#2D6A4F"),
            accent     = Color.parseColor("#74C69D")
        ),
        // 9) MIDNIGHT PURPLE — deep night-club purple
        KeyboardTheme(
            id = "midnight_purple",
            name = "Midnight Purple",
            keyboardBg = Color.parseColor("#10072B"),
            keyBg      = Color.parseColor("#241047"),
            keyText    = Color.parseColor("#E0D4FF"),
            suggestionBg = Color.parseColor("#0A041A"),
            pressedKey = Color.parseColor("#3B1A78"),
            accent     = Color.parseColor("#B388FF")
        ),
        // 10) CHERRY RED — bold high-contrast red
        KeyboardTheme(
            id = "cherry",
            name = "Cherry Red",
            keyboardBg = Color.parseColor("#1A0608"),
            keyBg      = Color.parseColor("#3A0F12"),
            keyText    = Color.parseColor("#FFE5E8"),
            suggestionBg = Color.parseColor("#100406"),
            pressedKey = Color.parseColor("#7A1F26"),
            accent     = Color.parseColor("#FF3854")
        ),
        // 11) COFFEE MOCHA — warm browns
        KeyboardTheme(
            id = "coffee",
            name = "Coffee Mocha",
            keyboardBg = Color.parseColor("#2B1810"),
            keyBg      = Color.parseColor("#4A2C20"),
            keyText    = Color.parseColor("#F5E6D3"),
            suggestionBg = Color.parseColor("#1F100A"),
            pressedKey = Color.parseColor("#6B4030"),
            accent     = Color.parseColor("#D4A574")
        ),
        // 12) LEMON YELLOW — bright cheerful yellow
        KeyboardTheme(
            id = "lemon",
            name = "Lemon Yellow",
            keyboardBg = Color.parseColor("#FFF5C2"),
            keyBg      = Color.parseColor("#FFFCE6"),
            keyText    = Color.parseColor("#5C4500"),
            suggestionBg = Color.parseColor("#FFEFA8"),
            pressedKey = Color.parseColor("#FFE066"),
            accent     = Color.parseColor("#FFB300")
        ),
        // 13) SLATE GRAY — minimal neutral gray
        KeyboardTheme(
            id = "slate",
            name = "Slate Gray",
            keyboardBg = Color.parseColor("#2B2F36"),
            keyBg      = Color.parseColor("#3A4049"),
            keyText    = Color.parseColor("#E8EAED"),
            suggestionBg = Color.parseColor("#1F2329"),
            pressedKey = Color.parseColor("#5A626E"),
            accent     = Color.parseColor("#5DADE2")
        ),
        // 14) CYAN WAVE — vibrant teal/cyan
        KeyboardTheme(
            id = "cyan_wave",
            name = "Cyan Wave",
            keyboardBg = Color.parseColor("#00363D"),
            keyBg      = Color.parseColor("#005662"),
            keyText    = Color.parseColor("#B2EBF2"),
            suggestionBg = Color.parseColor("#002A30"),
            pressedKey = Color.parseColor("#00838F"),
            accent     = Color.parseColor("#26C6DA")
        ),
        // 15) SKY MORNING — soft daylight sky-blue, light theme
        KeyboardTheme(
            id = "sky_morning",
            name = "Sky Morning",
            keyboardBg = Color.parseColor("#E3F2FD"),
            keyBg      = Color.parseColor("#FFFFFF"),
            keyText    = Color.parseColor("#0D47A1"),
            suggestionBg = Color.parseColor("#BBDEFB"),
            pressedKey = Color.parseColor("#90CAF9"),
            accent     = Color.parseColor("#1E88E5")
        ),

        // =====================================================================
        // CUTE / AESTHETIC THEMES — matching the pastel Gboard screenshot
        // Light sky-blue body, pure-white puffy keys, dark slate-blue text.
        // Best with: rounded border style + 3D shadow ON (set in defaults).
        // =====================================================================

        // CUTE-1) CUTE SKY BLUE — pixel-accurate match to the reference image:
        // #BDD8F0 body, white keys, #3D5A7A text, soft blue suggestion strip
        KeyboardTheme(
            id = "cute_sky_blue",
            name = "🩵 Cute Sky Blue",
            keyboardBg   = Color.parseColor("#BDD8F0"),
            keyBg        = Color.parseColor("#FFFFFF"),
            keyText      = Color.parseColor("#3D5A7A"),
            suggestionBg = Color.parseColor("#D0E8FA"),
            pressedKey   = Color.parseColor("#9DC4E8"),
            accent       = Color.parseColor("#5FA8D3")
        ),

        // CUTE-2) COTTON CANDY — warmer pink-blue pastel mix
        KeyboardTheme(
            id = "cute_cotton_candy",
            name = "🍬 Cotton Candy",
            keyboardBg   = Color.parseColor("#D8EEFF"),
            keyBg        = Color.parseColor("#FFF5FB"),
            keyText      = Color.parseColor("#4A4470"),
            suggestionBg = Color.parseColor("#E8F6FF"),
            pressedKey   = Color.parseColor("#FFCCE8"),
            accent       = Color.parseColor("#FF9FC8")
        ),

        // CUTE-3) BABY CLOUD — near-white dreamy with lilac tones
        KeyboardTheme(
            id = "cute_baby_cloud",
            name = "☁️ Baby Cloud",
            keyboardBg   = Color.parseColor("#EAF4FF"),
            keyBg        = Color.parseColor("#FFFFFF"),
            keyText      = Color.parseColor("#5B6D8A"),
            suggestionBg = Color.parseColor("#F0F8FF"),
            pressedKey   = Color.parseColor("#C8DEFF"),
            accent       = Color.parseColor("#88AADD")
        ),

        // CUTE-4) PEACH BLOSSOM — warm coral-peach pastel
        KeyboardTheme(
            id = "cute_peach",
            name = "🍑 Peach Blossom",
            keyboardBg   = Color.parseColor("#FFE4D6"),
            keyBg        = Color.parseColor("#FFFFFF"),
            keyText      = Color.parseColor("#7A3A2A"),
            suggestionBg = Color.parseColor("#FFEEE5"),
            pressedKey   = Color.parseColor("#FFBFA3"),
            accent       = Color.parseColor("#FF7F5E")
        ),

        // CUTE-5) MINT BUNNY — cool mint green soft look
        KeyboardTheme(
            id = "cute_mint",
            name = "🐰 Mint Bunny",
            keyboardBg   = Color.parseColor("#C8F0E2"),
            keyBg        = Color.parseColor("#FFFFFF"),
            keyText      = Color.parseColor("#2A5A48"),
            suggestionBg = Color.parseColor("#DAFAF0"),
            pressedKey   = Color.parseColor("#96DFC4"),
            accent       = Color.parseColor("#3DAB80")
        ),

        // =====================================================================
        // VIP PREMIUM THEMES
        // =====================================================================

        // VIP-1) ROYAL GOLD — deep black base, rich 24-carat gold accents
        // The go-to VIP default: black keyboard, golden keys, white text.
        KeyboardTheme(
            id = "vip_royal_gold",
            name = "⭐ Royal Gold",
            keyboardBg = Color.parseColor("#0A0800"),
            keyBg      = Color.parseColor("#1A1400"),
            keyText    = Color.parseColor("#FFD700"),
            suggestionBg = Color.parseColor("#050400"),
            pressedKey = Color.parseColor("#B8860B"),
            accent     = Color.parseColor("#FFD700")
        ),

        // VIP-2) DIAMOND BLACK — pure black with icy silver-white shimmer
        KeyboardTheme(
            id = "vip_diamond",
            name = "💎 Diamond Black",
            keyboardBg = Color.parseColor("#050505"),
            keyBg      = Color.parseColor("#111111"),
            keyText    = Color.parseColor("#E8F4FF"),
            suggestionBg = Color.parseColor("#000000"),
            pressedKey = Color.parseColor("#C0D8F0"),
            accent     = Color.parseColor("#A8D8FF")
        ),

        // VIP-3) PLATINUM — cool silver-grey luxury
        KeyboardTheme(
            id = "vip_platinum",
            name = "🥈 Platinum",
            keyboardBg = Color.parseColor("#1A1C1E"),
            keyBg      = Color.parseColor("#2E3033"),
            keyText    = Color.parseColor("#E8E8E8"),
            suggestionBg = Color.parseColor("#111315"),
            pressedKey = Color.parseColor("#5A5C60"),
            accent     = Color.parseColor("#C0C0C0")
        ),

        // VIP-4) ROYAL PURPLE — deep violet, crown jewel feel
        KeyboardTheme(
            id = "vip_royal_purple",
            name = "👑 Royal Purple",
            keyboardBg = Color.parseColor("#0D0018"),
            keyBg      = Color.parseColor("#1E0035"),
            keyText    = Color.parseColor("#F0E6FF"),
            suggestionBg = Color.parseColor("#070010"),
            pressedKey = Color.parseColor("#5B00B5"),
            accent     = Color.parseColor("#D4A0FF")
        ),

        // VIP-5) EMERALD VIP — luxury dark green with gold trim
        KeyboardTheme(
            id = "vip_emerald",
            name = "💚 Emerald VIP",
            keyboardBg = Color.parseColor("#020F07"),
            keyBg      = Color.parseColor("#051A0C"),
            keyText    = Color.parseColor("#A8FFD0"),
            suggestionBg = Color.parseColor("#010805"),
            pressedKey = Color.parseColor("#0A5C28"),
            accent     = Color.parseColor("#FFD700")
        ),

        // VIP-6) BLOOD RUBY — dramatic deep red with gold glow
        KeyboardTheme(
            id = "vip_ruby",
            name = "🔴 Blood Ruby",
            keyboardBg = Color.parseColor("#0F0000"),
            keyBg      = Color.parseColor("#200000"),
            keyText    = Color.parseColor("#FFE5E5"),
            suggestionBg = Color.parseColor("#070000"),
            pressedKey = Color.parseColor("#700000"),
            accent     = Color.parseColor("#FF2222")
        )
    )

    fun current(ctx: Context): KeyboardTheme {
        val prefs = SettingsStore.prefs(ctx)
        val id = prefs.getString(SettingsStore.KEY_THEME, "dark") ?: "dark"
        val base = BUILT_IN.firstOrNull { it.id == id } ?: BUILT_IN[0]
        return base.copy(
            keyBg = prefs.getInt(SettingsStore.KEY_KEY_BG, base.keyBg),
            keyText = prefs.getInt(SettingsStore.KEY_KEY_TEXT, base.keyText),
            keyboardBg = prefs.getInt(SettingsStore.KEY_KB_BG, base.keyboardBg),
            suggestionBg = prefs.getInt(SettingsStore.KEY_SUGG_BG, base.suggestionBg),
            pressedKey = prefs.getInt(SettingsStore.KEY_PRESSED, base.pressedKey)
        )
    }

    /**
     * Apply a built-in theme. ALL six prefs are written in a SINGLE edit so
     * the IME's `OnSharedPreferenceChangeListener` always sees the new colors
     * already committed when it reloads. Previously the theme id was written
     * first, fired the listener, and reload() ran while the per-color prefs
     * still held the previous theme's values — so picking "Neon Green" would
     * keep the keys orange. Bug fix for "Built in theme not working".
     */
    fun setTheme(ctx: Context, id: String) {
        val t = BUILT_IN.firstOrNull { it.id == id }
        val ed = SettingsStore.prefs(ctx).edit()
        ed.putString(SettingsStore.KEY_THEME, id)
        if (t != null) {
            ed.putInt(SettingsStore.KEY_KEY_BG, t.keyBg)
              .putInt(SettingsStore.KEY_KEY_TEXT, t.keyText)
              .putInt(SettingsStore.KEY_KB_BG, t.keyboardBg)
              .putInt(SettingsStore.KEY_SUGG_BG, t.suggestionBg)
              .putInt(SettingsStore.KEY_PRESSED, t.pressedKey)
        }
        ed.apply()
    }
}
