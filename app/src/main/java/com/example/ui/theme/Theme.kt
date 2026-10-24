package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Obsidian & Luxury Purple Theme (Strictly Zero Green)
private val DarkObsidianPurpleColorScheme = darkColorScheme(
  primary = PurplePrimary,
  onPrimary = Color.White,
  primaryContainer = PurpleBg,
  onPrimaryContainer = PurpleUltraLight,
  secondary = PurpleLight,
  onSecondary = Color.Black,
  secondaryContainer = PurpleBg,
  onSecondaryContainer = PurpleLight,
  tertiary = AmberAlert,
  onTertiary = Color.Black,
  tertiaryContainer = AmberBg,
  onTertiaryContainer = AmberAlert,
  background = DarkCanvas,
  onBackground = TextPrimary,
  surface = DarkSurface,
  onSurface = TextPrimary,
  surfaceVariant = DarkSurfaceElevated,
  onSurfaceVariant = TextSecondary,
  outline = DarkBorder,
  outlineVariant = DarkBorderSubtle,
  error = WarningRose,
  onError = Color.White,
  errorContainer = WarningRoseBg,
  onErrorContainer = WarningRose
)

@Composable
fun SubscriptionsAiTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = DarkObsidianPurpleColorScheme,
    typography = Typography,
    content = content
  )
}

