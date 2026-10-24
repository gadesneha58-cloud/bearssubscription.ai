package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.NeumorphicBottomBar
import com.example.ui.components.NeumorphicIconButton
import com.example.ui.components.TabNavigation
import com.example.ui.dialogs.BankSmsPipelineDialog
import com.example.ui.dialogs.CancellationLetterDialog
import com.example.ui.dialogs.DiscoverySheet
import com.example.ui.dialogs.ExtractionDialog
import com.example.ui.dialogs.NegotiationDialog
import com.example.ui.dialogs.ScreenshotAuditDialog
import com.example.ui.dialogs.SettingsDialog
import com.example.ui.dialogs.SupabaseDialog
import com.example.ui.dialogs.SupabaseCloudMessagingDialog
import com.example.data.SupabaseService
import com.example.ui.screens.ConsumerProScreen
import com.example.ui.screens.EnterpriseScreen
import com.example.ui.screens.OverviewScreen
import com.example.ui.screens.SubscriptionDetailScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.theme.SoftRose
import com.example.ui.theme.SoftSage
import com.example.ui.theme.SoftSageBg
import androidx.compose.ui.draw.shadow
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.SoftSageBorder
import com.example.ui.theme.SoftSky
import com.example.ui.theme.SubscriptionsAiTheme
import com.example.ui.theme.SupabaseBg
import com.example.ui.theme.SupabaseBorder
import com.example.ui.theme.SupabaseGreen
import com.example.ui.theme.TextMuted
import com.example.ui.screens.LoginScreen
import com.example.ui.dialogs.AddSubscriptionDialog
import com.example.viewmodel.UserRole
import com.example.ui.theme.SpaceGroteskFontFamily
import com.example.ui.theme.AptosGrotesqueFontFamily
import com.example.ui.theme.PurpleLight
import com.example.ui.theme.PurplePrimary
import androidx.compose.foundation.clickable
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.PurpleUltraLight
import com.example.ui.theme.PurpleBg
import com.example.ui.theme.PurpleBorder
import com.example.ui.theme.SkyAccent
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Business
import com.example.viewmodel.AppTab
import com.example.viewmodel.SubscriptionsViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: SubscriptionsViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      SubscriptionsAiTheme {
        SubscriptionsApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun SubscriptionsApp(viewModel: SubscriptionsViewModel) {
  val currentTab by viewModel.currentTab.collectAsState()
  val audit by viewModel.financialAudit.collectAsState()
  val subscriptions by viewModel.subscriptions.collectAsState()
  val enterpriseSeats by viewModel.enterpriseSeats.collectAsState()
  val auditLogs by viewModel.auditLogs.collectAsState()
  val monthlyBudgetGoal by viewModel.monthlyBudgetGoal.collectAsState()

  // Discovery state
  val isDiscovering by viewModel.isDiscovering.collectAsState()
  val discoveryStatusMessage by viewModel.discoveryStatusMessage.collectAsState()
  val discoveryReport by viewModel.discoveryReport.collectAsState()
  var showDiscoveryModal by remember { mutableStateOf(false) }

  // AI Negotiation state
  val activeNegotiatingSub by viewModel.activeNegotiatingSubscription.collectAsState()
  val negotiationMessages by viewModel.negotiationMessages.collectAsState()
  val isNegotiatingAiBusy by viewModel.isNegotiatingAiBusy.collectAsState()

  // AI Extraction state
  val extractionInputText by viewModel.extractionInputText.collectAsState()
  val extractedResult by viewModel.extractedResult.collectAsState()
  val isExtracting by viewModel.isExtracting.collectAsState()
  var showExtractionModal by remember { mutableStateOf(false) }

  // Supabase state
  var showSupabaseModal by remember { mutableStateOf(false) }
  var showCloudMessagingModal by remember { mutableStateOf(false) }
  val renewalAlerts by viewModel.renewalAlerts.collectAsState()
  val alertThresholdDays by viewModel.alertThresholdDays.collectAsState()
  val autoAlertsEnabled by viewModel.autoAlertsEnabled.collectAsState()
  val isDispatchingAlert by viewModel.isDispatchingAlert.collectAsState()
  val lastDispatchedAlert by viewModel.lastDispatchedAlert.collectAsState()

  val approachingSubsCount = remember(subscriptions, alertThresholdDays) {
    SupabaseService.getSubscriptionsApproachingRenewal(subscriptions, alertThresholdDays).size
  }

  // Cancellation Preview state
  val cancellationPreviewSub by viewModel.cancellationPreviewSub.collectAsState()

  // Settings state
  val apiKeyInput by viewModel.apiKeyInput.collectAsState()
  val selectedModel by viewModel.selectedModel.collectAsState()
  var showSettingsModal by remember { mutableStateOf(false) }

  // Screenshot Bill Audit State
  val isScreenshotAuditOpen by viewModel.isScreenshotAuditOpen.collectAsState()
  val lastAddedSub by viewModel.lastAddedSubscription.collectAsState()

  // Bank SMS & UPI AutoPay Mandate Pipeline State
  val isSmsPipelineOpen by viewModel.isSmsPipelineOpen.collectAsState()
  val bankSmsList by viewModel.bankSmsList.collectAsState()
  val isSmsScanning by viewModel.isSmsScanning.collectAsState()
  val selectedBankCode by viewModel.selectedBankCode.collectAsState()
  val selectedAutoPaySignal by viewModel.selectedAutoPaySignal.collectAsState()

  // Role & Login State
  val userRole by viewModel.userRole.collectAsState()
  val userName by viewModel.userName.collectAsState()
  var showAddSubscriptionModal by remember { mutableStateOf(false) }
  var addModalInitialCashMode by remember { mutableStateOf(false) }

  // READ_SMS Runtime Permission Flow
  val context = LocalContext.current
  var hasSmsPermission by remember {
    mutableStateOf(
      ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
    )
  }

  val smsPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    hasSmsPermission = isGranted
    if (isGranted) {
      viewModel.scanDeviceSms(context)
    } else {
      viewModel.loadSampleBankSms()
    }
  }

  if (userRole == null) {
    LoginScreen(
      onSelectRole = { role, name ->
        viewModel.selectRole(role, name)
      }
    )
    return
  }

  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = "subscription_list"
  ) {
    composable("subscription_list") {
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkCanvas,
        bottomBar = {
          NeumorphicBottomBar(
            userRole = userRole!!,
            currentTab = currentTab,
            onTabSelected = { viewModel.selectTab(it) },
            onOpenScreenshotAudit = { viewModel.openScreenshotAudit() },
            onOpenAlerts = { showCloudMessagingModal = true },
            alertsCount = approachingSubsCount
          )
        }
      ) { innerPadding ->
        Column(
          modifier = Modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .padding(innerPadding)
        ) {
          // Top Application Header (Obsidian Purple)
          TopHeader(
            realizedSavingsYtd = audit.realizedSavingsYtd,
            approachingRenewalsCount = approachingSubsCount,
            userRole = userRole!!,
            userName = userName,
            onSwitchRole = { viewModel.switchRole() },
            onLogout = { viewModel.logout() },
            onOpenAddSubscription = {
              addModalInitialCashMode = false
              showAddSubscriptionModal = true
            },
            onOpenCashSubscription = {
              addModalInitialCashMode = true
              showAddSubscriptionModal = true
            },
            onOpenDiscovery = { showDiscoveryModal = true },
            onOpenExtraction = { showExtractionModal = true },
            onOpenScreenshotAudit = { viewModel.openScreenshotAudit() },
            onOpenCloudMessaging = { showCloudMessagingModal = true },
            onOpenSmsPipeline = { viewModel.openSmsPipeline() },
            onOpenSettings = { showSettingsModal = true },
            onResetForJudgeDemo = {
              viewModel.resetAllDataForJudgeDemo(context)
              smsPermissionLauncher.launch(Manifest.permission.READ_SMS)
            },
            onExportAuditPdf = {
              com.example.engine.EnterprisePdfExporter.generateAndShareAuditPdf(
                context = context,
                companyName = userName.ifBlank { "Enterprise Org" },
                subscriptions = subscriptions,
                enterpriseSeats = enterpriseSeats,
                audit = audit
              )
            }
          )

          Spacer(modifier = Modifier.height(2.dp))

          // Tab Content View
          Crossfade(
            targetState = currentTab,
            animationSpec = tween(durationMillis = 150),
            modifier = Modifier.weight(1f),
            label = "tab_crossfade"
          ) { tab ->
            when (tab) {
              AppTab.OVERVIEW -> {
                OverviewScreen(
                  audit = audit,
                  subscriptions = subscriptions,
                  enterpriseSeats = enterpriseSeats,
                  auditLogs = auditLogs,
                  userRole = userRole!!,
                  onOpenDiscovery = { showDiscoveryModal = true },
                  onOpenExtraction = { showExtractionModal = true },
                  onOpenScreenshotAudit = { viewModel.openScreenshotAudit() },
                  onCancelSub = { viewModel.cancelSubscription(it) },
                  onNegotiateSub = { viewModel.startNegotiation(it) },
                  onReclaimSeat = { viewModel.reclaimEnterpriseSeats(it) },
                  onNavigateTab = { viewModel.selectTab(it) },
                  onToggleVirtualCard = { viewModel.toggleVirtualCard(it) },
                  onToggleGdprWipe = { viewModel.toggleGdprWipe(it) },
                  onOpenCloudMessaging = { showCloudMessagingModal = true },
                  onOpenSmsPipeline = { viewModel.openSmsPipeline() },
                  renewalAlerts = renewalAlerts,
                  monthlyBudgetGoal = monthlyBudgetGoal,
                  onUpdateBudgetGoal = { viewModel.updateMonthlyBudgetGoal(it) },
                  onSubscriptionClick = { sub ->
                    navController.navigate("subscription_detail/${sub.id}")
                  },
                  onOpenAddSubscription = {
                    addModalInitialCashMode = false
                    showAddSubscriptionModal = true
                  },
                  onOpenCashSubscription = {
                    addModalInitialCashMode = true
                    showAddSubscriptionModal = true
                  },
                  onAddDirectSubscription = { sub ->
                    viewModel.addSubscription(sub)
                  }
                )
              }

              AppTab.CONSUMER_PRO -> {
                ConsumerProScreen(
                  subscriptions = subscriptions,
                  onCancelSub = { viewModel.cancelSubscription(it) },
                  onNegotiateSub = { viewModel.startNegotiation(it) },
                  onToggleVirtualCard = { viewModel.toggleVirtualCard(it) },
                  onToggleGdprWipe = { viewModel.toggleGdprWipe(it) },
                  onOpenSmsPipeline = { viewModel.openSmsPipeline() },
                  onSubscriptionClick = { sub ->
                    navController.navigate("subscription_detail/${sub.id}")
                  }
                )
              }

              AppTab.ENTERPRISE -> {
                EnterpriseScreen(
                  enterpriseSeats = enterpriseSeats,
                  onReclaimSeat = { viewModel.reclaimEnterpriseSeats(it) },
                  onReclaimAllSeats = { viewModel.reclaimAllIdleSeats() },
                  onExportAuditPdf = {
                    com.example.engine.EnterprisePdfExporter.generateAndShareAuditPdf(
                      context = context,
                      companyName = userName.ifBlank { "Enterprise Org" },
                      subscriptions = subscriptions,
                      enterpriseSeats = enterpriseSeats,
                      audit = audit
                    )
                  }
                )
              }
            }
          }
        }
      }
    }

    composable(
      route = "subscription_detail/{subscriptionId}",
      arguments = listOf(navArgument("subscriptionId") { type = NavType.StringType })
    ) { backStackEntry ->
      val subscriptionId = backStackEntry.arguments?.getString("subscriptionId")
      val selectedSub = subscriptions.find { it.id == subscriptionId }

      SubscriptionDetailScreen(
        subscription = selectedSub,
        onBackClick = { navController.popBackStack() },
        onCancelSub = { viewModel.cancelSubscription(it) },
        onNegotiateSub = { viewModel.startNegotiation(it) },
        onToggleVirtualCard = { viewModel.toggleVirtualCard(it) },
        onToggleGdprWipe = { viewModel.toggleGdprWipe(it) }
      )
    }
  }

  // Interactive Dialogs
  if (showDiscoveryModal || isDiscovering) {
    DiscoverySheet(
      isDiscovering = isDiscovering,
      statusMessage = discoveryStatusMessage,
      report = discoveryReport,
      onTriggerScan = { viewModel.triggerDiscoveryScan() },
      onDismiss = {
        showDiscoveryModal = false
        viewModel.dismissDiscoveryReport()
      }
    )
  }

  if (activeNegotiatingSub != null) {
    NegotiationDialog(
      subscription = activeNegotiatingSub!!,
      messages = negotiationMessages,
      isAiBusy = isNegotiatingAiBusy,
      onSendMessage = { viewModel.sendNegotiationMessage(it) },
      onApplyPrice = { subId, price -> viewModel.applyNegotiatedPrice(subId, price) },
      onDismiss = { viewModel.closeNegotiation() }
    )
  }

  if (isScreenshotAuditOpen) {
    ScreenshotAuditDialog(
      onSaveSubscription = { viewModel.addAuditedSubscription(it) },
      onDismiss = { viewModel.dismissScreenshotAudit() }
    )
  }

  if (showExtractionModal) {
    ExtractionDialog(
      inputText = extractionInputText,
      extractedResult = extractedResult,
      isExtracting = isExtracting,
      onInputTextChange = { viewModel.updateExtractionInputText(it) },
      onRunExtraction = { viewModel.runExtraction() },
      onApplyExtractedAudit = {
        viewModel.applyExtractedAuditToActive()
        showExtractionModal = false
      },
      onDismiss = { showExtractionModal = false }
    )
  }

  if (showSupabaseModal) {
    SupabaseDialog(
      subscriptionsCount = subscriptions.size,
      onSyncSubscriptions = { viewModel.syncWithSupabase() },
      onPullSubscriptions = { viewModel.pullFromSupabase() },
      onAddMockSubscription = { name, cost, isZombie ->
        viewModel.addMockSubscription(
          name = name,
          cost = cost,
          classification = if (isZombie) com.example.model.SubscriptionClassification.ZOMBIE_SUBSCRIPTION else com.example.model.SubscriptionClassification.ACTIVE_UTILITY
        )
      },
      onOpenCloudMessaging = {
        showSupabaseModal = false
        showCloudMessagingModal = true
      },
      onDismiss = { showSupabaseModal = false }
    )
  }

  if (showCloudMessagingModal) {
    SupabaseCloudMessagingDialog(
      subscriptions = subscriptions,
      alerts = renewalAlerts,
      thresholdDays = alertThresholdDays,
      autoAlertsEnabled = autoAlertsEnabled,
      isDispatching = isDispatchingAlert,
      onSetThresholdDays = { viewModel.setAlertThresholdDays(it) },
      onToggleAutoAlerts = { viewModel.toggleAutoAlerts(it) },
      onTriggerCheckAndAlert = { ctx -> viewModel.triggerRenewalCheckAndAlert(ctx) },
      onSendSingleAlert = { sub, ctx -> viewModel.sendSingleSubscriptionRenewalAlert(sub, ctx) },
      onToggleVirtualCard = { viewModel.toggleVirtualCard(it) },
      onCancelSub = { viewModel.cancelSubscription(it) },
      onClearAlerts = { viewModel.clearRenewalAlerts() },
      onDismiss = { showCloudMessagingModal = false }
    )
  }

  if (isSmsPipelineOpen) {
    BankSmsPipelineDialog(
      smsList = bankSmsList,
      isScanning = isSmsScanning,
      hasSmsPermission = hasSmsPermission,
      selectedBankCode = selectedBankCode,
      selectedSignal = selectedAutoPaySignal,
      onSelectBank = { viewModel.setBankFilter(it) },
      onSelectSignal = { viewModel.setAutoPaySignalFilter(it) },
      onScanDeviceSms = {
        if (hasSmsPermission) {
          viewModel.scanDeviceSms(context)
        } else {
          smsPermissionLauncher.launch(Manifest.permission.READ_SMS)
        }
      },
      onLoadSampleFeed = { viewModel.loadSampleBankSms() },
      onParseCustomSms = { text, sender -> viewModel.parseAndAddCustomSms(text, sender) },
      onAddSmsToTracked = { viewModel.addParsedSmsToSubscriptions(it) },
      onImportAllToTracked = { viewModel.importAllParsedSmsToSubscriptions() },
      onDismiss = { viewModel.dismissSmsPipeline() }
    )
  }

  if (cancellationPreviewSub != null) {
    CancellationLetterDialog(
      subscription = cancellationPreviewSub!!,
      onDismiss = { viewModel.dismissCancellationPreview() },
      onRevokeMandateTier = { source, onStatus, onComplete ->
        viewModel.cancelSubscriptionWithEngine(
          context = context,
          subId = cancellationPreviewSub!!.id,
          preferredSource = source,
          onStatusUpdate = onStatus,
          onComplete = onComplete
        )
      }
    )
  }

  if (lastAddedSub != null) {
    com.example.ui.components.SubwaySurfersAddAnimationBanner(
      subscription = lastAddedSub!!,
      onDismiss = { viewModel.dismissAddAnimation() }
    )
  }

  if (showSettingsModal) {
    SettingsDialog(
      currentApiKey = apiKeyInput,
      currentModel = selectedModel,
      onSaveApiKey = { viewModel.updateApiKey(it) },
      onSelectModel = { viewModel.setModel(it) },
      onDismiss = { showSettingsModal = false }
    )
  }

  if (showAddSubscriptionModal) {
    AddSubscriptionDialog(
      onDismiss = { showAddSubscriptionModal = false },
      onAddSubscription = { sub ->
        viewModel.addSubscription(sub)
        showAddSubscriptionModal = false
      },
      initialCashMode = addModalInitialCashMode
    )
  }
}

