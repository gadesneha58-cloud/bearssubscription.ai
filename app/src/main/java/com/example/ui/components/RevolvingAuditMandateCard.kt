package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.DarkSurfaceSunken
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.filled.Payments
import androidx.compose.ui.draw.alpha

data class RevolvingAuditItem(
  val id: Int,
  val tabLabel: String,
  val title: String,
  val subtitle: String,
  val badgeText: String,
  val icon: ImageVector,
  val iconTint: Color
)

/**
 * Full-width revolving option card for UPI Mandates, Screenshot Bill Audit, and Cash/Gym Presets.
 * Replaces the cramped side-by-side buttons that cause horizontal text wrapping.
 */
@Composable
fun RevolvingAuditMandateCard(
  onOpenSmsPipeline: () -> Unit,
  onOpenScreenshotAudit: () -> Unit,
  onOpenAddSubscription: () -> Unit,
  onOpenCashSubscription: () -> Unit = onOpenAddSubscription,
  modifier: Modifier = Modifier
) {
  val items = remember {
    listOf(
      RevolvingAuditItem(
        id = 0,
        tabLabel = "UPI Mandates",
        title = "UPI Mandates & AutoPay",
        subtitle = "Live bank SMS parsing & recurring debits",
        badgeText = "Scan SMS ➔",
        icon = Icons.Default.Sms,
        iconTint = PurpleLight
      ),
      RevolvingAuditItem(
        id = 1,
        tabLabel = "Bill Audit",
        title = "AI Bill & Screenshot Audit",
        subtitle = "Instant rate & renewal date extraction",
        badgeText = "Upload Bill ➔",
        icon = Icons.Default.AddPhotoAlternate,
        iconTint = SkyAccent
      ),
      RevolvingAuditItem(
        id = 2,
        tabLabel = "+ Feed Data",
        title = "Add Real Subscription",
        subtitle = "Manually record service, cost & cycle",
        badgeText = "+ Add Entry ➔",
        icon = Icons.Default.Add,
        iconTint = PurplePrimary
      ),
      RevolvingAuditItem(
        id = 3,
        tabLabel = "Cash & Gym",
        title = "Cash & Gym Subscriptions",
        subtitle = "Gym, milk, society, maid & cash dues",
        badgeText = "Tap Presets ➔",
        icon = Icons.Default.Payments,
        iconTint = PurpleLight
      )
    )
  }

  var selectedIndex by remember { mutableIntStateOf(0) }
  var isUserInteracting by remember { androidx.compose.runtime.mutableStateOf(false) }

  // Auto-revolve smoothly every 6 seconds if user is not actively tapping
  LaunchedEffect(isUserInteracting) {
    while (!isUserInteracting) {
      delay(6000)
      selectedIndex = (selectedIndex + 1) % items.size
    }
  }

  val currentItem = items[selectedIndex]
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  Box(
    modifier = modifier
      .fillMaxWidth()
      .shadow(
        elevation = 6.dp,
        shape = RoundedCornerShape(20.dp),
        spotColor = Color(0xFF040307),
        ambientColor = Color(0xFF040307)
      )
      .clip(RoundedCornerShape(20.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(DarkSurfaceElevated, DarkSurface)
        )
      )
      .border(
        width = 1.2.dp,
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xFF382E56), Color(0xFF161226))
        ),
        shape = RoundedCornerShape(20.dp)
      )
      .padding(16.dp)
      .testTag("revolving_audit_card")
  ) {
    Column {
      // Top revolving tab selector with haptic feedback & scrollable row to prevent text squishing
      val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier
            .weight(1f, fill = false)
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items.forEachIndexed { index, item ->
            val isSelected = index == selectedIndex
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                  if (isSelected) Color(0xFF2E2254) else DarkSurfaceSunken
                )
                .border(
                  width = 0.8.dp,
                  color = if (isSelected) PurplePrimary else DarkBorder,
                  shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                  haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                  isUserInteracting = true
                  selectedIndex = index
                }
                .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Text(
                text = item.tabLabel,
                fontFamily = AptosGrotesqueFontFamily,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else TextMuted,
                softWrap = false,
                maxLines = 1
              )
            }
          }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Live status indicator dot with strict single line constraints
        val infiniteTransition = rememberInfiniteTransition(label = "pulse_trans")
        val pulseAlpha by infiniteTransition.animateFloat(
          initialValue = 0.35f,
          targetValue = 1f,
          animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
          ),
          label = "pulse_alpha"
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .alpha(pulseAlpha)
              .clip(CircleShape)
              .background(PurpleLight)
          )
          Text(
            text = "REVOLVING",
            fontFamily = AptosGrotesqueFontFamily,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = PurpleLight,
            letterSpacing = 0.5.sp,
            softWrap = false,
            maxLines = 1
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Animated Card Content
      AnimatedContent(
        targetState = currentItem,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "revolving_content"
      ) { item ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurfaceSunken)
            .border(1.dp, Color(0xFF261F3C), RoundedCornerShape(14.dp))
          .clickable(
            interactionSource = interactionSource,
            indication = null
          ) {
            when (item.id) {
              0 -> onOpenSmsPipeline()
              1 -> onOpenScreenshotAudit()
              2 -> onOpenAddSubscription()
              3 -> onOpenCashSubscription()
            }
          }
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            // Embossed squircle icon
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                  Brush.linearGradient(
                    listOf(Color(0xFF261D42), Color(0xFF140F24))
                  )
                )
                .border(1.dp, Color(0xFF4A3A73), RoundedCornerShape(12.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.iconTint,
                modifier = Modifier.size(22.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = item.title,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary,
                maxLines = 1
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = item.subtitle,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 11.5.sp,
                color = TextSecondary,
                maxLines = 1
              )
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Action CTA Pill
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.horizontalGradient(
                  listOf(PurplePrimary, Color(0xFF7C3AED))
                )
              )
              .padding(horizontal = 12.dp, vertical = 7.dp)
          ) {
            Text(
              text = item.badgeText,
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }
  }
}
