package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AptosGrotesqueFontFamily
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceSunken
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

// Obsidian Neumorphic Highlights & Shadow Boundaries
val NeuDarkHighlight = Color(0xFF2B2344)
val NeuDarkShadow = Color(0xFF040307)
val NeuDarkBorderTop = Color(0xFF382E56)
val NeuDarkBorderBottom = Color(0xFF141021)

/**
 * Obsidian & Luxury Purple Neumorphic Button with realistic dual-shadow convex / concave states.
 */
@Composable
fun NeumorphicButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  shape: CornerBasedShape = RoundedCornerShape(14.dp),
  backgroundColor: Color = DarkSurface,
  elevation: Dp = 5.dp,
  enabled: Boolean = true,
  testTag: String = "",
  content: @Composable RowScope.() -> Unit
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.97f else 1f,
    animationSpec = tween(durationMillis = 90),
    label = "neu_scale"
  )

  val currentElevation = if (isPressed) 1.dp else elevation

  val gradientBrush = if (isPressed) {
    Brush.verticalGradient(
      colors = listOf(DarkSurfaceSunken, DarkSurface)
    )
  } else {
    Brush.linearGradient(
      colors = listOf(
        backgroundColor.copy(alpha = 0.95f),
        DarkSurfaceSunken
      )
    )
  }

  val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

  Box(
    modifier = modifier
      .scale(scale)
      .shadow(
        elevation = currentElevation,
        shape = shape,
        spotColor = NeuDarkShadow,
        ambientColor = NeuDarkShadow
      )
      .clip(shape)
      .background(gradientBrush)
      .border(
        width = 1.dp,
        brush = Brush.verticalGradient(
          colors = listOf(NeuDarkBorderTop, NeuDarkBorderBottom)
        ),
        shape = shape
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enabled,
        onClick = {
          haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
          onClick()
        }
      )
      .then(if (testTag.isNotBlank()) Modifier.testTag(testTag) else Modifier),
    contentAlignment = Alignment.Center
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
      content = content
    )
  }
}

/**
 * Electric Purple Primary Action Button with outer ambient glow
 */
@Composable
fun NeumorphicPrimaryButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  shape: CornerBasedShape = RoundedCornerShape(14.dp),
  primaryColor: Color = PurplePrimary,
  contentColor: Color = Color.White,
  elevation: Dp = 6.dp,
  enabled: Boolean = true,
  testTag: String = "",
  content: @Composable RowScope.() -> Unit
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.97f else 1f,
    animationSpec = tween(durationMillis = 90),
    label = "neu_primary_scale"
  )

  val currentElevation = if (isPressed) 2.dp else elevation

  val gradientBrush = if (isPressed) {
    Brush.verticalGradient(
      colors = listOf(PurpleAccent, primaryColor)
    )
  } else {
    Brush.verticalGradient(
      colors = listOf(primaryColor, PurpleAccent)
    )
  }

  val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

  Box(
    modifier = modifier
      .scale(scale)
      .shadow(
        elevation = currentElevation,
        shape = shape,
        spotColor = Color(0x60A855F7),
        ambientColor = Color(0x30A855F7)
      )
      .clip(shape)
      .background(gradientBrush)
      .border(
        width = 1.dp,
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xFFE9D5FF).copy(alpha = 0.5f), Color(0xFF7C3AED))
        ),
        shape = shape
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enabled,
        onClick = {
          haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
          onClick()
        }
      )
      .then(if (testTag.isNotBlank()) Modifier.testTag(testTag) else Modifier),
    contentAlignment = Alignment.Center
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
      content = content
    )
  }
}

/**
 * Neumorphic Icon Button (Squircle / Rounded)
 */