@Composable
private fun TopHeader(
  realizedSavingsYtd: Double,
  approachingRenewalsCount: Int = 0,
  userRole: UserRole,
  userName: String = "",
  onSwitchRole: () -> Unit,
  onLogout: () -> Unit,
  onOpenAddSubscription: () -> Unit,
  onOpenCashSubscription: () -> Unit = onOpenAddSubscription,
  onOpenDiscovery: () -> Unit,
  onOpenExtraction: () -> Unit,
  onOpenScreenshotAudit: () -> Unit,
  onOpenCloudMessaging: () -> Unit = {},
  onOpenSmsPipeline: () -> Unit = {},
  onOpenSettings: () -> Unit,
  onResetForJudgeDemo: () -> Unit = {},
  onExportAuditPdf: () -> Unit = {}
) {
  var menuExpanded by remember { mutableStateOf(false) }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(com.example.ui.theme.DarkCanvas)
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Brand Logo & Role Pill
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(10.dp),
            spotColor = com.example.ui.theme.PurpleGlow,
            ambientColor = Color.Black
          )
          .clip(RoundedCornerShape(10.dp))
          .background(
            androidx.compose.ui.graphics.Brush.linearGradient(
              listOf(com.example.ui.theme.DarkSurfaceElevated, com.example.ui.theme.DarkSurfaceSunken)
            )
          )
          .border(1.dp, com.example.ui.theme.DarkBorder, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = "S",
            color = PurpleLight,
            fontSize = 16.sp,
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.Black
          )
          Text(
            text = "•",
            color = PurplePrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Subscriptions",
            color = TextPrimary,
            fontSize = 15.sp,
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.3).sp
          )
          Text(
            text = ".ai",
            color = PurpleLight,
            fontSize = 15.sp,
            fontFamily = AptosGrotesqueFontFamily,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.3).sp
          )
        }

        // Clickable Role Badge (Switch role on tap)
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(PurpleBg)
            .border(0.75.dp, PurpleBorder, RoundedCornerShape(6.dp))
            .clickable { onSwitchRole() }
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
          ) {
            Icon(
              imageVector = if (userRole == UserRole.ENTERPRISE) Icons.Default.Business else Icons.Default.Person,
              contentDescription = null,
              tint = if (userRole == UserRole.ENTERPRISE) SkyAccent else PurpleLight,
              modifier = Modifier.size(10.dp)
            )
            Text(
              text = if (userRole == UserRole.ENTERPRISE) "Enterprise" else "Personal",
              color = if (userRole == UserRole.ENTERPRISE) SkyAccent else PurpleLight,
              fontSize = 9.sp,
              fontFamily = AptosGrotesqueFontFamily,
              fontWeight = FontWeight.SemiBold
            )
            Icon(
              imageVector = Icons.Default.SwapHoriz,
              contentDescription = "Switch Role",
              tint = TextMuted,
              modifier = Modifier.size(9.dp)
            )
          }
        }
      }
    }

    // Right Action Buttons: + Add, Bell, More Menu
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // "🔄 Clear All & Re-Scan SMS (Judge Demo)" Quick Button
      NeumorphicIconButton(
        icon = Icons.Default.Refresh,
        onClick = onResetForJudgeDemo,
        tint = PurpleLight,
        size = 32.dp,
        iconSize = 16.dp,
        elevation = 3.dp,
        testTag = "btn_header_judge_reset"
      )

      // "+ Feed Data" Quick Button
      NeumorphicIconButton(
        icon = Icons.Default.Add,
        onClick = onOpenAddSubscription,
        tint = PurpleLight,
        size = 32.dp,
        iconSize = 16.dp,
        elevation = 3.dp,
        testTag = "btn_header_add_subscription"
      )

      // Renewal Alerts Bell Button with Badge
      Box {
        NeumorphicIconButton(
          icon = if (approachingRenewalsCount > 0) Icons.Default.NotificationsActive else Icons.Default.Notifications,
          onClick = onOpenCloudMessaging,
          tint = if (approachingRenewalsCount > 0) PurpleLight else TextMuted,
          size = 32.dp,
          iconSize = 16.dp,
          elevation = 3.dp,
          testTag = "btn_header_notifications"
        )

        if (approachingRenewalsCount > 0) {
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .size(13.dp)
              .clip(CircleShape)
              .background(SoftRose)
              .border(1.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "$approachingRenewalsCount",
              color = Color.White,
              fontSize = 7.5.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Minimalist Actions Menu ("•••")
      Box {
        NeumorphicIconButton(
          icon = Icons.Default.MoreVert,
          onClick = { menuExpanded = true },
          tint = TextMuted,
          size = 32.dp,
          iconSize = 16.dp,
          elevation = 3.dp,
          testTag = "btn_header_menu"
        )

        DropdownMenu(
          expanded = menuExpanded,
          onDismissRequest = { menuExpanded = false },
          modifier = Modifier
            .background(com.example.ui.theme.DarkSurfaceElevated)
            .border(1.dp, com.example.ui.theme.DarkBorder, RoundedCornerShape(12.dp))
        ) {
          DropdownMenuItem(
            text = { Text("🔄 Clear & Re-Scan SMS (Judge Demo)", fontSize = 12.sp, color = PurpleLight, fontWeight = FontWeight.Bold) },
            leadingIcon = {
              Icon(Icons.Default.Refresh, contentDescription = null, tint = PurpleLight, modifier = Modifier.size(15.dp))
            },
            onClick = {
              menuExpanded = false
              onResetForJudgeDemo()
            }
          )

          if (userRole == UserRole.ENTERPRISE) {
            DropdownMenuItem(
              text = { Text("📄 Export Executive Audit PDF", fontSize = 12.sp, color = SkyAccent, fontWeight = FontWeight.Bold) },
              leadingIcon = {
                Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = SkyAccent, modifier = Modifier.size(15.dp))
              },
              onClick = {
                menuExpanded = false
                onExportAuditPdf()
              }
            )
          }

          DropdownMenuItem(
            text = { Text("+ Add Subscription", fontSize = 12.sp, color = PurpleLight, fontWeight = FontWeight.Bold) },
            leadingIcon = {
              Icon(Icons.Default.Add, contentDescription = null, tint = PurpleLight, modifier = Modifier.size(15.dp))
            },
            onClick = {
              menuExpanded = false
              onOpenAddSubscription()
            }
          )

          DropdownMenuItem(
            text = { Text("💵 Add Cash & Gym Dues", fontSize = 12.sp, color = PurpleLight, fontWeight = FontWeight.Bold) },
            leadingIcon = {
              Icon(Icons.Default.Payments, contentDescription = null, tint = PurpleLight, modifier = Modifier.size(15.dp))
            },
            onClick = {
              menuExpanded = false
              onOpenCashSubscription()
            }
          )

          DropdownMenuItem(
            text = {
              Text(
                text = "Switch to ${if (userRole == UserRole.PERSONAL) "Enterprise Dashboard" else "Personal Dashboard"}",
                fontSize = 12.sp,
                color = SkyAccent,
                fontWeight = FontWeight.SemiBold
              )
            },
            leadingIcon = {
              Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = SkyAccent, modifier = Modifier.size(15.dp))
            },
            onClick = {
              menuExpanded = false
              onSwitchRole()
            }
          )

          DropdownMenuItem(
            text = { Text("Audit Bill Screenshot", fontSize = 12.sp, color = PurpleUltraLight, fontWeight = FontWeight.SemiBold) },
            leadingIcon = {
              Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = PurpleLight, modifier = Modifier.size(15.dp))
            },
            onClick = {
              menuExpanded = false
              onOpenScreenshotAudit()
            }
          )

          DropdownMenuItem(
            text = { Text("Renewal Date Alerts", fontSize = 12.sp, color = TextPrimary) },
            leadingIcon = {
              Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = PurpleLight, modifier = Modifier.size(15.dp))
            },
            onClick = {
              menuExpanded = false
              onOpenCloudMessaging()
            }
          )

          DropdownMenuItem(
            text = { Text("Scan Banking Feeds (Plaid)", fontSize = 12.sp, color = SkyAccent) },
            leadingIcon = {
              Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = SkyAccent, modifier = Modifier.size(15.dp))
            },
            onClick = {
              menuExpanded = false
              onOpenDiscovery()
            }
          )

          DropdownMenuItem(
            text = { Text("Extract Statement (AI JSON)", fontSize = 12.sp, color = TextPrimary) },
            leadingIcon = {
              Icon(Icons.Default.DataArray, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(15.dp))
            },
            onClick = {
              menuExpanded = false
              onOpenExtraction()
            }
          )

          DropdownMenuItem(
            text = { Text("UPI AutoPay & SMS Mandates", fontSize = 12.sp, color = SkyAccent, fontWeight = FontWeight.SemiBold) },
            leadingIcon = {
              Icon(Icons.Default.Sms, contentDescription = null, tint = SkyAccent, modifier = Modifier.size(15.dp))
            },
            onClick = {
              menuExpanded = false
              onOpenSmsPipeline()
            }
          )

          DropdownMenuItem(
            text = { Text("Settings & Engine Config", fontSize = 12.sp, color = TextSecondary) },
            leadingIcon = {
              Icon(Icons.Default.Settings, contentDescription = null, tint = TextMuted, modifier = Modifier.size(15.dp))
            },
            onClick = {
              menuExpanded = false
              onOpenSettings()
            }
          )

          DropdownMenuItem(
            text = { Text("Log Out / Change Role", fontSize = 12.sp, color = SoftRose) },
            leadingIcon = {
              Icon(Icons.Default.Person, contentDescription = null, tint = SoftRose, modifier = Modifier.size(15.dp))
            },
            onClick = {
              menuExpanded = false
              onLogout()
            }
          )
        }
      }
    }
  }
}
