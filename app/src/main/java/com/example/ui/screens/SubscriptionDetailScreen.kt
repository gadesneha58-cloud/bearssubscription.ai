package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.Warning
import com.example.ui.theme.AptosGrotesqueFontFamily
import com.example.ui.theme.PurpleUltraLight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BillingCadence
import com.example.model.RecommendedAction
import com.example.model.Subscription
import com.example.model.SubscriptionClassification
import com.example.model.SubscriptionStatus
import com.example.ui.components.BrandIcon
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.SubscriptionStatusBadge
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily
import com.example.ui.theme.PurpleBg
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.WarningRose
import com.example.ui.theme.WarningRoseBg
import com.example.ui.theme.SoftAmber
import com.example.ui.theme.SoftAmberBg
import com.example.ui.theme.SoftAmberBorder
import com.example.ui.theme.SoftRose
import com.example.ui.theme.SoftRoseBg
import com.example.ui.theme.SoftRoseBorder
import com.example.ui.theme.SoftSage
import com.example.ui.theme.SoftSageBg
import com.example.ui.theme.SoftSageBorder
import com.example.ui.theme.SoftSky
import com.example.ui.theme.SoftSkyBg
import com.example.ui.theme.SoftSkyBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPlaceholder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteBackground
import com.example.ui.theme.WhiteBorder
import com.example.ui.theme.WhiteBorderSubtle
import com.example.ui.theme.WhiteSurface

