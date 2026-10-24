package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceSunken
import com.example.ui.theme.GreycliffHeavy
import com.example.ui.theme.GreycliffHeavyFontFamily
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.UserRole

@Composable
fun LoginScreen(
  onSelectRole: (UserRole, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedRole by remember { mutableStateOf(UserRole.PERSONAL) }
  var userNameInput by remember { mutableStateOf("") }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(DarkCanvas)
      .statusBarsPadding()
      .navigationBarsPadding()
  ) {
    // Ambient Top Violet Radial Spotlight
    Canvas(
      modifier = Modifier
        .fillMaxWidth()
        .height(380.dp)
        .align(Alignment.TopCenter)
    ) {
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            Color(0x35A855F7),
            Color(0x187C3AED),
            Color(0x00090810)
          ),
          center = Offset(size.width * 0.5f, 90.dp.toPx()),
          radius = size.width * 0.75f
        ),
        radius = size.width * 0.75f,
        center = Offset(size.width * 0.5f, 90.dp.toPx())
      )
    }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 22.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top
    ) {
      Spacer(modifier = Modifier.height(24.dp))

      Spacer(modifier = Modifier.height(18.dp))

      // Brand Title in Bold Greycliff Heavy
      Text(
        text = "Subscriptions.AI",
        fontFamily = GreycliffHeavy,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary,
        letterSpacing = (-0.6).sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Tagline Pill
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(30.dp))
          .background(Color(0xFF1B1433))
          .border(1.dp, Color(0xFF4C337D), RoundedCornerShape(30.dp))
          .padding(horizontal = 12.dp, vertical = 5.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(PurpleLight)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "RECURRING CHARGES & MANDATE AUDITOR",
            fontFamily = GreycliffHeavy,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Bold,
            color = PurpleLight,
            letterSpacing = 0.5.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Select your workspace to customize intelligence dashboards",
        fontFamily = GreycliffHeavyFontFamily,
        fontSize = 13.5.sp,
        color = TextSecondary,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(26.dp))

      // Neumorphic Role Selection Cards
      // 1. Personal / Individual Consumer Card
      val isPersonal = selectedRole == UserRole.PERSONAL
      NeumorphicRoleCard(
        title = "Individual / Personal User",
        subtitle = "Track OTT, gym cash dues, UPI AutoPay mandates, milk/maid payments & bank debit alerts.",
        isSelected = isPersonal,
        primaryAccent = PurplePrimary,
        lightAccent = PurpleLight,
        icon = Icons.Default.Person,
        tags = listOf("💵 Cash & Gym Dues", "⚡ UPI Mandates", "🔒 1-Click Cancel"),
        onClick = { selectedRole = UserRole.PERSONAL },
        testTag = "role_personal_card"
      )

      Spacer(modifier = Modifier.height(16.dp))

      // 2. Enterprise / Corporate Card
      val isEnterprise = selectedRole == UserRole.ENTERPRISE
      NeumorphicRoleCard(
        title = "Enterprise / Organization",
        subtitle = "Manage organizational SaaS stack (Figma, GitHub, AWS, Slack), team seats & reclaim idle licenses.",
        isSelected = isEnterprise,
        primaryAccent = SkyAccent,
        lightAccent = Color(0xFF7DD3FC),
        icon = Icons.Default.Business,
        tags = listOf("🏢 SaaS Stack", "👥 Reclaim Idle Seats", "🔑 SSO Directory"),
        onClick = { selectedRole = UserRole.ENTERPRISE },
        testTag = "role_enterprise_card"
      )

      Spacer(modifier = Modifier.height(22.dp))

      // Neumorphic Inset Input Field (Debossed Well)
      Text(
        text = if (isPersonal) "YOUR PROFILE NAME" else "ORGANIZATION OR TEAM NAME",
        fontFamily = GreycliffHeavy,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = TextMuted,
        letterSpacing = 0.5.sp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp, vertical = 4.dp)
      )

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            spotColor = Color(0xFF040307),
            ambientColor = Color(0xFF040307)
          )
          .clip(RoundedCornerShape(16.dp))
          .background(DarkSurfaceSunken)
          .border(
            width = 1.2.dp,
            brush = if (isPersonal) {
              Brush.linearGradient(listOf(Color(0xFF4C337D), DarkBorderSubtle))
            } else {
              Brush.linearGradient(listOf(Color(0xFF0284C7), DarkBorderSubtle))
            },
            shape = RoundedCornerShape(16.dp)
          )
      ) {
        OutlinedTextField(
          value = userNameInput,
          onValueChange = { userNameInput = it },
          placeholder = {
            Text(
              text = if (isPersonal) "Enter your name (e.g. Alex Sharma)" else "Enter company name (e.g. Acme Technologies)",
              fontFamily = GreycliffHeavyFontFamily,
              color = TextMuted,
              fontSize = 13.5.sp
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_user_name"),
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
          ),
          singleLine = true
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Primary Neumorphic Continue Action Button
      NeumorphicLuxuryActionButton(
        text = if (isPersonal) "Enter Personal Dashboard ➔" else "Enter Enterprise Dashboard ➔",
        accentGradient = if (isPersonal) {
          listOf(PurplePrimary, Color(0xFF7C3AED))
        } else {
          listOf(Color(0xFF0284C7), Color(0xFF0369A1))
        },
        glowColor = if (isPersonal) Color(0x70A855F7) else Color(0x700284C7),
        onClick = {
          val displayName = userNameInput.trim().ifBlank {
            if (isPersonal) "Personal Account" else "Enterprise Admin"
          }
          onSelectRole(selectedRole, displayName)
        },
        modifier = Modifier.fillMaxWidth(),
        testTag = "btn_enter_dashboard"
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Fast Demo Persona Buttons (One-Tap Access)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        NeumorphicPillButton(
          text = "⚡ Alex (Personal)",
          modifier = Modifier.weight(1f),
          onClick = {
            onSelectRole(UserRole.PERSONAL, "Alex Sharma")
          },
          testTag = "quick_demo_personal"
        )

        NeumorphicPillButton(
          text = "🏢 Acme Labs (Org)",
          modifier = Modifier.weight(1f),
          onClick = {
            onSelectRole(UserRole.ENTERPRISE, "Acme Labs Corp")
          },
          testTag = "quick_demo_enterprise"
        )
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Bank Grade Security Footer Badge
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(
          imageVector = Icons.Default.Shield,
          contentDescription = null,
          tint = PurpleLight,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Bank-Grade 256-Bit Encryption • Zero-Knowledge On-Device AI",
          fontFamily = GreycliffHeavyFontFamily,
          fontSize = 11.sp,
          color = TextMuted
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

/**
 * SubscriptionsAiMasterLogo:
 * An authentic, iconic 3D Neumorphic emblem combining a rotating celestial mandate ring,
 * an embossed faceted shield, and a radiant AI fintech star.
 */
@Composable
fun SubscriptionsAiMasterLogo(
  size: Dp = 94.dp,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "logo_anim")

  val orbitRotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 8000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "orbit_rot"
  )

  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.55f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  Box(
    modifier = modifier
      .size(size)
      .shadow(
        elevation = 16.dp,
        shape = CircleShape,
        spotColor = Color(0x90A855F7),
        ambientColor = Color(0x60000000)
      )
      .clip(CircleShape)
      .background(
        Brush.radialGradient(
          colors = listOf(
            Color(0xFF271C44),
            Color(0xFF130E24),
            Color(0xFF090712)
          )
        )
      )
      .border(
        width = 2.dp,
        brush = Brush.linearGradient(
          colors = listOf(
            Color(0xFFC084FC),
            Color(0xFF7C3AED),
            Color(0xFF2B2048)
          )
        ),
        shape = CircleShape
      ),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = this.size.width
      val h = this.size.height
      val center = Offset(w * 0.5f, h * 0.5f)

      // 1. Orbital Ring (Subscription Mandate Cycle)
      drawCircle(
        color = Color(0x35A855F7),
        radius = w * 0.40f,
        center = center,
        style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
      )

      // 2. Rotating Mandate Satellite Pulse Dot
      rotate(degrees = orbitRotation, pivot = center) {
        val satelliteCenter = Offset(center.x + w * 0.40f, center.y)
        drawCircle(
          color = Color(0xFFC084FC).copy(alpha = pulseAlpha),
          radius = 3.5.dp.toPx(),
          center = satelliteCenter
        )
        drawCircle(
          color = Color.White,
          radius = 1.8.dp.toPx(),
          center = satelliteCenter
        )
      }

      // 3. Central Faceted Security Shield
      val shieldPath = Path().apply {
        moveTo(center.x, center.y - h * 0.26f)
        lineTo(center.x + w * 0.22f, center.y - h * 0.14f)
        lineTo(center.x + w * 0.18f, center.y + h * 0.14f)
        lineTo(center.x, center.y + h * 0.28f)
        lineTo(center.x - w * 0.18f, center.y + h * 0.14f)
        lineTo(center.x - w * 0.22f, center.y - h * 0.14f)
        close()
      }

      // Shield Gradient Fill
      drawPath(
        path = shieldPath,
        brush = Brush.linearGradient(
          colors = listOf(Color(0xFF281E48), Color(0xFF140E26)),
          start = Offset(center.x, center.y - h * 0.26f),
          end = Offset(center.x, center.y + h * 0.28f)
        )
      )

      // Shield Bezel Border
      drawPath(
        path = shieldPath,
        brush = Brush.linearGradient(
          colors = listOf(Color(0xFFE9D5FF), Color(0xFFA855F7), Color(0xFF4C337D))
        ),
        style = Stroke(width = 1.75.dp.toPx())
      )

      // 4. Stylized Financial 'S' Ribbon inside Shield
      val sRibbon = Path().apply {
        moveTo(center.x + w * 0.08f, center.y - h * 0.14f)
        cubicTo(
          center.x - w * 0.12f, center.y - h * 0.14f,
          center.x - w * 0.10f, center.y,
          center.x, center.y
        )
        cubicTo(
          center.x + w * 0.10f, center.y,
          center.x + w * 0.12f, center.y + h * 0.14f,
          center.x - w * 0.08f, center.y + h * 0.14f
        )
      }
      drawPath(
        path = sRibbon,
        brush = Brush.linearGradient(
          colors = listOf(Color(0xFFC084FC), Color(0xFF38BDF8))
        ),
        style = Stroke(width = 2.8.dp.toPx(), cap = StrokeCap.Round)
      )

      // 5. Central AI Intelligence Star Spark
      val sparkSize = 4.dp.toPx()
      drawCircle(
        color = Color.White,
        radius = sparkSize,
        center = center
      )
      drawCircle(
        color = Color(0x8038BDF8),
        radius = sparkSize * 2.2f,
        center = center
      )
    }
  }
}

/**
 * Tactile Neumorphic Role Selection Card:
 * Extruded card with convex gradient, dual-layer highlights, and tactile radio pill.
 */
@Composable
private fun NeumorphicRoleCard(
  title: String,
  subtitle: String,
  isSelected: Boolean,
  primaryAccent: Color,
  lightAccent: Color,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  tags: List<String>,
  onClick: () -> Unit,
  testTag: String
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.985f else 1f,
    animationSpec = tween(100),
    label = "role_scale"
  )

  val cardElevation by animateFloatAsState(
    targetValue = if (isSelected) 10f else if (isPressed) 2f else 4f,
    animationSpec = tween(150),
    label = "role_elev"
  )

  val borderBrush = if (isSelected) {
    Brush.linearGradient(
      colors = listOf(lightAccent, primaryAccent, Color(0xFF281F45))
    )
  } else {
    Brush.linearGradient(
      colors = listOf(Color(0xFF2E244E), DarkBorderSubtle)
    )
  }

  val backgroundBrush = if (isSelected) {
    Brush.verticalGradient(
      colors = listOf(Color(0xFF22173E), Color(0xFF140F27))
    )
  } else {
    Brush.verticalGradient(
      colors = listOf(DarkSurfaceElevated, DarkSurface)
    )
  }

  val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .scale(scale)
      .shadow(
        elevation = cardElevation.dp,
        shape = RoundedCornerShape(20.dp),
        spotColor = if (isSelected) primaryAccent.copy(alpha = 0.40f) else Color(0xFF040307),
        ambientColor = Color(0xFF040307)
      )
      .clip(RoundedCornerShape(20.dp))
      .background(backgroundBrush)
      .border(
        width = if (isSelected) 1.8.dp else 1.dp,
        brush = borderBrush,
        shape = RoundedCornerShape(20.dp)
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = {
          haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
          onClick()
        }
      )
      .padding(18.dp)
      .testTag(testTag)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.Top
    ) {
      // Extruded Neumorphic Icon Well
      Box(
        modifier = Modifier
          .size(48.dp)
          .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(14.dp),
            spotColor = if (isSelected) primaryAccent.copy(alpha = 0.50f) else Color(0xFF040307)
          )
          .clip(RoundedCornerShape(14.dp))
          .background(
            if (isSelected) {
              Brush.linearGradient(listOf(primaryAccent, Color(0xFF6B21A8)))
            } else {
              Brush.linearGradient(listOf(DarkSurfaceElevated, DarkSurfaceSunken))
            }
          )
          .border(
            width = 1.dp,
            color = if (isSelected) lightAccent.copy(alpha = 0.8f) else DarkBorder,
            shape = RoundedCornerShape(14.dp)
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = if (isSelected) Color.White else TextMuted,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = title,
            fontFamily = GreycliffHeavy,
            fontSize = 16.5.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )

          // Neumorphic Radio Indicator
          NeumorphicRadioIndicator(
            isSelected = isSelected,
            activeColor = lightAccent
          )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
          text = subtitle,
          fontFamily = GreycliffHeavyFontFamily,
          fontSize = 12.5.sp,
          color = TextSecondary,
          lineHeight = 17.5.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Capability Tag Pills
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          tags.forEach { tag ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isSelected) Color(0xFF281E48) else DarkSurfaceSunken)
                .border(
                  width = 0.6.dp,
                  color = if (isSelected) lightAccent.copy(alpha = 0.4f) else DarkBorderSubtle,
                  shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
              Text(
                text = tag,
                fontFamily = GreycliffHeavy,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) lightAccent else TextMuted
              )
            }
          }
        }
      }
    }
  }
}

