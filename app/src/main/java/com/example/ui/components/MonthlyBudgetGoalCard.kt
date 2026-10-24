package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Subscription
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

/**
 * High-fidelity, highly graphical Monthly Subscription Budget Goal & Utilization Card.
 * Features an interactive budget goal slider/input, multi-tiered graphical progress bar,
 * status indicator badges, and budget burner insights.
 */
@Composable
fun MonthlyBudgetGoalCard(
  totalMonthlySpend: Double,
  budgetGoal: Double,
  onUpdateBudgetGoal: (Double) -> Unit,
  subscriptions: List<Subscription> = emptyList(),
  modifier: Modifier = Modifier
) {
  val haptic = LocalHapticFeedback.current
  val keyboardController = LocalSoftwareKeyboardController.current
  var isEditingGoal by remember { mutableStateOf(false) }
  var sliderValue by remember(budgetGoal) { mutableFloatStateOf(budgetGoal.toFloat()) }
  var textInputValue by remember(budgetGoal) { mutableStateOf(budgetGoal.toInt().toString()) }

  // Utilization Calculations
  val utilizationPercentage = if (budgetGoal > 0) ((totalMonthlySpend / budgetGoal) * 100).toFloat() else 0f
  val isOverBudget = totalMonthlySpend > budgetGoal
  val remainingBudget = (budgetGoal - totalMonthlySpend).coerceAtLeast(0.0)
  val overrunAmount = (totalMonthlySpend - budgetGoal).coerceAtLeast(0.0)

  // Status Badge Colors & Text
  val statusText: String
  val statusColor: Color
  val statusBgColor: Color
  val progressGradient: List<Color>

  when {
    isOverBudget -> {
      statusText = "BUDGET OVERRUN • Exceeded cap by ₹${"%.0f".format(overrunAmount)}"
      statusColor = SoftRose
      statusBgColor = SoftRose.copy(alpha = 0.18f)
      progressGradient = listOf(PurplePrimary, SoftAmber, SoftRose)
    }
    utilizationPercentage >= 80f -> {
      statusText = "NEAR BUDGET CAP • ₹${"%.0f".format(remainingBudget)} remaining"
      statusColor = SoftAmber
      statusBgColor = SoftAmber.copy(alpha = 0.18f)
      progressGradient = listOf(PurplePrimary, SkyAccent, SoftAmber)
    }
    else -> {
      statusText = "SAFE ZONE • ₹${"%.0f".format(remainingBudget)} remaining"
      statusColor = SkyAccent
      statusBgColor = SkyAccent.copy(alpha = 0.18f)
      progressGradient = listOf(PurplePrimary, SkyAccent)
    }
  }

  val animatedProgress by animateFloatAsState(
    targetValue = (utilizationPercentage / 100f).coerceIn(0f, 1f),
    animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
    label = "budget_progress"
  )

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
      .testTag("monthly_budget_goal_card")
  ) {
    Column {
      // Header Row
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
              imageVector = Icons.Default.AccountBalanceWallet,
              contentDescription = "Monthly Budget Goal",
              tint = PurpleLight,
              modifier = Modifier.size(20.dp)
            )
          }

          Column {
            Text(
              text = "Monthly Budget Goal",
              fontFamily = AptosGrotesqueFontFamily,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 17.sp,
              color = TextPrimary
            )
            Text(
              text = "Cap recurring charges & track spend limit",
              fontFamily = AptosGrotesqueFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp,
              color = TextSecondary
            )
          }
        }

        // Set / Adjust Budget Action Button
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurfaceSunken)
            .border(1.dp, if (isEditingGoal) PurpleLight else Color(0xFF3B2D60), RoundedCornerShape(10.dp))
            .clickable {
              haptic.performHapticFeedback(HapticFeedbackType.LongPress)
              isEditingGoal = !isEditingGoal
            }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("btn_adjust_budget_goal")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = if (isEditingGoal) Icons.Default.Check else Icons.Default.Tune,
              contentDescription = "Adjust Budget",
              tint = PurpleLight,
              modifier = Modifier.size(13.dp)
            )
            Text(
              text = if (isEditingGoal) "Done" else "Adjust Goal",
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 11.5.sp,
              fontWeight = FontWeight.Bold,
              color = PurpleLight
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Main Budget Metrics Display
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = "₹${"%.0f".format(totalMonthlySpend)}",
              fontFamily = SpaceGroteskFontFamily,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 30.sp,
              color = if (isOverBudget) SoftRose else TextPrimary,
              letterSpacing = (-1).sp
            )
            Text(
              text = "/ ₹${"%.0f".format(budgetGoal)} budget",
              fontFamily = AptosGrotesqueFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = TextSecondary,
              modifier = Modifier.padding(bottom = 4.dp)
            )
          }
        }

        // Percentage Utilization Pill
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(statusBgColor)
            .border(1.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Text(
            text = "${utilizationPercentage.toInt()}% UTILIZED",
            fontFamily = SpaceGroteskFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            color = statusColor
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Highly Graphical Multi-Tiered Progress Bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(28.dp),
        contentAlignment = Alignment.CenterStart
      ) {
        Canvas(
          modifier = Modifier
            .fillMaxWidth()
            .height(18.dp)
            .clip(RoundedCornerShape(10.dp))
        ) {
          val width = size.width
          val height = size.height

          // Track Background
          drawRoundRect(
            color = Color(0xFF13101F),
            size = size,
            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
          )

          // Filled Progress Bar
          val fillWidth = width * animatedProgress
          if (fillWidth > 0f) {
            drawRoundRect(
              brush = Brush.horizontalGradient(progressGradient),
              size = Size(fillWidth, height),
              cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
            )
          }

          // Threshold Markers (25%, 50%, 75%, 100%)
          listOf(0.25f, 0.50f, 0.75f).forEach { pct ->
            val markX = width * pct
            drawLine(
              color = Color(0x40FFFFFF),
              start = Offset(markX, 0f),
              end = Offset(markX, height),
              strokeWidth = 1.dp.toPx()
            )
          }
        }

        // Pointer Glow Indicator Dot at Spent Location
        if (animatedProgress > 0.05f) {
          Box(
            modifier = Modifier
              .fillMaxWidth(animatedProgress)
          ) {
            Box(
              modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(18.dp)
                .shadow(6.dp, CircleShape, spotColor = statusColor)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, statusColor, CircleShape)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Status Badge Banner Below Progress Bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0xFF17122B))
          .border(1.dp, statusColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
          .padding(horizontal = 12.dp, vertical = 7.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(statusColor)
          )

          Text(
            text = statusText,
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            color = statusColor
          )
        }
      }

      // Expandable Budget Goal Setting Panel
      AnimatedVisibility(
        visible = isEditingGoal,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurfaceSunken)
            .border(1.dp, Color(0xFF332654), RoundedCornerShape(16.dp))
            .padding(14.dp)
        ) {
          Text(
            text = "SET YOUR MONTHLY BUDGET CAP",
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            color = PurpleLight,
            letterSpacing = 0.5.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Slider Control
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "₹1k",
              fontFamily = SpaceGroteskFontFamily,
              fontSize = 11.sp,
              color = TextMuted
            )

            Slider(
              value = sliderValue,
              onValueChange = { valFloat ->
                sliderValue = valFloat
                textInputValue = valFloat.toInt().toString()
                onUpdateBudgetGoal(valFloat.toDouble())
              },
              valueRange = 1000f..30000f,
              steps = 57, // Steps of ₹500
              modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .testTag("slider_budget_goal"),
              colors = SliderDefaults.colors(
                thumbColor = PurpleLight,
                activeTrackColor = PurplePrimary,
                inactiveTrackColor = Color(0xFF261D42)
              )
            )

            Text(
              text = "₹30k",
              fontFamily = SpaceGroteskFontFamily,
              fontSize = 11.sp,
              color = TextMuted
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Quick Preset Chips & Custom Input Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            listOf(3000.0, 5000.0, 10000.0, 15000.0).forEach { preset ->
              val isSelected = budgetGoal == preset
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) PurplePrimary else Color(0xFF1E1736))
                  .border(1.dp, if (isSelected) PurpleLight else DarkBorder, RoundedCornerShape(8.dp))
                  .clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    sliderValue = preset.toFloat()
                    textInputValue = preset.toInt().toString()
                    onUpdateBudgetGoal(preset)
                  }
                  .padding(vertical = 7.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "₹${preset.toInt() / 1000}k",
                  fontFamily = SpaceGroteskFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.5.sp,
                  color = if (isSelected) Color.White else TextSecondary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Manual Numeric Input Field
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = textInputValue,
              onValueChange = { newVal ->
                textInputValue = newVal.filter { it.isDigit() }
                val parsed = textInputValue.toDoubleOrNull()
                if (parsed != null && parsed >= 500) {
                  sliderValue = parsed.toFloat().coerceIn(1000f, 30000f)
                  onUpdateBudgetGoal(parsed)
                }
              },
              label = {
                Text(
                  text = "Custom Budget Amount (₹)",
                  fontSize = 11.sp,
                  color = TextMuted
                )
              },
              keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
              ),
              keyboardActions = KeyboardActions(
                onDone = {
                  keyboardController?.hide()
                  isEditingGoal = false
                }
              ),
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF151128),
                unfocusedContainerColor = Color(0xFF151128),
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = DarkBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .testTag("input_custom_budget_goal")
            )

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(PurplePrimary)
                .clickable {
                  haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                  keyboardController?.hide()
                  isEditingGoal = false
                }
                .padding(horizontal = 16.dp, vertical = 14.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "Apply",
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp,
                color = Color.White
              )
            }
          }
        }
      }

      // Top 3 Budget Burners List Preview
      if (subscriptions.isNotEmpty()) {
        Spacer(modifier = Modifier.height(14.dp))

        val topBurners = remember(subscriptions) {
          subscriptions
            .filter { it.status != com.example.model.SubscriptionStatus.CANCELLED }
            .sortedByDescending { it.currentCost }
            .take(3)
        }

        if (topBurners.isNotEmpty()) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(DarkSurfaceSunken)
              .border(1.dp, Color(0xFF281F42), RoundedCornerShape(12.dp))
              .padding(10.dp)
          ) {
            Text(
              text = "TOP BUDGET CONSUMERS",
              fontFamily = AptosGrotesqueFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 10.5.sp,
              color = TextMuted,
              letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              topBurners.forEach { sub ->
                val subPct = if (budgetGoal > 0) ((sub.currentCost / budgetGoal) * 100).toInt() else 0
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF191330))
                    .border(1.dp, Color(0xFF332756), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                  Column {
                    Text(
                      text = sub.merchantName,
                      fontFamily = AptosGrotesqueFontFamily,
                      fontWeight = FontWeight.Bold,
                      fontSize = 11.5.sp,
                      color = TextPrimary,
                      maxLines = 1
                    )
                    Text(
                      text = "₹${"%.0f".format(sub.currentCost)} ($subPct% budget)",
                      fontFamily = SpaceGroteskFontFamily,
                      fontWeight = FontWeight.ExtraBold,
                      fontSize = 10.sp,
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
  }
}