@Composable
fun SubscriptionDetailScreen(
  subscription: Subscription?,
  onBackClick: () -> Unit,
  onCancelSub: (String) -> Unit,
  onNegotiateSub: (Subscription) -> Unit,
  onToggleVirtualCard: (String) -> Unit,
  onToggleGdprWipe: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler(onBack = onBackClick)

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = WhiteBackground,
    topBar = {
      DetailTopBar(
        title = subscription?.merchantName ?: "Subscription Details",
        onBackClick = onBackClick
      )
    }
  ) { paddingValues ->
    if (subscription == null) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
          modifier = Modifier.padding(24.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Subscription not found",
            fontFamily = OutfitFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextPrimary
          )
          Spacer(modifier = Modifier.height(16.dp))
          NeumorphicButton(
            onClick = onBackClick,
            testTag = "back_to_list_button"
          ) {
            Text(
              text = "Back to Subscriptions",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = TextPrimary
            )
          }
        }
      }
    } else {
      val isCancelled = subscription.status == SubscriptionStatus.CANCELLED
      val isZombie = subscription.classification == SubscriptionClassification.ZOMBIE_SUBSCRIPTION
      val isNegotiated = subscription.status == SubscriptionStatus.NEGOTIATED_SAVED
      val annualCost = if (subscription.billingCadence == BillingCadence.MONTHLY) {
        subscription.currentCost * 12
      } else {
        subscription.currentCost
      }

      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        // Hero Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WhiteSurface)
            .border(1.dp, WhiteBorder, RoundedCornerShape(16.dp))
            .padding(18.dp)
            .testTag("detail_hero_card")
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
              ) {
                BrandIcon(
                  name = subscription.merchantName,
                  size = 52.dp
                )
                Column {
                  Text(
                    text = subscription.merchantName,
                    fontFamily = OutfitFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary
                  )
                  Text(
                    text = subscription.category,
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 12.sp,
                    color = TextMuted
                  )
                }
              }
              SubscriptionStatusBadge(subscription = subscription)
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = WhiteBorderSubtle, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Pricing Headline
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              Column {
                Text(
                  text = "CURRENT PLAN COST",
                  fontFamily = PlusJakartaSansFontFamily,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextMuted,
                  letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                  Text(
                    text = "$${String.format("%.2f", subscription.currentCost)}",
                    fontFamily = OutfitFontFamily,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = when {
                      isCancelled -> TextMuted
                      isZombie -> SoftRose
                      isNegotiated -> SoftSage
                      else -> TextPrimary
                    }
                  )
                  Text(
                    text = if (subscription.billingCadence == BillingCadence.MONTHLY) " / month" else " / year",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                  )
                }
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFF1F5F9))
                  .border(0.75.dp, WhiteBorder, RoundedCornerShape(8.dp))
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(
                  text = "Est. $${String.format("%.2f", annualCost)} / yr",
                  fontFamily = PlusJakartaSansFontFamily,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = TextSecondary
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Subscription Timeline & Metrics Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(WhiteSurface)
            .border(1.dp, WhiteBorder, RoundedCornerShape(14.dp))
            .padding(16.dp)
            .testTag("detail_metrics_card")
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
              text = "BILLING & RENEWAL METRICS",
              fontFamily = PlusJakartaSansFontFamily,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = TextMuted,
              letterSpacing = 0.5.sp
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              MetricItem(
                label = "Next Renewal",
                value = subscription.renewalDate,
                icon = Icons.Default.CalendarToday,
                color = if (subscription.renewalDate.contains("today", ignoreCase = true) || subscription.renewalDate.contains("1 day", ignoreCase = true)) SoftRose else SoftSky
              )
              MetricItem(
                label = "Billing Cycle",
                value = if (subscription.billingCadence == BillingCadence.MONTHLY) "Monthly" else "Annual",
                icon = Icons.Default.CreditCard,
                color = SoftSky
              )
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              MetricItem(
                label = "Member Since",
                value = subscription.startDate,
                icon = Icons.Default.CheckCircle,
                color = TextSecondary
              )
              MetricItem(
                label = "Usage Activity",
                value = if (subscription.daysSinceLastUse == 0) "Active Today" else "${subscription.daysSinceLastUse} days idle",
                icon = if (subscription.daysSinceLastUse > 30) Icons.Default.Warning else Icons.Default.Info,
                color = if (subscription.daysSinceLastUse > 30) SoftRose else SoftSage
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // AI Spend Optimization Insights
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(WhiteSurface)
            .border(1.dp, if (isZombie) SoftRoseBorder else if (isNegotiated) SoftSageBorder else WhiteBorder, RoundedCornerShape(14.dp))
            .padding(16.dp)
            .testTag("detail_optimization_card")
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = if (isZombie) SoftRose else SoftAmber,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = "AI OPTIMIZATION INSIGHTS",
                fontFamily = PlusJakartaSansFontFamily,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isZombie) SoftRose else SoftAmber,
                letterSpacing = 0.5.sp
              )
            }

            // Classification Analysis
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(
                  when (subscription.classification) {
                    SubscriptionClassification.ZOMBIE_SUBSCRIPTION -> SoftRoseBg
                    SubscriptionClassification.IDLE_SEAT -> SoftSkyBg
                    else -> SoftSageBg
                  }
                )
                .padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = when (subscription.classification) {
                  SubscriptionClassification.ZOMBIE_SUBSCRIPTION -> Icons.Default.Warning
                  SubscriptionClassification.IDLE_SEAT -> Icons.Default.Info
                  else -> Icons.Default.CheckCircle
                },
                contentDescription = null,
                tint = when (subscription.classification) {
                  SubscriptionClassification.ZOMBIE_SUBSCRIPTION -> SoftRose
                  SubscriptionClassification.IDLE_SEAT -> SoftSky
                  else -> SoftSage
                },
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = when (subscription.classification) {
                  SubscriptionClassification.ZOMBIE_SUBSCRIPTION ->
                    "Zombie Detected: Inactive for ${subscription.daysSinceLastUse} days. We recommend 1-Click Cancellation."
                  SubscriptionClassification.IDLE_SEAT ->
                    "Idle Enterprise Seat: License unassigned or unused. Reclaim to recover budget."
                  else ->
                    "Active Utility: Regularly used subscription. AI can renegotiate for lower monthly rate."
                },
                fontFamily = PlusJakartaSansFontFamily,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
              )
            }

            if (subscription.targetNegotiatedPrice > 0 && !isCancelled) {
              val potentialSavings = subscription.currentCost - subscription.targetNegotiatedPrice
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                    contentDescription = null,
                    tint = SoftSage,
                    modifier = Modifier.size(14.dp)
                  )
                  Text(
                    text = "AI Target Rate: ₹${String.format("%.2f", subscription.targetNegotiatedPrice)}/mo",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftSage
                  )
                }

                if (potentialSavings > 0) {
                  Text(
                    text = "Save ₹${String.format("%.2f", potentialSavings)}/mo",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SoftSage
                  )
                }
              }
            }

            if (!subscription.negotiationLeverage.isNullOrBlank()) {
              Text(
                text = "Negotiation Angle: ${subscription.negotiationLeverage}",
                fontFamily = PlusJakartaSansFontFamily,
                fontSize = 11.5.sp,
                color = TextSecondary
              )
            }

            if (!subscription.darkPatternWarning.isNullOrBlank()) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(SoftAmberBg)
                  .border(0.75.dp, SoftAmberBorder, RoundedCornerShape(8.dp))
                  .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Warning,
                  contentDescription = null,
                  tint = SoftAmber,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "Dark Pattern Warning: ${subscription.darkPatternWarning}",
                  fontFamily = PlusJakartaSansFontFamily,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium,
                  color = TextPrimary
                )
              }
            }
          }
        }

        if (subscription.isOfflineCash) {
          Spacer(modifier = Modifier.height(14.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(com.example.ui.theme.DarkSurfaceElevated)
              .border(1.dp, com.example.ui.theme.DarkBorder, RoundedCornerShape(14.dp))
              .padding(16.dp)
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Payments,
                  contentDescription = null,
                  tint = com.example.ui.theme.PurpleLight,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "OFFLINE CASH PAYMENT SPECIFICATIONS",
                  fontFamily = AptosGrotesqueFontFamily,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = com.example.ui.theme.PurpleLight,
                  letterSpacing = 0.5.sp
                )
              }

              Text(
                text = "Payment Method: ${subscription.paymentMethod}",
                fontFamily = AptosGrotesqueFontFamily,
                fontSize = 12.sp,
                color = TextPrimary
              )

              if (subscription.cashCollectorName.isNotBlank()) {
                Text(
                  text = "Cash Collector / Desk: ${subscription.cashCollectorName}",
                  fontFamily = AptosGrotesqueFontFamily,
                  fontSize = 12.sp,
                  color = PurpleUltraLight
                )
              }

              if (subscription.cashReceiptNote.isNotBlank()) {
                Text(
                  text = "Receipt / Note: ${subscription.cashReceiptNote}",
                  fontFamily = AptosGrotesqueFontFamily,
                  fontSize = 11.5.sp,
                  color = TextMuted
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Virtual Card & Privacy Shield
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(WhiteSurface)
            .border(1.dp, WhiteBorder, RoundedCornerShape(14.dp))
            .padding(16.dp)
            .testTag("detail_security_card")
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
              text = "CARD SECURITY & PRIVACY CONTROLS",
              fontFamily = PlusJakartaSansFontFamily,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = TextMuted,
              letterSpacing = 0.5.sp
            )

            // Virtual Card Control
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                  imageVector = if (subscription.isVirtualCardFrozen) Icons.Default.Lock else Icons.Default.LockOpen,
                  contentDescription = null,
                  tint = if (subscription.isVirtualCardFrozen) WarningRose else PurpleLight,
                  modifier = Modifier.size(18.dp)
                )
                Column {
                  Text(
                    text = "Virtual Card Status",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                  )
                  Text(
                    text = if (subscription.isVirtualCardFrozen) "Card is FROZEN. Recurring charges are blocked." else "Card is ACTIVE. Auto-pay transactions allowed.",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 10.5.sp,
                    color = TextMuted
                  )
                }
              }

              NeumorphicButton(
                onClick = { onToggleVirtualCard(subscription.id) },
                backgroundColor = if (subscription.isVirtualCardFrozen) WarningRoseBg else PurpleBg,
                testTag = "detail_freeze_card_button"
              ) {
                Text(
                  text = if (subscription.isVirtualCardFrozen) "Unfreeze" else "Freeze Card",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (subscription.isVirtualCardFrozen) WarningRose else PurpleLight
                )
              }
            }

            HorizontalDivider(color = WhiteBorderSubtle, thickness = 0.75.dp)

            // GDPR Data Erasure
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                  imageVector = Icons.Default.Shield,
                  contentDescription = null,
                  tint = if (subscription.gdprWipeRequested) SoftSage else TextMuted,
                  modifier = Modifier.size(18.dp)
                )
                Column {
                  Text(
                    text = "GDPR Data Wipe",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                  )
                  Text(
                    text = if (subscription.gdprWipeRequested) "Erasure demand dispatched to vendor DPO." else "Demand complete erasure of KYC & card profile.",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 10.5.sp,
                    color = TextMuted
                  )
                }
              }

              NeumorphicButton(
                onClick = { onToggleGdprWipe(subscription.id) },
                backgroundColor = if (subscription.gdprWipeRequested) SoftSageBg else WhiteSurface,
                testTag = "detail_gdpr_button"
              ) {
                Text(
                  text = if (subscription.gdprWipeRequested) "Wipe Sent" else "Request Wipe",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (subscription.gdprWipeRequested) SoftSage else TextSecondary
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Direct Actions Section
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          if (!isCancelled) {
            NeumorphicButton(
              onClick = { onCancelSub(subscription.id) },
              backgroundColor = SoftRoseBg,
              modifier = Modifier.weight(1f),
              testTag = "detail_cancel_button"
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = SoftRose,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "1-Click Cancel",
                fontFamily = PlusJakartaSansFontFamily,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = SoftRose
              )
            }

            NeumorphicButton(
              onClick = { onNegotiateSub(subscription) },
              backgroundColor = SoftAmberBg,
              modifier = Modifier.weight(1f),
              testTag = "detail_negotiate_button"
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Chat,
                contentDescription = null,
                tint = SoftAmber,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "AI Negotiate",
                fontFamily = PlusJakartaSansFontFamily,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = SoftAmber
              )
            }
          } else {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SoftSageBg)
                .border(1.dp, SoftSageBorder, RoundedCornerShape(10.dp))
                .padding(12.dp),
              contentAlignment = Alignment.Center
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = SoftSage,
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = "This subscription has been cancelled and recurring payments stopped.",
                  fontFamily = PlusJakartaSansFontFamily,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = SoftSage
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(32.dp))
      }
    }
  }
}

@Composable
private fun MetricItem(
  label: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier
  ) {
    Box(
      modifier = Modifier
        .size(30.dp)
        .clip(CircleShape)
        .background(color.copy(alpha = 0.12f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = Modifier.size(14.dp)
      )
    }
    Column {
      Text(
        text = label,
        fontFamily = PlusJakartaSansFontFamily,
        fontSize = 10.sp,
        color = TextMuted
      )
      Text(
        text = value,
        fontFamily = PlusJakartaSansFontFamily,
        fontSize = 12.5.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary
      )
    }
  }
}

@Composable
private fun DetailTopBar(
  title: String,
  onBackClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .background(WhiteBackground)
      .padding(horizontal = 8.dp, vertical = 6.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onBackClick,
        modifier = Modifier.testTag("detail_back_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back to subscriptions list",
          tint = TextPrimary
        )
      }
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = title,
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = TextPrimary,
        maxLines = 1
      )
    }
  }
}