@Composable
fun NeumorphicIconButton(
  icon: ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  contentDescription: String? = null,
  tint: Color = TextSecondary,
  size: Dp = 38.dp,
  iconSize: Dp = 18.dp,
  shape: CornerBasedShape = RoundedCornerShape(12.dp),
  elevation: Dp = 4.dp,
  testTag: String = ""
) {
  val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.94f else 1f,
    animationSpec = tween(durationMillis = 90),
    label = "icon_btn_scale"
  )

  val currentElevation = if (isPressed) 1.dp else elevation

  val bg = if (isPressed) {
    Brush.verticalGradient(listOf(DarkSurfaceSunken, DarkSurface))
  } else {
    Brush.verticalGradient(listOf(DarkSurfaceElevated, DarkSurface))
  }

  Box(
    modifier = modifier
      .size(size)
      .scale(scale)
      .shadow(
        elevation = currentElevation,
        shape = shape,
        spotColor = NeuDarkShadow,
        ambientColor = NeuDarkShadow
      )
      .clip(shape)
      .background(bg)
      .border(1.dp, NeuDarkBorderTop, shape)
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = {
          haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
          onClick()
        }
      )
      .then(if (testTag.isNotBlank()) Modifier.testTag(testTag) else Modifier),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = icon,
      contentDescription = contentDescription,
      tint = tint,
      modifier = Modifier.size(iconSize)
    )
  }
}

/**
 * Neumorphic Filter Pill with tactile convex and sunken states.
 */
@Composable
fun NeumorphicFilterPill(
  selected: Boolean,
  onClick: () -> Unit,
  label: String,
  modifier: Modifier = Modifier,
  count: Int? = null,
  testTag: String = ""
) {
  val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.95f else 1f,
    animationSpec = tween(durationMillis = 90),
    label = "pill_scale"
  )

  val elevation = if (selected || isPressed) 1.dp else 4.dp

  val bg = if (selected) {
    Brush.verticalGradient(listOf(Color(0xFF261D42), Color(0xFF19122D)))
  } else {
    Brush.verticalGradient(listOf(DarkSurfaceElevated, DarkSurface))
  }

  val borderColor = if (selected) {
    Brush.verticalGradient(listOf(PurpleLight, PurplePrimary))
  } else {
    Brush.verticalGradient(listOf(NeuDarkBorderTop, NeuDarkBorderBottom))
  }

  Box(
    modifier = modifier
      .scale(scale)
      .shadow(
        elevation = elevation,
        shape = RoundedCornerShape(12.dp),
        spotColor = NeuDarkShadow,
        ambientColor = NeuDarkShadow
      )
      .clip(RoundedCornerShape(12.dp))
      .background(bg)
      .border(1.2.dp, borderColor, RoundedCornerShape(12.dp))
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = {
          haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
          onClick()
        }
      )
      .padding(horizontal = 12.dp, vertical = 7.dp)
      .then(if (testTag.isNotBlank()) Modifier.testTag(testTag) else Modifier),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      if (selected) {
        Box(
          modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(PurpleLight)
        )
        Spacer(modifier = Modifier.width(6.dp))
      }
      Text(
        text = if (count != null) "$label ($count)" else label,
        color = if (selected) Color.White else TextMuted,
        fontSize = 12.sp,
        fontFamily = AptosGrotesqueFontFamily,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
      )
    }
  }
}

/**
 * Compact Action Tile in Obsidian Purple Neumorphic Style
 */
@Composable
fun NeumorphicActionTile(
  title: String,
  icon: ImageVector,
  tint: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = ""
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.96f else 1f,
    animationSpec = tween(durationMillis = 90),
    label = "tile_scale"
  )

  val elevation = if (isPressed) 1.dp else 4.dp

  Box(
    modifier = modifier
      .height(42.dp)
      .scale(scale)
      .shadow(
        elevation = elevation,
        shape = RoundedCornerShape(12.dp),
        spotColor = NeuDarkShadow,
        ambientColor = NeuDarkShadow
      )
      .clip(RoundedCornerShape(12.dp))
      .background(
        if (isPressed) DarkSurfaceSunken else DarkSurface
      )
      .border(
        width = 1.dp,
        brush = Brush.verticalGradient(listOf(NeuDarkBorderTop, NeuDarkBorderBottom)),
        shape = RoundedCornerShape(12.dp)
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 8.dp)
      .then(if (testTag.isNotBlank()) Modifier.testTag(testTag) else Modifier),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(15.dp))
      Spacer(modifier = Modifier.width(5.dp))
      Text(
        text = title,
        color = TextPrimary,
        fontSize = 11.5.sp,
        fontFamily = AptosGrotesqueFontFamily,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1
      )
    }
  }
}
