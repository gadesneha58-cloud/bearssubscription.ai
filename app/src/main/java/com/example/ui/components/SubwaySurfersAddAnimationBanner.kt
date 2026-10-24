package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Subscription
import com.example.model.SubscriptionClassification
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceSunken
import com.example.ui.theme.GreycliffHeavy
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.WarningRose
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * Arcade Subway Surfers-style Reward Pop-up Banner for newly added subscriptions.
 * Features:
 * - Spring Bouncy Drop Entrance
 * - Particle Burst Canvas (arcade sparks)
 * - Count-Up Animated Amount counter (e.g. ₹0.00 -> ₹649.00)
 * - Tactile Haptic Vibration
 * - Auto-dismiss after 3.2 seconds
 */
@Composable
fun SubwaySurfersAddAnimationBanner(
  subscription: Subscription,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val haptic = LocalHapticFeedback.current

  // Bouncy Scale Animatable
  val scaleAnim = remember { Animatable(0.2f) }
  val particleProgress = remember { Animatable(0f) }

  // Animated Amount Counter State
  var amountTarget by remember { mutableFloatStateOf(0f) }
  val animatedAmount by animateFloatAsState(
    targetValue = amountTarget,
    animationSpec = tween(durationMillis = 1200, easing = LinearEasing),
    label = "amount_count_up"
  )

  LaunchedEffect(subscription) {
    // 1. Trigger Tactile Haptic Vibration
    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

    // 2. Animate Bouncy Scale
    scaleAnim.animateTo(
      targetValue = 1f,
      animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
      )
    )

    // 3. Trigger Particle Explosion
    particleProgress.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 800)
    )

    // 4. Start Amount Count Up
    amountTarget = subscription.currentCost.toFloat()

    // 5. Auto Dismiss after 3.2 seconds
    delay(3200)
    onDismiss()
  }

  // Pulsing Halo Glow
  val infiniteTransition = rememberInfiniteTransition(label = "banner_glow")
  val haloAlpha by infiniteTransition.animateFloat(
    initialValue = 0.40f,
    targetValue = 0.90f,
    animationSpec = infiniteRepeatable(
      animation = tween(800),
      repeatMode = RepeatMode.Reverse
    ),
    label = "halo_alpha"
  )

  val isZombie = subscription.classification == SubscriptionClassification.ZOMBIE_SUBSCRIPTION
  val bannerAccent = if (isZombie) WarningRose else PurplePrimary
  val bannerGlow = if (isZombie) Color(0x90F43F5E) else Color(0x90A855F7)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0x88000000))
      .clickable { onDismiss() },
    contentAlignment = Alignment.Center
  ) {
    // Particle Burst Canvas
    Canvas(modifier = Modifier.size(320.dp)) {
      val center = Offset(size.width * 0.5f, size.height * 0.5f)
      val radiusMax = size.width * 0.45f * particleProgress.value
      val particleColors = listOf(
        Color(0xFFFBBF24), Color(0xFFC084FC), Color(0xFF38BDF8),
        Color(0xFFF43F5E), Color(0xFF34D399), Color(0xFFE9D5FF)
      )

      for (i in 0..15) {
        val angle = (i * (360f / 16f)) * (Math.PI / 180f)
        val x = center.x + radiusMax * cos(angle).toFloat()
        val y = center.y + radiusMax * sin(angle).toFloat()
        val particleSize = (6.dp.toPx()) * (1f - particleProgress.value)
        val alpha = (1f - particleProgress.value).coerceIn(0f, 1f)

        drawCircle(
          color = particleColors[i % particleColors.size].copy(alpha = alpha),
          radius = particleSize.coerceAtLeast(1f),
          center = Offset(x, y)
        )
      }
    }

    // Main Bouncy Subway Surfers Card
    Box(
      modifier = Modifier
        .scale(scaleAnim.value)
        .fillMaxWidth(0.88f)
        .shadow(
          elevation = 20.dp,
          shape = RoundedCornerShape(26.dp),
          spotColor = bannerGlow,
          ambientColor = bannerGlow
        )
        .clip(RoundedCornerShape(26.dp))
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color(0xFF231842),
              Color(0xFF140F27),
              DarkSurfaceSunken
            )
          )
        )
        .border(
          width = 2.dp,
          brush = Brush.linearGradient(
            colors = listOf(
              Color(0xFFE9D5FF),
              bannerAccent,
              Color(0xFF1E1736)
            )
          ),
          shape = RoundedCornerShape(26.dp)
        )
        .padding(22.dp)
        .testTag("banner_subway_surfers_add")
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
      ) {
        // Arcade Top Pill Badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .background(
              Brush.horizontalGradient(
                listOf(bannerAccent, Color(0xFF7C3AED))
              )
            )
            .border(1.dp, Color(0x80FFFFFF), RoundedCornerShape(30.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = if (isZombie) Icons.Default.Warning else Icons.Default.FlashOn,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = if (isZombie) "🧟 ZOMBIE MANDATE TRACKED!" else "⚡ NEW SUBSCRIPTION TRACKED!",
              fontFamily = GreycliffHeavy,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White,
              letterSpacing = 0.5.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Merchant Name
        Text(
          text = subscription.merchantName,
          fontFamily = GreycliffHeavy,
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = subscription.category,
          fontFamily = GreycliffHeavy,
          fontSize = 13.sp,
          color = TextMuted
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Animated Cost Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurfaceSunken)
            .border(1.2.dp, Color(0xFF382E56), RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp, horizontal = 16.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "RECURRING AMOUNT",
              fontFamily = GreycliffHeavy,
              fontSize = 9.5.sp,
              fontWeight = FontWeight.Bold,
              color = TextMuted,
              letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = "+ ₹${"%.2f".format(animatedAmount)}/mo",
              fontFamily = GreycliffHeavy,
              fontSize = 28.sp,
              fontWeight = FontWeight.Black,
              color = if (isZombie) WarningRose else PurpleLight,
              letterSpacing = (-0.5).sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Footer Dismiss Tip
        Text(
          text = "Tap anywhere to continue ➔",
          fontFamily = GreycliffHeavy,
          fontSize = 11.sp,
          color = TextMuted,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}
