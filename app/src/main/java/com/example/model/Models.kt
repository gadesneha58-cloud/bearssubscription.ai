package com.example.model

enum class BillingCadence {
  MONTHLY,
  ANNUAL
}

enum class SubscriptionClassification {
  ACTIVE_UTILITY,
  ZOMBIE_SUBSCRIPTION,
  IDLE_SEAT
}

enum class RecommendedAction {
  KEEP,
  `1_CLICK_CANCEL`,
  AI_NEGOTIATE
}

enum class SubscriptionStatus {
  ACTIVE,
  CANCELLING_OPTIMISTIC,
  CANCELLED,
  NEGOTIATING,
  NEGOTIATED_SAVED
}

data class Subscription(
  val id: String,
  val merchantName: String,
  val currentCost: Double,
  val billingCadence: BillingCadence,
  val classification: SubscriptionClassification,
  val recommendedAction: RecommendedAction,
  val targetNegotiatedPrice: Double,
  val negotiationLeverage: String? = null,
  val category: String,
  val darkPatternWarning: String? = null,
  val daysSinceLastUse: Int = 0,
  val renewalDate: String = "In 12 days",
  val startDate: String = "Oct 12, 2026",
  val chatbotUrl: String? = null,
  val status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
  val isEnterprise: Boolean = false,
  val notes: String = "",
  val isVirtualCardFrozen: Boolean = false,
  val gdprWipeRequested: Boolean = false,
  val isLegacyVoiceNegotiating: Boolean = false,
  val hasRenewalAlertSent: Boolean = false,
  val isOfflineCash: Boolean = false,
  val paymentMethod: String = "Digital", // "Cash", "UPI Direct", "Cheque / Slip", "Digital"
  val cashCollectorName: String = "",
  val cashReceiptNote: String = ""
)

data class OfflineSubscriptionPreset(
  val id: String,
  val name: String,
  val defaultCost: Double,
  val category: String,
  val defaultCadence: BillingCadence = BillingCadence.MONTHLY,
  val defaultRenewalNotice: String = "1st of month",
  val collectorHint: String,
  val iconEmoji: String,
  val receiptTip: String,
  val tag: String = "CASH DUES"
)

object OfflinePresetCatalog {
  val commonPresets = listOf(
    OfflineSubscriptionPreset(
      id = "preset_gym",
      name = "Gym Membership",
      defaultCost = 1500.0,
      category = "Fitness",
      collectorHint = "Front Desk / Gym Trainer",
      iconEmoji = "🏋️",
      receiptTip = "Collect membership receipt & check locker card",
      tag = "POPULAR"
    ),
    OfflineSubscriptionPreset(
      id = "preset_crossfit",
      name = "Cult.fit / CrossFit Studio",
      defaultCost = 2400.0,
      category = "Fitness",
      collectorHint = "Studio Manager / Biometric Desk",
      iconEmoji = "⚡",
      receiptTip = "Biometric renewal on 1st of month",
      tag = "FITNESS"
    ),
    OfflineSubscriptionPreset(
      id = "preset_society",
      name = "Society Maintenance Fee",
      defaultCost = 3500.0,
      category = "Housing",
      collectorHint = "Society Office / RWA Secretary",
      iconEmoji = "🏢",
      receiptTip = "Demand signed maintenance slip & stamped voucher",
      tag = "HOUSING"
    ),
    OfflineSubscriptionPreset(
      id = "preset_maid",
      name = "House Maid / Cook Salary",
      defaultCost = 4500.0,
      category = "Domestic",
      collectorHint = "Handed in cash 1st week of month",
      iconEmoji = "🍳",
      receiptTip = "Log monthly leaves in domestic attendance diary",
      tag = "DOMESTIC"
    ),
    OfflineSubscriptionPreset(
      id = "preset_milk",
      name = "Daily Milk Delivery",
      defaultCost = 1800.0,
      category = "Essentials",
      collectorHint = "Morning Milkman / Dairy Vendor",
      iconEmoji = "🥛",
      receiptTip = "Verify monthly bottle delivery card tally",
      tag = "DAILY"
    ),
    OfflineSubscriptionPreset(
      id = "preset_water",
      name = "Drinking 20L Water Cans",
      defaultCost = 800.0,
      category = "Utilities",
      collectorHint = "Water Delivery Agency",
      iconEmoji = "💧",
      receiptTip = "Pay against bottle delivery token slips",
      tag = "UTILITY"
    ),
    OfflineSubscriptionPreset(
      id = "preset_newspaper",
      name = "Newspaper & Magazines",
      defaultCost = 350.0,
      category = "Essentials",
      collectorHint = "Newspaper Distributor Boy",
      iconEmoji = "📰",
      receiptTip = "Collect printed receipt slip on 1st Sunday",
      tag = "DAILY"
    ),
    OfflineSubscriptionPreset(
      id = "preset_yoga",
      name = "Yoga & Pilates Batch",
      defaultCost = 2000.0,
      category = "Wellness",
      collectorHint = "Yoga Instructor / Shala Desk",
      iconEmoji = "🧘",
      receiptTip = "Monthly batch slot renewal fee",
      tag = "WELLNESS"
    ),
    OfflineSubscriptionPreset(
      id = "preset_tuition",
      name = "Tutors & Coaching Classes",
      defaultCost = 3500.0,
      category = "Education",
      collectorHint = "Teacher / Tuition Academy",
      iconEmoji = "📚",
      receiptTip = "Fee card signed upon payment",
      tag = "EDUCATION"
    ),
    OfflineSubscriptionPreset(
      id = "preset_carwash",
      name = "Society Car Cleaner",
      defaultCost = 600.0,
      category = "Automotive",
      collectorHint = "Parking Cleaner / Security",
      iconEmoji = "🚗",
      receiptTip = "Monthly wash log sign-off",
      tag = "VEHICLE"
    ),
    OfflineSubscriptionPreset(
      id = "preset_cable",
      name = "Local Cable TV / DTH",
      defaultCost = 400.0,
      category = "Entertainment",
      collectorHint = "Cable Operator Collection Agent",
      iconEmoji = "📺",
      receiptTip = "Collect physical stamped receipt paper",
      tag = "LOCAL"
    ),
    OfflineSubscriptionPreset(
      id = "preset_laundry",
      name = "Dhobi / Ironing Service",
      defaultCost = 900.0,
      category = "Domestic",
      collectorHint = "Local Ironing Vendor",
      iconEmoji = "👔",
      receiptTip = "Cross-check piece tally notebook",
      tag = "SERVICE"
    )
  )
}

