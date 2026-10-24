package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val OutfitFontFamily = FontFamily(
    Font(R.font.outfit, FontWeight.Normal),
    Font(R.font.outfit, FontWeight.Bold)
)

val PlusJakartaSansFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.Bold)
)

val GreycliffHeavy = OutfitFontFamily
val GreycliffHeavyFontFamily = OutfitFontFamily
val AptosGrotesqueFontFamily = OutfitFontFamily
val SpaceGroteskFontFamily = PlusJakartaSansFontFamily

val Typography = Typography(
  displayLarge = TextStyle(
    fontFamily = GreycliffHeavy,
    fontWeight = FontWeight.Bold,
    fontSize = 38.sp,
    lineHeight = 44.sp,
    letterSpacing = (-0.8).sp
  ),
  headlineLarge = TextStyle(
    fontFamily = GreycliffHeavy,
    fontWeight = FontWeight.Bold,
    fontSize = 30.sp,
    lineHeight = 36.sp,
    letterSpacing = (-0.6).sp
  ),
  headlineMedium = TextStyle(
    fontFamily = GreycliffHeavy,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 30.sp,
    letterSpacing = (-0.4).sp
  ),
  titleLarge = TextStyle(
    fontFamily = GreycliffHeavy,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 26.sp,
    letterSpacing = (-0.3).sp
  ),
  titleMedium = TextStyle(
    fontFamily = GreycliffHeavy,
    fontWeight = FontWeight.Bold,
    fontSize = 17.sp,
    lineHeight = 22.sp,
    letterSpacing = (-0.2).sp
  ),
  bodyLarge = TextStyle(
    fontFamily = GreycliffHeavyFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    lineHeight = 22.sp,
    letterSpacing = (-0.15).sp
  ),
  bodyMedium = TextStyle(
    fontFamily = GreycliffHeavyFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = (-0.1).sp
  ),
  labelLarge = TextStyle(
    fontFamily = GreycliffHeavy,
    fontWeight = FontWeight.Bold,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.sp
  ),
  labelSmall = TextStyle(
    fontFamily = GreycliffHeavy,
    fontWeight = FontWeight.SemiBold,
    fontSize = 11.sp,
    lineHeight = 15.sp,
    letterSpacing = 0.2.sp
  )
)
