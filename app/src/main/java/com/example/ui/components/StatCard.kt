package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AptosGrotesqueFontFamily
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun StatCard(
  title: String,
  value: String,
  subtitle: String,
  accentColor: Color,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  badgeText: String? = null,
  badgeColor: Color = accentColor,
  badgeBgColor: Color = Color(0xFF261D42),
  testTag: String = "stat_card"
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(126.dp)
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
      .padding(horizontal = 14.dp, vertical = 12.dp)
      .testTag(testTag)
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (icon != null) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
          }
          Text(
            text = title.uppercase(),
            fontFamily = AptosGrotesqueFontFamily,
            color = TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            maxLines = 1
          )
        }

        if (badgeText != null) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(badgeBgColor)
              .padding(horizontal = 7.dp, vertical = 3.dp)
          ) {
            Text(
              text = badgeText,
              fontFamily = AptosGrotesqueFontFamily,
              color = badgeColor,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1
            )
          }
        }
      }

      Column {
        Text(
          text = value,
          color = TextPrimary,
          fontFamily = SpaceGroteskFontFamily,
          fontSize = 26.sp,
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = (-0.5).sp,
          maxLines = 1,
          overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = subtitle,
          color = accentColor,
          fontFamily = AptosGrotesqueFontFamily,
          fontSize = 12.5.sp,
          fontWeight = FontWeight.SemiBold,
          maxLines = 1,
          overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
      }
    }
  }
}