data class ScreenshotAuditResult(
  val id: String,
  val merchantName: String,
  val startDate: String,
  val expiryDate: String,
  val currentCost: Double,
  val billingCadence: BillingCadence = BillingCadence.MONTHLY,
  val planName: String,
  val auditStatus: String = "Audited Successfully",
  val suggestedChatbotScript: String = "",
  val chatbotUrl: String = ""
)

data class EnterpriseSeat(
  val id: String,
  val appName: String,
  val iconKey: String,
  val totalSeats: Int,
  val activeSeats: Int,
  val idleSeats: Int, // SSO last login > 30 days
  val costPerSeatMonthly: Double,
  val ssoProvider: String = "Okta SSO",
  val isReclaimed: Boolean = false,
  val contractRenewalDate: String = "Nov 2026",
  val complianceTier: String = "SOC2 / HIPAA",
  val alternativeVendor: String? = null
) {
  val monthlyWaste: Double
    get() = if (isReclaimed) 0.0 else idleSeats * costPerSeatMonthly

  val annualWaste: Double
    get() = monthlyWaste * 12.0
}

data class Transaction(
  val id: String,
  val date: String,
  val merchant: String,
  val amount: Double,
  val category: String,
  val accountName: String,
  val isRecurring: Boolean = true,
  val cadenceDays: Int = 30
)

data class FinancialAudit(
  val totalMonthlySpend: Double,
  val identifiedWaste: Double,
  val wastePercentage: Double,
  val realizedSavingsYtd: Double,
  val activeSubscriptionsCount: Int,
  val zombieCount: Int,
  val idleSeatsCount: Int
)

data class DetectedSubscriptionJson(
  val merchant_name: String,
  val current_cost: Double,
  val billing_cadence: String,
  val classification: String,
  val recommended_action: String,
  val target_negotiated_price: Double,
  val negotiation_leverage: String?
)

data class AuditJsonOutput(
  val financial_audit: AuditSummaryJson,
  val detected_subscriptions: List<DetectedSubscriptionJson>
)

data class AuditSummaryJson(
  val total_monthly_spend: Double,
  val identified_waste: Double,
  val waste_percentage: Double
)

data class NegotiationMessage(
  val sender: String, // "AI Architect", "User", "Merchant Rep"
  val text: String,
  val timestamp: String,
  val isLeverageTactic: Boolean = false
)

data class RenewalAlert(
  val id: String,
  val subscriptionId: String,
  val merchantName: String,
  val currentCost: Double,
  val billingCadence: BillingCadence,
  val renewalDate: String,
  val daysRemaining: Int,
  val alertTitle: String,
  val alertBody: String,
  val timestamp: String,
  val channel: String = "Supabase Cloud Messaging (SCM)",
  val isDelivered: Boolean = true,
  val priority: String = "HIGH", // HIGH, CRITICAL, NORMAL
  val suggestedAction: String = "FREEZE_OR_CANCEL"
)

enum class SupabaseSyncStatus {
  CONNECTED,
  SYNCED,
  SYNCING,
  ERROR,
  DEMO_MODE
}

data class SupabaseUser(
  val id: String,
  val email: String,
  val role: String = "authenticated",
  val isAnonymous: Boolean = false,
  val lastSignIn: String = "Today, 10:45 AM",
  val accessToken: String? = null,
  val refreshToken: String? = null
)

data class SupabaseTableStat(
  val tableName: String,
  val rowCount: Int,
  val status: String = "SYNCED",
  val lastSynced: String = "Just now"
)