/**
 * Neumorphic Radio Indicator with glowing inner state
 */
@Composable
private fun NeumorphicRadioIndicator(
  isSelected: Boolean,
  activeColor: Color
) {
  Box(
    modifier = Modifier
      .size(20.dp)
      .shadow(
        elevation = if (isSelected) 3.dp else 1.dp,
        shape = CircleShape,
        spotColor = if (isSelected) activeColor else Color.Transparent
      )
      .clip(CircleShape)
      .background(DarkSurfaceSunken)
      .border(
        width = 1.2.dp,
        color = if (isSelected) activeColor else DarkBorder,
        shape = CircleShape
      ),
    contentAlignment = Alignment.Center
  ) {
    if (isSelected) {
      Box(
        modifier = Modifier
          .size(9.dp)
          .clip(CircleShape)
          .background(activeColor)
      )
    }
  }
}

/**
 * Tactile Neumorphic Primary Action Button
 */
@Composable
private fun NeumorphicLuxuryActionButton(
  text: String,
  accentGradient: List<Color>,
  glowColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = ""
) {
  val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.97f else 1f,
    animationSpec = tween(90),
    label = "cta_scale"
  )

  val elevation by animateFloatAsState(
    targetValue = if (isPressed) 2f else 8f,
    animationSpec = tween(120),
    label = "cta_elevation"
  )

  Box(
    modifier = modifier
      .scale(scale)
      .shadow(
        elevation = elevation.dp,
        shape = RoundedCornerShape(16.dp),
        spotColor = glowColor,
        ambientColor = Color(0xFF040307)
      )
      .clip(RoundedCornerShape(16.dp))
      .background(
        Brush.horizontalGradient(accentGradient)
      )
      .border(
        width = 1.dp,
        color = Color(0x60FFFFFF),
        shape = RoundedCornerShape(16.dp)
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = {
          haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
          onClick()
        }
      )
      .padding(vertical = 16.dp)
      .testTag(testTag),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      fontFamily = GreycliffHeavy,
      fontSize = 15.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White,
      letterSpacing = 0.2.sp
    )
  }
}

/**
 * Tactile Neumorphic Quick-Action Pill Button
 */
@Composable
private fun NeumorphicPillButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = ""
) {
  val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.96f else 1f,
    animationSpec = tween(80),
    label = "pill_scale"
  )

  Box(
    modifier = modifier
      .scale(scale)
      .shadow(
        elevation = if (isPressed) 1.dp else 4.dp,
        shape = RoundedCornerShape(12.dp),
        spotColor = Color(0xFF040307),
        ambientColor = Color(0xFF040307)
      )
      .clip(RoundedCornerShape(12.dp))
      .background(
        Brush.verticalGradient(
          listOf(DarkSurfaceElevated, DarkSurfaceSunken)
        )
      )
      .border(
        width = 1.dp,
        brush = Brush.verticalGradient(
          listOf(Color(0xFF382E56), Color(0xFF19142A))
        ),
        shape = RoundedCornerShape(12.dp)
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = {
          haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
          onClick()
        }
      )
      .padding(horizontal = 10.dp, vertical = 11.dp)
      .testTag(testTag),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      fontFamily = GreycliffHeavy,
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      color = TextSecondary
    )
  }
}
