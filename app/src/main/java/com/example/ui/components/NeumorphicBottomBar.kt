package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AptosGrotesqueFontFamily
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderSubtle
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.WarningRose
import com.example.viewmodel.AppTab
import com.example.viewmodel.UserRole

/**
 * Obsidian & Luxury Purple Neumorphic Bottom Navigation Bar.
 * Features an elevated center floating button and purple active indicators (Zero Green).
 */
@Composable
fun NeumorphicBottomBar(
  userRole: UserRole,
  currentTab: AppTab,
  onTabSelected: (AppTab) -> Unit,
  onOpenScreenshotAudit: () -> Unit,
  onOpenAlerts: () -> Unit,
  alertsCount: Int = 0,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 8.dp)
  ) {
    // Background Navigation Bar with subtle dual shadow
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(
          elevation = 10.dp,
          shape = RoundedCornerShape(24.dp),
          spotColor = Color(0xFF040307),
          ambientColor = Color(0xFF040307)
        )
        .clip(RoundedCornerShape(24.dp))
        .background(
          Brush.verticalGradient(
            listOf(DarkSurfaceElevated, DarkSurface)
          )
        )
        .border(
          width = 1.2.dp,
          brush = Brush.verticalGradient(
            listOf(Color(0xFF3B2E5C), Color(0xFF161226))
          ),
          shape = RoundedCornerShape(24.dp)
        )
        .padding(horizontal = 6.dp, vertical = 6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // 1. Overview (Dashboard & Spend Breakdown)
        BottomBarTabItem(
          label = "Overview",
          icon = Icons.Default.GridView,
          isSelected = currentTab == AppTab.OVERVIEW,
          onClick = { onTabSelected(AppTab.OVERVIEW) },
          testTag = "nav_tab_overview",
          modifier = Modifier.weight(1f)
        )

        // 2. AutoPay Mandates & 1-Click Cancel Vault
        BottomBarTabItem(
          label = "Mandates",
          icon = Icons.Default.Shield,
          isSelected = currentTab == AppTab.CONSUMER_PRO,
          onClick = { onTabSelected(AppTab.CONSUMER_PRO) },
          testTag = "nav_tab_consumer",
          modifier = Modifier.weight(1f)
        )

        // 3. Enterprise SaaS Stack & Reclaimer (Only shown in Enterprise Role)
        if (userRole == UserRole.ENTERPRISE) {
          BottomBarTabItem(
            label = "Enterprise",
            icon = Icons.Default.Business,
            isSelected = currentTab == AppTab.ENTERPRISE,
            onClick = { onTabSelected(AppTab.ENTERPRISE) },
            testTag = "nav_tab_enterprise",
            modifier = Modifier.weight(1f)
          )
        }

        // 4. Renewal Alerts
        BottomBarTabItem(
          label = "Alerts",
          icon = Icons.Default.NotificationsActive,
          isSelected = false,
          onClick = onOpenAlerts,
          badgeCount = alertsCount,
          testTag = "nav_tab_alerts",
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
private fun BottomBarTabItem(
  label: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  badgeCount: Int = 0,
  testTag: String = ""
) {
  val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val iconColor by animateColorAsState(
    targetValue = if (isSelected) PurpleLight else TextMuted,
    animationSpec = tween(150),
    label = "tab_color"
  )

  Column(
    modifier = modifier
      .clip(RoundedCornerShape(14.dp))
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = {
          haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
          onClick()
        }
      )
      .padding(vertical = 6.dp)
      .then(if (testTag.isNotBlank()) Modifier.testTag(testTag) else Modifier),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(contentAlignment = Alignment.TopEnd) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = iconColor,
        modifier = Modifier.size(20.dp)
      )

      if (badgeCount > 0) {
        Box(
          modifier = Modifier
            .offset(x = 6.dp, y = (-4).dp)
            .size(14.dp)
            .clip(CircleShape)
            .background(WarningRose),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (badgeCount > 9) "9+" else badgeCount.toString(),
            color = Color.White,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(2.dp))

    Text(
      text = label,
      color = if (isSelected) Color.White else TextMuted,
      fontSize = 10.5.sp,
      fontFamily = AptosGrotesqueFontFamily,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
    )

    if (isSelected) {
      Spacer(modifier = Modifier.height(2.dp))
      Box(
        modifier = Modifier
          .size(width = 12.dp, height = 2.5.dp)
          .clip(CircleShape)
          .background(PurpleLight)
      )
    }
  }
}
