package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BillingCadence
import com.example.model.Subscription
import com.example.model.SubscriptionClassification
import com.example.ui.theme.AptosGrotesqueFontFamily
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceSunken
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.SoftAmber
import com.example.ui.theme.SoftRose
import com.example.ui.theme.SoftSage
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlin.math.atan2

data class CategorySpendSlice(
  val categoryName: String,
  val amount: Double,
  val percentage: Float,
  val color: Color,
  val subscriptions: List<Subscription>
)

private val CATEGORY_COLOR_PALETTE = listOf(
  PurpleLight,          // Neon Lavender #C084FC
  SkyAccent,            // Sky Cyan #38BDF8
  SoftSage,             // Vibrant Sage #10B981
  SoftAmber,            // Warm Amber #F59E0B
  SoftRose,             // Soft Rose #F43F5E
  Color(0xFF818CF8),    // Electric Indigo
  Color(0xFFFB7185),    // Coral Rose
  Color(0xFF34D399)     // Bright Mint
)

/**
 * High-fidelity, aesthetic Animated Donut Chart & Money Trail Breakdown for Subscriptions.AI.
 * Features item-by-item sequential pop-up entrance animations, slice inspection,
 * replay controls, and exact destination money flow drill-downs.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySpendDonutChart(
  subscriptions: List<Subscription>,
  modifier: Modifier = Modifier
) {
  val haptic = LocalHapticFeedback.current

  // 1. Compute category breakdown with attached subscriptions
  val categorySlices = remember(subscriptions) {
    if (subscriptions.isEmpty()) {
      emptyList()
    } else {
      val grouped = subscriptions.groupBy { it.category.ifBlank { "Other" } }
      val totalSpend = subscriptions.sumOf { it.currentCost }.coerceAtLeast(1.0)

      grouped.entries.mapIndexed { index, entry ->
        val catSpend = entry.value.sumOf { it.currentCost }
        val pct = (catSpend / totalSpend * 100).toFloat()
        val color = CATEGORY_COLOR_PALETTE[index % CATEGORY_COLOR_PALETTE.size]
        CategorySpendSlice(
          categoryName = entry.key,
          amount = catSpend,
          percentage = pct,
          color = color,
          subscriptions = entry.value.sortedByDescending { it.currentCost }
        )
      }.sortedByDescending { it.amount }
    }
  }

  val totalMonthlySpend = remember(subscriptions) { subscriptions.sumOf { it.currentCost } }
  var selectedCategoryName by remember { mutableStateOf<String?>(null) }
  val selectedSlice = categorySlices.find { it.categoryName == selectedCategoryName }

  // Sequence replay trigger state
  var animationKey by remember { mutableIntStateOf(0) }

  // 2. Main Donut Arc sweep animation
  val arcAnimationProgress = remember { Animatable(0f) }

  // 3. Sequential Staggered Pop-Up States for Category Legend Chips
  val itemPopStates = remember(categorySlices, animationKey) {
    List(categorySlices.size) { Animatable(0f) }
  }

  // Trigger sequential pop-up cascade whenever categorySlices or replay is triggered
  LaunchedEffect(categorySlices, animationKey) {
    arcAnimationProgress.snapTo(0f)
    itemPopStates.forEach { it.snapTo(0f) }

    // First sweep the main arc
    arcAnimationProgress.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )

    // Then pop up each category chip and spend detail sequentially with haptic tick
    categorySlices.forEachIndexed { index, _ ->
      delay(90)
      haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
      itemPopStates.getOrNull(index)?.animateTo(
        targetValue = 1f,
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessLow
        )
      )
    }
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .shadow(
        elevation = 8.dp,
        shape = RoundedCornerShape(24.dp),
        spotColor = Color(0xFF040307),
        ambientColor = Color(0xFF040307)
      )
      .clip(RoundedCornerShape(24.dp))
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
        shape = RoundedCornerShape(24.dp)
      )
      .padding(20.dp)
      .testTag("category_spend_donut_card")
  ) {
    Column {
      // Top Header Row with enlarged text & Replay Pop-up Sequence button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFF261D42))
              .border(1.dp, PurplePrimary, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.PieChart,
              contentDescription = "Category Spend Chart",
              tint = PurpleLight,
              modifier = Modifier.size(20.dp)
            )
          }

          Column {
            Text(
              text = "Category Spend Breakdown",
              fontFamily = AptosGrotesqueFontFamily,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 17.5.sp,
              color = TextPrimary
            )
            Text(
              text = "${categorySlices.size} active categories • Tap slice to see destination",
              fontFamily = AptosGrotesqueFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp,
              color = TextSecondary
            )
          }
        }

        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Replay sequence animation button
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(DarkSurfaceSunken)
              .border(1.dp, Color(0xFF3B2D60), RoundedCornerShape(10.dp))
              .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                animationKey++
              }
              .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Replay sequence",
                tint = SkyAccent,
                modifier = Modifier.size(13.dp)
              )
              Text(
                text = "Replay",
                fontFamily = AptosGrotesqueFontFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = SkyAccent
              )
            }
          }

          if (selectedCategoryName != null) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(DarkSurfaceSunken)
                .border(1.dp, PurplePrimary, RoundedCornerShape(10.dp))
                .clickable {
                  haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                  selectedCategoryName = null
                }
                .padding(horizontal = 9.dp, vertical = 6.dp)
            ) {
              Text(
                text = "Reset ✕",
                fontFamily = AptosGrotesqueFontFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleLight
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Donut Canvas & Center Infographic Display
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(230.dp),
        contentAlignment = Alignment.Center
      ) {
        val strokeWidthPx = 36.dp

        Canvas(
          modifier = Modifier
            .size(210.dp)
            .pointerInput(categorySlices) {
              detectTapGestures { tapOffset ->
                val center = Offset(size.width / 2f, size.height / 2f)
                val dx = tapOffset.x - center.x
                val dy = tapOffset.y - center.y
                var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                if (angle < 0) angle += 360f

                val rotatedAngle = (angle + 90f) % 360f

                var accumAngle = 0f
                for (slice in categorySlices) {
                  val sweepAngle = (slice.percentage / 100f) * 360f
                  if (rotatedAngle in accumAngle..(accumAngle + sweepAngle)) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    selectedCategoryName = if (selectedCategoryName == slice.categoryName) null else slice.categoryName
                    break
                  }
                  accumAngle += sweepAngle
                }
              }
            }
        ) {
          val canvasSize = size.minDimension
          val radius = (canvasSize - strokeWidthPx.toPx()) / 2f
          val arcBounds = Size(radius * 2f, radius * 2f)
          val arcOffset = Offset((size.width - arcBounds.width) / 2f, (size.height - arcBounds.height) / 2f)

          if (categorySlices.isEmpty()) {
            drawArc(
              color = DarkBorder,
              startAngle = 0f,
              sweepAngle = 360f,
              useCenter = false,
              topLeft = arcOffset,
              size = arcBounds,
              style = Stroke(width = strokeWidthPx.toPx(), cap = StrokeCap.Round)
            )
          } else {
            var currentStartAngle = -90f

            categorySlices.forEach { slice ->
              val rawSweep = (slice.percentage / 100f) * 360f
              val animatedSweep = rawSweep * arcAnimationProgress.value
              val isSelected = slice.categoryName == selectedCategoryName
              val isDimmed = selectedCategoryName != null && !isSelected

              val stroke = Stroke(
                width = if (isSelected) strokeWidthPx.toPx() + 10f else strokeWidthPx.toPx(),
                cap = StrokeCap.Butt
              )

              drawArc(
                color = if (isDimmed) slice.color.copy(alpha = 0.25f) else slice.color,
                startAngle = currentStartAngle,
                sweepAngle = (animatedSweep - 2.5f).coerceAtLeast(0.5f),
                useCenter = false,
                topLeft = arcOffset,
                size = arcBounds,
                style = stroke
              )

              currentStartAngle += rawSweep
            }
          }
        }

        // Center Content Overlay with ExtraBold Bolder Infographics
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
          modifier = Modifier.padding(horizontal = 24.dp)
        ) {
          val displayValue = selectedSlice?.amount ?: totalMonthlySpend
          val displayTitle = selectedSlice?.categoryName ?: "Monthly Spend"

          Text(
            text = "₹${"%.0f".format(displayValue)}",
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            color = TextPrimary,
            letterSpacing = (-1).sp
          )

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = displayTitle.uppercase(),
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (selectedSlice != null) selectedSlice.color else PurpleLight,
            letterSpacing = 0.5.sp,
            maxLines = 1
          )

          if (selectedSlice != null) {
            Text(
              text = "${selectedSlice.percentage.toInt()}% of total spend • ${selectedSlice.subscriptions.size} subs",
              fontFamily = AptosGrotesqueFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.5.sp,
              color = TextSecondary
            )
          } else {
            Text(
              text = "${subscriptions.size} total active services",
              fontFamily = AptosGrotesqueFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 11.sp,
              color = TextMuted
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Sequential Pop-Up Category Legend Chips FlowRow
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        categorySlices.forEachIndexed { index, slice ->
          val isSelected = slice.categoryName == selectedCategoryName
          val isDimmed = selectedCategoryName != null && !isSelected
          val popProgress = itemPopStates.getOrNull(index)?.value ?: 1f

          Box(
            modifier = Modifier.scale(popProgress)
          ) {
            Row(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                  if (isSelected) Color(0xFF2E2254) else DarkSurfaceSunken
                )
                .border(
                  width = 1.2.dp,
                  color = if (isSelected) slice.color else DarkBorder,
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                  haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                  selectedCategoryName = if (isSelected) null else slice.categoryName
                }
                .padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(if (isDimmed) slice.color.copy(alpha = 0.35f) else slice.color)
              )

              Spacer(modifier = Modifier.width(8.dp))

              Text(
                text = slice.categoryName,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                fontSize = 13.sp,
                color = if (isDimmed) TextMuted else TextPrimary
              )

              Spacer(modifier = Modifier.width(8.dp))

              Text(
                text = "${slice.percentage.toInt()}% • ₹${slice.amount.toInt()}",
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = if (isSelected) slice.color else TextSecondary
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // WHERE THE MONEY IS EXACTLY GOING: Money Destination Breakdown Panel
      ExactMoneyDestinationTrail(
        categorySlices = categorySlices,
        selectedCategoryName = selectedCategoryName,
        totalMonthlySpend = totalMonthlySpend,
        onSelectCategory = { cat ->
          haptic.performHapticFeedback(HapticFeedbackType.LongPress)
          selectedCategoryName = if (selectedCategoryName == cat) null else cat
        }
      )
    }
  }
}

/**
 * Exact Money Destination Drill-down list component showing precisely where the money is going
 * for each subscription category with pop-up card animations, merchant logos, and payment cadences.
 */
