package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sms
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AutoPaySignal
import com.example.model.BankCode
import com.example.model.ParsedBankSms
import com.example.ui.components.BrandIcon
import com.example.ui.components.FintechMoneyText
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicPrimaryButton
import com.example.ui.theme.AptosGrotesqueFontFamily
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.DarkSurfaceSunken
import com.example.ui.theme.PurpleBg
import com.example.ui.theme.PurpleBorder
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.SkyAccent
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningRose
import com.example.ui.theme.WarningRoseBg

/**
 * Minimal, Professional Neumorphic UPI Mandates & Bank SMS Dialog.
 * Eliminates visual clutter while providing 1-tap scanning and tracking.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankSmsPipelineDialog(
  smsList: List<ParsedBankSms>,
  isScanning: Boolean,
  hasSmsPermission: Boolean = false,
  selectedBankCode: BankCode?,
  selectedSignal: AutoPaySignal?,
  onSelectBank: (BankCode?) -> Unit,
  onSelectSignal: (AutoPaySignal?) -> Unit,
  onScanDeviceSms: () -> Unit,
  onLoadSampleFeed: () -> Unit,
  onParseCustomSms: (String, String) -> Unit,
  onAddSmsToTracked: (ParsedBankSms) -> Unit,
  onImportAllToTracked: () -> Unit,
  onDismiss: () -> Unit
) {
  var isPasteExpanded by remember { mutableStateOf(false) }
  var pasteText by remember { mutableStateOf("") }

  BasicAlertDialog(onDismissRequest = onDismiss) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(24.dp))
        .background(DarkSurfaceElevated)
        .border(1.2.dp, DarkBorder, RoundedCornerShape(24.dp))
        .padding(20.dp)
        .testTag("upi_mandate_dialog")
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        // Minimal Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF261D42))
                .border(1.dp, Color(0xFF4C3682), RoundedCornerShape(12.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Sms,
                contentDescription = null,
                tint = PurpleLight,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "UPI AutoPay Mandates",
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary
              )
              Text(
                text = "Scan bank SMS to track recurring debits",
                fontFamily = AptosGrotesqueFontFamily,
                fontSize = 12.sp,
                color = TextSecondary
              )
            }
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

        Spacer(modifier = Modifier.height(16.dp))

        // Scanner Control Card (Minimal Neumorphic)
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurfaceSunken)
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (hasSmsPermission) PurpleLight else SkyAccent)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (hasSmsPermission) "INBOX ACCESS ACTIVE" else "AUTO-SCAN READY",
                  fontFamily = AptosGrotesqueFontFamily,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (hasSmsPermission) PurpleLight else SkyAccent,
                  letterSpacing = 0.5.sp
                )
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = if (hasSmsPermission) "Directly reads bank notifications" else "Scan device SMS inbox",
                fontFamily = AptosGrotesqueFontFamily,
                fontSize = 12.sp,
                color = TextSecondary
              )
            }

            NeumorphicPrimaryButton(
              onClick = onScanDeviceSms,
              testTag = "btn_scan_device_sms"
            ) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (isScanning) "Scanning..." else "Scan Inbox",
                fontFamily = AptosGrotesqueFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Mandates List
        Text(
          text = "DETECTED MANDATES (${smsList.size})",
          fontFamily = AptosGrotesqueFontFamily,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = PurpleLight,
          letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (smsList.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(DarkSurfaceSunken)
              .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
              .padding(24.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "No bank mandates found yet",
                fontFamily = SpaceGroteskFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Tap 'Scan Inbox' to read your device SMS or paste a bank debit SMS.",
                fontFamily = AptosGrotesqueFontFamily,
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
              Spacer(modifier = Modifier.height(12.dp))
              NeumorphicButton(
                onClick = { isPasteExpanded = !isPasteExpanded }
              ) {
                Text(
                  text = "Paste SMS Text ➔",
                  fontFamily = AptosGrotesqueFontFamily,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = PurpleLight
                )
              }
            }
          }
        } else {
          LazyColumn(
            modifier = Modifier
              .fillMaxWidth()
              .heightIn(max = 280.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(smsList, key = { it.id }) { sms ->
              MandateMinimalItem(
                sms = sms,
                onAddTracked = { onAddSmsToTracked(sms) }
              )
            }
          }
        }

        // Paste Custom SMS Collapsible section
        AnimatedVisibility(visible = isPasteExpanded) {
          Column(modifier = Modifier.padding(top = 12.dp)) {
            OutlinedTextField(
              value = pasteText,
              onValueChange = { pasteText = it },
              placeholder = { Text("Paste bank debit/mandate SMS text...", color = TextMuted, fontSize = 12.sp) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurfaceSunken,
                unfocusedContainerColor = DarkSurfaceSunken,
                focusedBorderColor = PurplePrimary,
                unfocusedBorderColor = DarkBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
              ),
              maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            NeumorphicPrimaryButton(
              onClick = {
                if (pasteText.isNotBlank()) {
                  onParseCustomSms(pasteText, "AD-HDFCBK")
                  pasteText = ""
                  isPasteExpanded = false
                }
              },
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("Parse & Add Mandate ➔", fontFamily = AptosGrotesqueFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bottom Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (isPasteExpanded) "Hide Paste" else "Paste SMS Text",
            fontFamily = AptosGrotesqueFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = PurpleLight,
            modifier = Modifier
              .clickable { isPasteExpanded = !isPasteExpanded }
              .padding(4.dp)
          )

          if (smsList.any { !it.isAddedToTracked }) {
            NeumorphicButton(
              onClick = onImportAllToTracked,
              testTag = "btn_import_all_mandates"
            ) {
              Text(
                text = "Track All (${smsList.count { !it.isAddedToTracked }}) ➔",
                fontFamily = AptosGrotesqueFontFamily,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleLight
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun MandateMinimalItem(
  sms: ParsedBankSms,
  onAddTracked: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(DarkSurfaceSunken)
      .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
      .padding(12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        BrandIcon(
          name = sms.merchant,
          size = 36.dp
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Text(
            text = sms.merchant,
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = TextPrimary
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = sms.bankCode.bankName,
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 11.sp,
              color = TextMuted
            )
            Text(text = "•", color = TextMuted, fontSize = 10.sp)
            Text(
              text = sms.autopaySignal.name,
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = when (sms.autopaySignal) {
                AutoPaySignal.REVOKED -> WarningRose
                AutoPaySignal.SCHEDULED -> SkyAccent
                else -> PurpleLight
              }
            )
          }
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        FintechMoneyText(
          amount = sms.amount,
          fontSize = 16.sp,
          color = TextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (sms.isAddedToTracked) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = PurpleLight,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "Tracked",
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = PurpleLight
            )
          }
        } else {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF261D42))
              .border(0.8.dp, PurplePrimary, RoundedCornerShape(8.dp))
              .clickable { onAddTracked() }
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = "+ Track",
              fontFamily = AptosGrotesqueFontFamily,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = PurpleLight
            )
          }
        }
      }
    }
  }
}
