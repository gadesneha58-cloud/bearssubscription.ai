package com.example.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.TextPrimary
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Reusable Google Pay / PhonePe-inspired Fintech Currency Display.
 * Uses bold tabular Space Grotesk numerals with Indian numbering formatting (e.g., ₹ 1,49,999.00).
 */
@Composable
fun FintechMoneyText(
  amount: Double,
  modifier: Modifier = Modifier,
  currencySymbol: String = "₹",
  color: Color = TextPrimary,
  fontSize: TextUnit = 28.sp,
  fontWeight: FontWeight = FontWeight.Bold,
  showDecimals: Boolean = true,
  cadenceSuffix: String? = null
) {
  val symbols = DecimalFormatSymbols(Locale.ENGLISH)
  val formatter = if (showDecimals && amount % 1.0 != 0.0) {
    DecimalFormat("#,##,##0.00", symbols)
  } else {
    DecimalFormat("#,##,##0", symbols)
  }

  val formattedNumber = formatter.format(amount)
  val parts = formattedNumber.split(".")
  val integerPart = parts[0]
  val decimalPart = if (parts.size > 1) ".${parts[1]}" else ""

  Row(
    modifier = modifier,
    verticalAlignment = Alignment.Bottom
  ) {
    // Rupee / Currency Symbol
    Text(
      text = currencySymbol,
      fontFamily = SpaceGroteskFontFamily,
      fontWeight = FontWeight.SemiBold,
      fontSize = (fontSize.value * 0.72f).sp,
      color = color.copy(alpha = 0.9f),
      modifier = Modifier.padding(bottom = (fontSize.value * 0.12f).dp, end = 3.dp)
    )

    // Main Integer Digits
    Text(
      text = integerPart,
      fontFamily = SpaceGroteskFontFamily,
      fontWeight = fontWeight,
      fontSize = fontSize,
      color = color,
      letterSpacing = (-0.5).sp
    )

    // Decimals in sleek secondary size
    if (decimalPart.isNotEmpty()) {
      Text(
        text = decimalPart,
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = (fontSize.value * 0.65f).sp,
        color = color.copy(alpha = 0.75f),
        modifier = Modifier.padding(bottom = (fontSize.value * 0.12f).dp)
      )
    }

    // Optional Cadence Suffix (e.g. " / mo" or " / yr")
    if (!cadenceSuffix.isNullOrBlank()) {
      Text(
        text = cadenceSuffix,
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = (fontSize.value * 0.45f).sp,
        color = color.copy(alpha = 0.6f),
        modifier = Modifier.padding(bottom = (fontSize.value * 0.14f).dp, start = 4.dp)
      )
    }
  }
}
