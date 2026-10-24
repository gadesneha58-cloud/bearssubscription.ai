package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BillingCadence
import com.example.model.OfflinePresetCatalog
import com.example.model.OfflineSubscriptionPreset
import com.example.model.RecommendedAction
import com.example.model.Subscription
import com.example.model.SubscriptionClassification
import com.example.model.SubscriptionStatus
import com.example.ui.components.NeumorphicPrimaryButton
import com.example.ui.theme.AptosGrotesqueFontFamily
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceSunken
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleBg
import com.example.ui.theme.PurpleBorder
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubscriptionDialog(
  onDismiss: () -> Unit,
  onAddSubscription: (Subscription) -> Unit,
  initialCashMode: Boolean = false
) {
  var isCashMode by remember { mutableStateOf(initialCashMode) }

  // Form Fields
  var merchantName by remember { mutableStateOf("") }
  var costInput by remember { mutableStateOf("") }
  var selectedCadence by remember { mutableStateOf(BillingCadence.MONTHLY) }
  var selectedCategory by remember { mutableStateOf(if (initialCashMode) "Fitness" else "Entertainment") }
  var renewalDate by remember { mutableStateOf(if (initialCashMode) "1st of next month" else "In 30 days") }
  var paymentMethod by remember { mutableStateOf(if (initialCashMode) "Cash in Hand" else "Digital") }
  var cashCollectorName by remember { mutableStateOf("") }
  var cashReceiptNote by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val categories = listOf("Fitness", "Housing", "Domestic", "Essentials", "Entertainment", "Utilities", "Telecom", "Cloud / SaaS", "Other")
  val cashPaymentOptions = listOf("Cash in Hand", "UPI Direct to Owner", "Cheque / Slip")

  BasicAlertDialog(onDismissRequest = onDismiss) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(22.dp))
        .background(DarkSurfaceElevated)
        .border(1.2.dp, DarkBorder, RoundedCornerShape(22.dp))
        .padding(20.dp)
        .testTag("add_subscription_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
      ) {
        // Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (isCashMode) "Add Cash & Gym Dues" else "Add Subscription",
              fontFamily = SpaceGroteskFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 19.sp,
              color = TextPrimary
            )
            Text(
              text = if (isCashMode) "Track Gym, Society, Milk, Maid & cash recurring" else "Feed your active subscription to track & optimize",
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 11.sp,
              color = TextSecondary
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextMuted,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Animated Segmented Mode Switcher (Digital vs Cash/Offline)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceSunken)
            .border(1.dp, DarkBorderSubtle, RoundedCornerShape(12.dp))
            .padding(4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          // Digital Button
          val isDigital = !isCashMode
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(9.dp))
              .background(if (isDigital) PurpleBg else Color.Transparent)
              .border(1.dp, if (isDigital) PurpleBorder else Color.Transparent, RoundedCornerShape(9.dp))
              .clickable {
                isCashMode = false
                paymentMethod = "Digital"
                errorMessage = null
              }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                tint = if (isDigital) PurpleLight else TextMuted,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Online / AutoPay",
                color = if (isDigital) PurpleLight else TextMuted,
                fontSize = 11.sp,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = if (isDigital) FontWeight.Bold else FontWeight.Medium
              )
            }
          }

          // Cash Button
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(9.dp))
              .background(if (isCashMode) PurpleBg else Color.Transparent)
              .border(1.dp, if (isCashMode) PurpleBorder else Color.Transparent, RoundedCornerShape(9.dp))
              .clickable {
                isCashMode = true
                paymentMethod = "Cash in Hand"
                selectedCategory = "Fitness"
                renewalDate = "1st of every month"
                errorMessage = null
              }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Payments,
                contentDescription = null,
                tint = if (isCashMode) PurpleLight else TextMuted,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "💵 Cash / Offline",
                color = if (isCashMode) PurpleLight else TextMuted,
                fontSize = 11.sp,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = if (isCashMode) FontWeight.Bold else FontWeight.Medium
              )
            }
          }
        }

        // Quick-Tap Preset Shelf (Visible especially in Cash mode)
        AnimatedVisibility(
          visible = isCashMode,
          enter = fadeIn() + expandVertically(),
          exit = fadeOut() + shrinkVertically()
        ) {
          Column {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
              text = "TAP PRESET TO AUTOFILL",
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 9.5.sp,
              fontWeight = FontWeight.Bold,
              color = PurpleLight,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              items(OfflinePresetCatalog.commonPresets) { preset ->
                val isSelected = merchantName.contains(preset.name, ignoreCase = true)
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) PurpleBg else DarkSurfaceSunken)
                    .border(1.dp, if (isSelected) PurpleLight else DarkBorderSubtle, RoundedCornerShape(10.dp))
                    .clickable {
                      merchantName = preset.name
                      costInput = preset.defaultCost.toInt().toString()
                      selectedCategory = preset.category
                      cashCollectorName = preset.collectorHint
                      cashReceiptNote = preset.receiptTip
                      renewalDate = preset.defaultRenewalNotice
                      errorMessage = null
                    }
                    .padding(horizontal = 9.dp, vertical = 6.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = preset.iconEmoji, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = preset.name,
                      color = if (isSelected) PurpleLight else TextPrimary,
                      fontSize = 10.sp,
                      fontFamily = AptosGrotesqueFontFamily,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Service Name Input
        Text(
          text = if (isCashMode) "RECURRING CASH EXPENSE / GYM" else "SERVICE OR VENDOR NAME",
          fontFamily = AptosGrotesqueFontFamily,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = PurpleLight,
          letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = merchantName,
          onValueChange = {
            merchantName = it
            errorMessage = null
          },
          placeholder = {
            Text(
              if (isCashMode) "e.g. Gold's Gym, Society Maintenance, Cook Salary" else "e.g. Netflix, Spotify, Claude Pro",
              color = TextMuted,
              fontSize = 12.sp,
              fontFamily = AptosGrotesqueFontFamily
            )
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_service_name"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PurpleLight,
            unfocusedBorderColor = DarkBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedContainerColor = DarkSurfaceSunken,
            unfocusedContainerColor = DarkSurfaceSunken
          ),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Amount & Cadence Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Cost Input
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (isCashMode) "CASH AMOUNT (₹)" else "MONTHLY COST (₹)",
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = PurpleLight,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
              value = costInput,
              onValueChange = {
                costInput = it.filter { char -> char.isDigit() || char == '.' }
                errorMessage = null
              },
              placeholder = {
                Text(
                  if (isCashMode) "1500" else "499",
                  color = TextMuted,
                  fontSize = 13.sp,
                  fontFamily = SpaceGroteskFontFamily
                )
              },
              singleLine = true,
              prefix = {
                Text(
                  "₹ ",
                  color = PurpleLight,
                  fontWeight = FontWeight.Bold,
                  fontFamily = SpaceGroteskFontFamily
                )
              },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_cost"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurpleLight,
                unfocusedBorderColor = DarkBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = DarkSurfaceSunken,
                unfocusedContainerColor = DarkSurfaceSunken
              ),
              shape = RoundedCornerShape(12.dp)
            )
          }

          // Billing Cycle
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "FREQUENCY",
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = PurpleLight,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DarkSurfaceSunken)
                .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                .padding(3.dp),
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(9.dp))
                  .background(if (selectedCadence == BillingCadence.MONTHLY) PurpleBg else Color.Transparent)
                  .clickable { selectedCadence = BillingCadence.MONTHLY }
                  .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "Monthly",
                  color = if (selectedCadence == BillingCadence.MONTHLY) PurpleLight else TextMuted,
                  fontSize = 11.sp,
                  fontFamily = AptosGrotesqueFontFamily,
                  fontWeight = FontWeight.Bold
                )
              }

              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(9.dp))
                  .background(if (selectedCadence == BillingCadence.ANNUAL) PurpleBg else Color.Transparent)
                  .clickable { selectedCadence = BillingCadence.ANNUAL }
                  .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "Annual",
                  color = if (selectedCadence == BillingCadence.ANNUAL) PurpleLight else TextMuted,
                  fontSize = 11.sp,
                  fontFamily = AptosGrotesqueFontFamily,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }

        // Cash Specific Additional Fields
        AnimatedVisibility(
          visible = isCashMode,
          enter = fadeIn() + expandVertically(),
          exit = fadeOut() + shrinkVertically()
        ) {
          Column {
            Spacer(modifier = Modifier.height(12.dp))

            // Payment Mode Selector
            Text(
              text = "PAYMENT METHOD",
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = PurpleLight,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              cashPaymentOptions.forEach { option ->
                val isSelected = paymentMethod == option
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) PurpleBg else DarkSurfaceSunken)
                    .border(1.dp, if (isSelected) PurpleLight else DarkBorderSubtle, RoundedCornerShape(10.dp))
                    .clickable { paymentMethod = option }
                    .padding(vertical = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = option,
                    color = if (isSelected) PurpleLight else TextSecondary,
                    fontSize = 9.5.sp,
                    fontFamily = AptosGrotesqueFontFamily,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cash Collector / Person in Charge
            Text(
              text = "WHO RECEIVES CASH? (COLLECTOR / DESK)",
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = PurpleLight,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
              value = cashCollectorName,
              onValueChange = { cashCollectorName = it },
              placeholder = {
                Text(
                  "e.g. Ramesh (Gym Trainer), Society Manager, Milk Vendor",
                  color = TextMuted,
                  fontSize = 12.sp,
                  fontFamily = AptosGrotesqueFontFamily
                )
              },
              singleLine = true,
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurpleLight,
                unfocusedBorderColor = DarkBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = DarkSurfaceSunken,
                unfocusedContainerColor = DarkSurfaceSunken
              ),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Cash Receipt Reminder Note
            Text(
              text = "RECEIPT OR WITHDRAWAL REMINDER",
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = PurpleLight,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
              value = cashReceiptNote,
              onValueChange = { cashReceiptNote = it },
              placeholder = {
                Text(
                  "e.g. Keep ₹500 notes ready; collect stamped monthly receipt",
                  color = TextMuted,
                  fontSize = 12.sp,
                  fontFamily = AptosGrotesqueFontFamily
                )
              },
              singleLine = true,
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurpleLight,
                unfocusedBorderColor = DarkBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = DarkSurfaceSunken,
                unfocusedContainerColor = DarkSurfaceSunken
              ),
              shape = RoundedCornerShape(12.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Renewal / Cash Due Date
        Text(
          text = if (isCashMode) "CASH DUE DATE / CYCLE" else "NEXT RENEWAL DATE",
          fontFamily = AptosGrotesqueFontFamily,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = PurpleLight,
          letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = renewalDate,
          onValueChange = { renewalDate = it },
          placeholder = {
            Text(
              if (isCashMode) "e.g. 1st of every month, 5th of month" else "e.g. In 15 days, Nov 12, 2026",
              color = TextMuted,
              fontSize = 12.sp,
              fontFamily = AptosGrotesqueFontFamily
            )
          },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PurpleLight,
            unfocusedBorderColor = DarkBorder,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedContainerColor = DarkSurfaceSunken,
            unfocusedContainerColor = DarkSurfaceSunken
          ),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Pills
        Text(
          text = "CATEGORY",
          fontFamily = AptosGrotesqueFontFamily,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = PurpleLight,
          letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(categories) { category ->
            val isSelected = selectedCategory == category
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) PurpleBg else DarkSurfaceSunken)
                .border(1.dp, if (isSelected) PurpleBorder else DarkBorderSubtle, RoundedCornerShape(8.dp))
                .clickable { selectedCategory = category }
                .padding(horizontal = 9.dp, vertical = 5.dp)
            ) {
              Text(
                text = category,
                color = if (isSelected) PurpleLight else TextMuted,
                fontSize = 10.sp,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            }
          }
        }

        // Error message if any
        if (errorMessage != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = errorMessage!!,
            color = Color(0xFFF43F5E), // Warning Rose (Zero green)
            fontSize = 11.5.sp,
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Save Action Button
        NeumorphicPrimaryButton(
          onClick = {
            val cost = costInput.toDoubleOrNull()
            if (merchantName.isBlank()) {
              errorMessage = if (isCashMode) "Please enter service or gym name" else "Please enter the service name"
            } else if (cost == null || cost <= 0.0) {
              errorMessage = "Please enter a valid amount"
            } else {
              val newSub = Subscription(
                id = "sub_${UUID.randomUUID().toString().take(8)}",
                merchantName = merchantName.trim(),
                currentCost = cost,
                billingCadence = selectedCadence,
                classification = SubscriptionClassification.ACTIVE_UTILITY,
                recommendedAction = RecommendedAction.KEEP,
                targetNegotiatedPrice = cost * 0.85,
                negotiationLeverage = if (isCashMode) "Direct cash subscription with vendor" else "Direct user recorded subscription",
                category = selectedCategory,
                daysSinceLastUse = 0,
                renewalDate = renewalDate.trim().ifBlank { if (isCashMode) "1st of next month" else "In 30 days" },
                startDate = "Today",
                status = SubscriptionStatus.ACTIVE,
                isOfflineCash = isCashMode,
                paymentMethod = if (isCashMode) paymentMethod else "Digital",
                cashCollectorName = cashCollectorName.trim(),
                cashReceiptNote = cashReceiptNote.trim()
              )
              onAddSubscription(newSub)
              onDismiss()
            }
          },
          modifier = Modifier.fillMaxWidth(),
          testTag = "btn_save_subscription"
        ) {
          Icon(
            imageVector = if (isCashMode) Icons.Default.Payments else Icons.Default.CreditCard,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isCashMode) "Save Cash Subscription ➔" else "Save Subscription ➔",
            fontFamily = AptosGrotesqueFontFamily,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }
    }
  }
}
