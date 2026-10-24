package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Obsidian & Luxury Purple Neumorphic Palette (Zero Green)
val DarkCanvas = Color(0xFF090810)          // Deep obsidian black canvas base
val DarkSurface = Color(0xFF13101F)         // Extruded neumorphic card surface
val DarkSurfaceElevated = Color(0xFF1B172C) // Elevated floating panels
val DarkSurfaceSunken = Color(0xFF0E0C17)   // Concave / pressed / inset well
val DarkBorder = Color(0xFF28213D)          // Subtle violet boundary
val DarkBorderSubtle = Color(0xFF1C172E)    // Ambient edge border
val DarkHover = Color(0xFF221C38)           // Hover / tap state

// Vibrant Electric Purple & Neon Lavender (Signature Accent)
val PurplePrimary = Color(0xFFA855F7)       // Primary vibrant purple (M3 primary)
val PurpleAccent = Color(0xFF8B5CF6)        // Rich violet for buttons & icons
val PurpleLight = Color(0xFFC084FC)         // Neon lavender for badges, highlights, and positive values
val PurpleUltraLight = Color(0xFFE9D5FF)    // Pale lavender text
val PurpleBg = Color(0xFF20163A)           // Container background for positive/verified pills
val PurpleBorder = Color(0xFF4C337D)       // Badge border
val PurpleGlow = Color(0x38A855F7)         // Outer glow for neumorphic elevations

// Secondary Accents (Refined Warning & Alert Tones - No Green)
val WarningRose = Color(0xFFF43F5E)        // Soft neon rose for zombie subscriptions & cancellation
val WarningRoseBg = Color(0xFF2E0F1A)      // Rose pill background
val WarningRoseBorder = Color(0xFF6B1D35)

val AmberAlert = Color(0xFFF59E0B)         // Warm amber for AI negotiation leverage & dark patterns
val AmberBg = Color(0xFF2E1A08)            // Amber pill background
val AmberBorder = Color(0xFF663B0E)

val SkyAccent = Color(0xFF38BDF8)          // Sky cyan for enterprise SSO & integrations
val SkyBg = Color(0xFF0C243B)
val SkyBorder = Color(0xFF16456E)

// High-Contrast Crisp Typography Tones
val TextPrimary = Color(0xFFF8FAFC)        // Crisp white for headings and values
val TextSecondary = Color(0xFFCBD5E1)      // Muted silver for body text
val TextMuted = Color(0xFF8E88A8)          // Soft violet-slate for secondary captions
val TextPlaceholder = Color(0xFF554E70)    // Muted placeholder

// Legacy aliases mapped to Obsidian-Purple (strictly avoiding any green tones)
val WhiteBackground = DarkCanvas
val WhiteSurface = DarkSurface
val WhiteSurfaceElevated = DarkSurfaceElevated
val WhiteBorder = DarkBorder
val WhiteBorderSubtle = DarkBorderSubtle
val WhiteHover = DarkHover

// Green replacements (redirected to Electric Purple & Neon Lavender)
val SoftSage = PurpleLight
val SoftSageBg = PurpleBg
val SoftSageBorder = PurpleBorder
val SupabaseGreen = PurplePrimary
val SupabaseBg = PurpleBg
val SupabaseBorder = PurpleBorder

val SoftRose = WarningRose
val SoftRoseBg = WarningRoseBg
val SoftRoseBorder = WarningRoseBorder

val SoftAmber = AmberAlert
val SoftAmberBg = AmberBg
val SoftAmberBorder = AmberBorder

val SoftSky = SkyAccent
val SoftSkyBg = SkyBg
val SoftSkyBorder = SkyBorder

