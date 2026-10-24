package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AptosGrotesqueFontFamily
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.SoftRose
import com.example.ui.theme.SoftSage
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun WasteBreakdownBar(
  totalSpend: Double,
  wasteAmount: Double,
  wastePercentage: Double,
  zombieAmount: Double,
  idleSeatsAmount: Double,
  modifier: Modifier = Modifier
) {
  val activeUtilityAmount = (totalSpend - wasteAmount).coerceAtLeast(0.0)

  val utilityWeight = if (totalSpend > 0) (activeUtilityAmount / totalSpend).toFloat().coerceIn(0.05f, 0.9f) else 0.65f
  val zombieWeight = if (totalSpend > 0) (zombieAmount / totalSpend).toFloat().coerceIn(0.05f, 0.5f) else 0.15f
  val idleWeight = if (totalSpend > 0) (idleSeatsAmount / totalSpend).toFloat().coerceIn(0.05f, 0.5f) else 0.20f

  Box(
    modifier = modifier
      .fillMaxWidth()
      .shadow(
        elevation = 6.dp,
        shape = RoundedCornerShape(18.dp),
        spotColor = Color(0xFF040307),
        ambientColor = Color(0xFF040307)
      )
      .clip(RoundedCornerShape(18.dp))
      .background(
        Brush.verticalGradient(listOf(DarkSurfaceElevated, DarkSurface))
      )
      .border(1.2.dp, DarkBorder, RoundedCornerShape(18.dp))
      .padding(16.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "SPEND LEAKAGE ANALYSIS (${wastePercentage.toInt()}%)",
          fontFamily = AptosGrotesqueFontFamily,
          color = TextMuted,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )
        Text(
          text = "₹${wasteAmount.toInt()}/mo reclaimable",
          fontFamily = AptosGrotesqueFontFamily,
          color = SoftRose,
          fontSize = 14.5.sp,
          fontWeight = FontWeight.ExtraBold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Multi-segment progress bar with enlarged height & smooth rounded edges
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(10.dp)
          .clip(RoundedCornerShape(5.dp))
          .background(Color(0xFF140F24))
      ) {
        Box(
          modifier = Modifier
            .weight(utilityWeight)
            .height(10.dp)
            .background(PurpleLight)
        )
        Box(
          modifier = Modifier
            .weight(zombieWeight)
            .height(10.dp)
            .background(SoftRose)
        )
        Box(
          modifier = Modifier
            .weight(idleWeight)
            .height(10.dp)
            .background(SkyAccent)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // High-Legibility Legend Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        LegendItem(
          color = PurpleLight,
          label = "Utility",
          amount = "₹${activeUtilityAmount.toInt()}"
        )
        LegendItem(
          color = SoftRose,
          label = "Zombie",
          amount = "₹${zombieAmount.toInt()}"
        )
        LegendItem(
          color = SkyAccent,
          label = "Idle SSO",
          amount = "₹${idleSeatsAmount.toInt()}"
        )
      }
    }
  }
}

@Composable
private fun LegendItem(
  color: Color,
  label: String,
  amount: String
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(8.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(6.dp))
    Text(
      text = "$label: $amount",
      fontFamily = AptosGrotesqueFontFamily,
      color = TextPrimary,
      fontSize = 12.5.sp,
      fontWeight = FontWeight.Bold
    )
  }
}