@Composable
private fun ExactMoneyDestinationTrail(
  categorySlices: List<CategorySpendSlice>,
  selectedCategoryName: String?,
  totalMonthlySpend: Double,
  onSelectCategory: (String) -> Unit
) {
  val activeSlices = if (selectedCategoryName != null) {
    categorySlices.filter { it.categoryName == selectedCategoryName }
  } else {
    categorySlices
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .background(DarkSurfaceSunken)
      .border(1.dp, Color(0xFF2B2245), RoundedCornerShape(18.dp))
      .padding(16.dp)
  ) {
    // Section Title & Money Destination Summary
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color(0xFF2E234E)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "₹",
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = PurpleLight
          )
        }

        Text(
          text = if (selectedCategoryName != null) "Money Going to $selectedCategoryName" else "Where The Money Is Exactly Going",
          fontFamily = AptosGrotesqueFontFamily,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 14.5.sp,
          color = TextPrimary
        )
      }

      Text(
        text = "${activeSlices.sumOf { it.subscriptions.size }} Services",
        fontFamily = AptosGrotesqueFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = SkyAccent
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    // List of Categories and their exact subscription breakdown cards
    activeSlices.forEach { slice ->
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
      ) {
        // Category Header Strip
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1E1736))
            .border(1.dp, slice.color.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(slice.color)
            )

            Text(
              text = slice.categoryName,
              fontFamily = AptosGrotesqueFontFamily,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 13.sp,
              color = TextPrimary
            )
          }

          Text(
            text = "Total: ₹${"%.0f".format(slice.amount)} / mo (${slice.percentage.toInt()}%)",
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = slice.color
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Subscription Items inside Category - Pop-up cards
        slice.subscriptions.forEachIndexed { subIndex, sub ->
          val subPopProgress = remember(sub.id) { Animatable(0f) }

          LaunchedEffect(sub.id) {
            delay(subIndex * 60L)
            subPopProgress.animateTo(
              targetValue = 1f,
              animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
              )
            )
          }

          Box(
            modifier = Modifier
              .scale(subPopProgress.value)
              .padding(vertical = 3.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF151128))
                .border(1.dp, Color(0xFF281F42), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 9.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
              ) {
                BrandIcon(
                  name = sub.merchantName,
                  size = 34.dp,
                  iconSize = 20.dp
                )

                Column {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Text(
                      text = sub.merchantName,
                      fontFamily = AptosGrotesqueFontFamily,
                      fontWeight = FontWeight.ExtraBold,
                      fontSize = 13.5.sp,
                      color = TextPrimary
                    )

                    if (sub.classification == SubscriptionClassification.ZOMBIE_SUBSCRIPTION) {
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(6.dp))
                          .background(SoftRose.copy(alpha = 0.2f))
                          .padding(horizontal = 5.dp, vertical = 2.dp)
                      ) {
                        Text(
                          text = "ZOMBIE",
                          fontFamily = AptosGrotesqueFontFamily,
                          fontWeight = FontWeight.ExtraBold,
                          fontSize = 9.sp,
                          color = SoftRose
                        )
                      }
                    } else if (sub.isOfflineCash) {
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(6.dp))
                          .background(SoftAmber.copy(alpha = 0.2f))
                          .padding(horizontal = 5.dp, vertical = 2.dp)
                      ) {
                        Text(
                          text = "CASH",
                          fontFamily = AptosGrotesqueFontFamily,
                          fontWeight = FontWeight.ExtraBold,
                          fontSize = 9.sp,
                          color = SoftAmber
                        )
                      }
                    }
                  }

                  Text(
                    text = "${if (sub.billingCadence == BillingCadence.ANNUAL) "Annual" else "Monthly"} • Renews ${sub.renewalDate}",
                    fontFamily = AptosGrotesqueFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = TextMuted
                  )
                }
              }

              // Exact Monthly Contribution Amount
              Column(
                horizontalAlignment = Alignment.End
              ) {
                Text(
                  text = "₹${"%.0f".format(sub.currentCost)}",
                  fontFamily = SpaceGroteskFontFamily,
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 14.sp,
                  color = TextPrimary
                )

                val percentageOfTotal = if (totalMonthlySpend > 0) (sub.currentCost / totalMonthlySpend * 100).toFloat() else 0f
                Text(
                  text = "${"%.1f".format(percentageOfTotal)}% of total",
                  fontFamily = AptosGrotesqueFontFamily,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 10.5.sp,
                  color = PurpleLight
                )
              }
            }
          }
        }
      }
    }
  }
}
