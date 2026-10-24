package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.engine.MandateRevocationEngine
import com.example.engine.MandateSource
import com.example.model.BillingCadence
import com.example.model.Subscription
import com.example.ui.components.BrandIcon
import com.example.ui.components.NeumorphicPrimaryButton
import com.example.ui.theme.AptosGrotesqueFontFamily
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceSunken
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.SoftAmber
import com.example.ui.theme.SoftRose
import com.example.ui.theme.SoftSage
import com.example.ui.theme.SoftSageBg
import com.example.ui.theme.SoftSageBorder
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteBackground
import com.example.ui.theme.WhiteBorder
import com.example.ui.theme.WhiteSurface
import kotlinx.coroutines.launch

/**
 * Enhanced Cancellation Letter & 3-Tier Mandate Revocation Dialog.
 * Integrates MandateRevocationEngine supporting:
 * Tier 1: Subscriptions.AI Proxy Gateway API
 * Tier 2: RBI SI-Hub Bank Portal (sihub.in/managesi/)
 * Tier 3: UPI App Handoff (GPay / PhonePe)
 */
@Composable
fun CancellationLetterDialog(
  subscription: Subscription,
  onDismiss: () -> Unit,
  onRevokeMandateTier: (MandateSource, (String) -> Unit, (Boolean, String) -> Unit) -> Unit = { _, _, _ -> }
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val annualSaved = if (subscription.billingCadence == BillingCadence.MONTHLY) {
    subscription.currentCost * 12.0
  } else subscription.currentCost

  val mandate = remember(subscription) {
    MandateRevocationEngine.createMandateFromSubscription(subscription)
  }

  var liveRevocationStatus by remember { mutableStateOf("Mandate token queued for revocation.") }
  var isRevoking by remember { mutableStateOf(false) }
  var revocationCompleteMsg by remember { mutableStateOf<String?>(null) }

  val legalLetterText = """
    NOTICE OF IMMEDIATE MANDATE & AUTHORIZATION REVOCATION
    Date: October 24, 2026
    To: ${subscription.merchantName} Billing Desk & Sponsor Bank
    Subject: Formal Mandate Revocation Notice (UMN: ${mandate.umnOrTokenId})
    
    Pursuant to the FTC 'Click-to-Cancel' Rule (16 CFR Part 425) and RBI Master Direction on Recurring Transactions (RBI/2021-22/100), all standing instruction billing authorizations for this account are revoked effective immediately.
    
    Mandate UMN: ${mandate.umnOrTokenId}
    Rail Tier: ${mandate.source.label}
    Annual Savings Protected: ₹${"%.2f".format(annualSaved)}/yr
  """.trimIndent()

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .clip(RoundedCornerShape(22.dp))
        .background(DarkCanvas)
        .border(1.2.dp, Color(0xFF332654), RoundedCornerShape(22.dp)),
      color = DarkCanvas
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        // Dialog Top Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            BrandIcon(
              name = subscription.merchantName,
              size = 36.dp,
              iconSize = 20.dp
            )
            Column {
              Text(
                text = "Revoke Mandate: ${subscription.merchantName}",
                color = TextPrimary,
                fontSize = 15.sp,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = FontWeight.ExtraBold
              )
              Text(
                text = "3-Tier RBI Mandate Kill-Switch Active",
                color = PurpleLight,
                fontSize = 11.sp,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = FontWeight.Medium
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Success / Status Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurfaceSunken)
            .border(1.dp, Color(0xFF332756), RoundedCornerShape(14.dp))
            .padding(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = SoftSage,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Mandate Revocation Active",
                color = TextPrimary,
                fontSize = 13.sp,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = FontWeight.ExtraBold
              )
              Text(
                text = revocationCompleteMsg ?: liveRevocationStatus,
                color = if (revocationCompleteMsg != null) SoftSage else SkyAccent,
                fontSize = 11.sp,
                fontFamily = AptosGrotesqueFontFamily,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3-Tier Revocation Trigger Options
        Text(
          text = "3-TIER MANDATE KILL-SWITCH RAILS",
          fontFamily = AptosGrotesqueFontFamily,
          fontSize = 10.5.sp,
          fontWeight = FontWeight.Bold,
          color = TextMuted,
          letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tier 1: Proxy Gateway
        MandateRailActionTile(
          title = "Tier 1: Instant Gateway API Kill-Switch",
          subtitle = "Revokes UMN Token [${mandate.umnOrTokenId}] on Subscriptions.AI Proxy Rail",
          icon = Icons.Default.Shield,
          accentColor = PurpleLight,
          badgeText = "INSTANT API",
          onClick = {
            isRevoking = true
            scope.launch {
              MandateRevocationEngine(context).cancelSubscription(
                mandate = mandate.copy(source = MandateSource.SUBSCRIPTIONS_AI_PROXY_GATEWAY),
                onStatusUpdate = { liveRevocationStatus = it },
                onComplete = { success, msg ->
                  isRevoking = false
                  revocationCompleteMsg = msg
                }
              )
            }
          }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tier 2: RBI SI-Hub Bank Portal (sihub.in/managesi)
        MandateRailActionTile(
          title = "Tier 2: RBI SI-Hub Portal (sihub.in/managesi)",
          subtitle = "Routes directly to bank SI-Hub portal for card lookup & 6-digit OTP confirmation",
          icon = Icons.Default.Language,
          accentColor = SkyAccent,
          badgeText = "BANK OTP",
          onClick = {
            isRevoking = true
            scope.launch {
              MandateRevocationEngine(context).cancelSubscription(
                mandate = mandate.copy(source = MandateSource.BANK_CARD_SIHUB),
                onStatusUpdate = { liveRevocationStatus = it },
                onComplete = { success, msg ->
                  isRevoking = false
                  revocationCompleteMsg = msg
                }
              )
            }
          }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tier 3: UPI App Handoff
        MandateRailActionTile(
          title = "Tier 3: Launch UPI Mandate Manager",
          subtitle = "Opens GPay / PhonePe / Paytm deep mandate manager to confirm UPI PIN revocation",
          icon = Icons.Default.Smartphone,
          accentColor = SoftAmber,
          badgeText = "UPI HANDOFF",
          onClick = {
            isRevoking = true
            scope.launch {
              MandateRevocationEngine(context).cancelSubscription(
                mandate = mandate.copy(source = MandateSource.UPI_EXTERNAL_APP),
                onStatusUpdate = { liveRevocationStatus = it },
                onComplete = { success, msg ->
                  isRevoking = false
                  revocationCompleteMsg = msg
                }
              )
            }
          }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Legal Notice Preview Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurfaceSunken)
            .border(1.dp, Color(0xFF281F42), RoundedCornerShape(10.dp))
            .padding(10.dp)
        ) {
          Text(
            text = legalLetterText,
            color = TextSecondary,
            fontSize = 9.5.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 14.sp
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        NeumorphicPrimaryButton(
          onClick = onDismiss,
          primaryColor = PurplePrimary,
          contentColor = Color.White,
          shape = RoundedCornerShape(12.dp),
          elevation = 4.dp,
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
          testTag = "btn_close_cancellation_letter"
        ) {
          Text(
            text = "Done & Return to Dashboard",
            fontSize = 13.sp,
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
          )
        }
      }
    }
  }
}

@Composable
private fun MandateRailActionTile(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  accentColor: Color,
  badgeText: String,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(DarkSurfaceSunken)
      .border(1.dp, Color(0xFF2E234A), RoundedCornerShape(12.dp))
      .clickable(onClick = onClick)
      .padding(horizontal = 12.dp, vertical = 10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF22183A))
            .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(16.dp)
          )
        }

        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = title,
              fontFamily = AptosGrotesqueFontFamily,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 12.5.sp,
              color = TextPrimary
            )

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(accentColor.copy(alpha = 0.2f))
                .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
              Text(
                text = badgeText,
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 8.5.sp,
                color = accentColor
              )
            }
          }

          Text(
            text = subtitle,
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 10.5.sp,
            color = TextMuted,
            maxLines = 1
          )
        }
      }

      Icon(
        imageVector = Icons.Default.OpenInNew,
        contentDescription = "Execute Mandate Rail",
        tint = accentColor,
        modifier = Modifier.size(16.dp)
      )
    }
  }
}
