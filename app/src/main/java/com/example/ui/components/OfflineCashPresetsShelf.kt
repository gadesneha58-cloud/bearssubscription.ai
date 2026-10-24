package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BillingCadence
import com.example.model.OfflinePresetCatalog
import com.example.model.OfflineSubscriptionPreset
import com.example.model.RecommendedAction
import com.example.model.Subscription
import com.example.model.SubscriptionClassification
import com.example.model.SubscriptionStatus
import com.example.ui.theme.AptosGrotesqueFontFamily
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceSunken
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleBg
import com.example.ui.theme.PurpleBorder
import com.example.ui.theme.PurpleGlow
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.PurpleUltraLight
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Animated "Tap & Add" Shelf for Offline and Cash Subscriptions
 * (Gym, Society Maintenance, Maid, Milk Delivery, Water, Newspaper, etc.)
 * Includes silky smooth spring micro-animations and zero jitter.
 */
@Composable
fun OfflineCashPresetsShelf(
  onAddSubscription: (Subscription) -> Unit,
  onOpenCustomAdder: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategory by remember { mutableStateOf("All") }
  val addedStateMap = remember { mutableStateMapOf<String, Boolean>() }
  val coroutineScope = rememberCoroutineScope()

  val allPresets = remember { OfflinePresetCatalog.commonPresets }

  val filteredPresets = remember(selectedCategory) {
    when (selectedCategory) {
      "Fitness" -> allPresets.filter { it.category == "Fitness" || it.category == "Wellness" }
      "Housing & Home" -> allPresets.filter { it.category == "Housing" || it.category == "Domestic" }
      "Essentials" -> allPresets.filter { it.category == "Essentials" || it.category == "Utilities" }
      else -> allPresets
    }
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
  ) {
    // Header Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .shadow(
              elevation = 3.dp,
              shape = RoundedCornerShape(8.dp),
              spotColor = PurpleGlow,
              ambientColor = Color.Black
            )
            .clip(RoundedCornerShape(8.dp))
            .background(
              Brush.linearGradient(
                listOf(DarkSurfaceElevated, DarkSurfaceSunken)
              )
            )
            .border(1.dp, DarkBorder, RoundedCornerShape(8.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Payments,
            contentDescription = null,
            tint = PurpleLight,
            modifier = Modifier.size(15.dp)
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
          Text(
            text = "Offline & Cash Subscriptions",
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.5.sp,
            color = TextPrimary
          )
          Text(
            text = "Tap & add Gym, Society, Milk, Maid & cash dues",
            fontFamily = AptosGrotesqueFontFamily,
            fontSize = 10.sp,
            color = TextMuted
          )
        }
      }

      // "+ Custom" Button
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(DarkSurfaceElevated)
          .border(1.dp, DarkBorder, RoundedCornerShape(8.dp))
          .clickable { onOpenCustomAdder() }
          .padding(horizontal = 8.dp, vertical = 4.dp)
          .testTag("btn_custom_cash_adder")
      ) {
        Text(
          text = "+ Custom Cash",
          color = PurpleLight,
          fontSize = 10.sp,
          fontFamily = AptosGrotesqueFontFamily,
          fontWeight = FontWeight.Bold
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Category Filter Chips with Smooth Spring Transitions
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      val categories = listOf("All", "Fitness", "Housing & Home", "Essentials")
      items(categories) { category ->
        val isSelected = selectedCategory == category
        val animatedBg by animateColorAsState(
          targetValue = if (isSelected) PurpleBg else DarkSurfaceSunken,
          animationSpec = tween(durationMillis = 180),
          label = "chip_bg"
        )
        val animatedBorder by animateColorAsState(
          targetValue = if (isSelected) PurpleBorder else DarkBorderSubtle,
          animationSpec = tween(durationMillis = 180),
          label = "chip_border"
        )
        val animatedText by animateColorAsState(
          targetValue = if (isSelected) PurpleLight else TextMuted,
          animationSpec = tween(durationMillis = 180),
          label = "chip_text"
        )

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(animatedBg)
            .border(1.dp, animatedBorder, RoundedCornerShape(10.dp))
            .clickable { selectedCategory = category }
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Text(
            text = category,
            color = animatedText,
            fontSize = 10.5.sp,
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Interactive Preset Cards Shelf
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(filteredPresets, key = { it.id }) { preset ->
        val isAdded = addedStateMap[preset.id] == true

        OfflinePresetCard(
          preset = preset,
          isAdded = isAdded,
          onTapAdd = {
            if (!isAdded) {
              val newSub = Subscription(
                id = "cash_${UUID.randomUUID().toString().take(8)}",
                merchantName = preset.name,
                currentCost = preset.defaultCost,
                billingCadence = preset.defaultCadence,
                classification = SubscriptionClassification.ACTIVE_UTILITY,
                recommendedAction = RecommendedAction.KEEP,
                targetNegotiatedPrice = preset.defaultCost,
                negotiationLeverage = "Cash payment with direct vendor",
                category = preset.category,
                daysSinceLastUse = 0,
                renewalDate = preset.defaultRenewalNotice,
                startDate = "Today",
                status = SubscriptionStatus.ACTIVE,
                isOfflineCash = true,
                paymentMethod = "Cash in Hand",
                cashCollectorName = preset.collectorHint,
                cashReceiptNote = preset.receiptTip
              )
              onAddSubscription(newSub)

              addedStateMap[preset.id] = true
              coroutineScope.launch {
                delay(3000)
                addedStateMap[preset.id] = false
              }
            }
          }
        )
      }
    }
  }
}

/**
 * Single Neumorphic Preset Card with Jitter-Free Doddly Micro-Animations.
 */
@Composable
private fun OfflinePresetCard(
  preset: OfflineSubscriptionPreset,
  isAdded: Boolean,
  onTapAdd: () -> Unit,
  modifier: Modifier = Modifier
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.94f else if (isAdded) 1.03f else 1f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "card_spring_scale"
  )

  val borderGlow by animateColorAsState(
    targetValue = if (isAdded) PurpleLight else if (isPressed) PurpleAccent else DarkBorder,
    animationSpec = tween(durationMillis = 200),
    label = "card_glow"
  )

  Box(
    modifier = modifier
      .width(168.dp)
      .scale(scale)
      .shadow(
        elevation = if (isAdded) 8.dp else 4.dp,
        shape = RoundedCornerShape(16.dp),
        spotColor = if (isAdded) Color(0x60A855F7) else Color(0xFF040307),
        ambientColor = Color.Black
      )
      .clip(RoundedCornerShape(16.dp))
      .background(
        Brush.verticalGradient(
          listOf(DarkSurfaceElevated, DarkSurface)
        )
      )
      .border(1.2.dp, borderGlow, RoundedCornerShape(16.dp))
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onTapAdd
      )
      .padding(12.dp)
      .testTag("preset_card_${preset.id}")
  ) {
    Column {
      // Top Tag & Emoji
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        BrandIcon(
          name = preset.name,
          size = 34.dp,
          iconSize = 20.dp
        )

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(PurpleBg)
            .border(0.5.dp, PurpleBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp)
        ) {
          Text(
            text = preset.tag,
            color = PurpleLight,
            fontSize = 7.5.sp,
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Preset Title
      Text(
        text = preset.name,
        color = TextPrimary,
        fontFamily = AptosGrotesqueFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      // Collector Subtitle
      Text(
        text = preset.collectorHint,
        color = TextMuted,
        fontFamily = AptosGrotesqueFontFamily,
        fontSize = 9.5.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Price Tag (Space Grotesk GPay / PhonePe style)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column {
          Text(
            text = "₹${preset.defaultCost.toInt()}",
            color = TextPrimary,
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Text(
            text = "/month (Cash)",
            color = TextMuted,
            fontFamily = AptosGrotesqueFontFamily,
            fontSize = 8.sp
          )
        }

        // Tap Add Button with Smooth Spring Doddly Animation
        val buttonBg by animateColorAsState(
          targetValue = if (isAdded) PurplePrimary else DarkSurfaceSunken,
          animationSpec = tween(durationMillis = 200),
          label = "btnBg"
        )
        val buttonBorder by animateColorAsState(
          targetValue = if (isAdded) PurpleLight else DarkBorder,
          animationSpec = tween(durationMillis = 200),
          label = "btnBorder"
        )

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(buttonBg)
            .border(1.dp, buttonBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 7.dp, vertical = 4.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            if (isAdded) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Added",
                tint = Color.White,
                modifier = Modifier.size(11.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = "Added",
                color = Color.White,
                fontSize = 9.sp,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = FontWeight.Bold
              )
            } else {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = PurpleLight,
                modifier = Modifier.size(10.dp)
              )
              Spacer(modifier = Modifier.width(2.dp))
              Text(
                text = "Tap",
                color = PurpleLight,
                fontSize = 9.sp,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}
