package com.example.data

import com.example.model.BillingCadence
import com.example.model.EnterpriseSeat
import com.example.model.RecommendedAction
import com.example.model.RenewalAlert
import com.example.model.Subscription
import com.example.model.SubscriptionClassification
import com.example.model.SubscriptionStatus
import com.example.model.Transaction

object SampleData {

  val initialSubscriptions: List<Subscription> = listOf(
    Subscription(
      id = "sub_jio_fiber",
      merchantName = "Jio Fiber & Mobile",
      currentCost = 999.0,
      billingCadence = BillingCadence.MONTHLY,
      classification = SubscriptionClassification.ACTIVE_UTILITY,
      recommendedAction = RecommendedAction.KEEP,
      targetNegotiatedPrice = 799.0,
      negotiationLeverage = "Jio Fiber Postpaid plan rate optimization",
      category = "Telecom & Broadband",
      startDate = "Sep 28, 2026",
      renewalDate = "Oct 28, 2026",
      chatbotUrl = "https://wa.me/917000770007",
      status = SubscriptionStatus.ACTIVE,
      isVirtualCardFrozen = false,
      gdprWipeRequested = false,
      notes = "AutoPay via HDFC Bank"
    ),
    Subscription(
      id = "sub_netflix_4k",
      merchantName = "Netflix Premium",
      currentCost = 649.0,
      billingCadence = BillingCadence.MONTHLY,
      classification = SubscriptionClassification.ACTIVE_UTILITY,
      recommendedAction = RecommendedAction.AI_NEGOTIATE,
      targetNegotiatedPrice = 499.0,
      negotiationLeverage = "Benchmark against standard 2-screen tier",
      category = "Entertainment",
      startDate = "Sep 13, 2026",
      renewalDate = "In 3 days",
      chatbotUrl = null,
      status = SubscriptionStatus.ACTIVE,
      isVirtualCardFrozen = true,
      gdprWipeRequested = false,
      notes = "UPI AutoPay token"
    ),
    Subscription(
      id = "sub_whoop_fitness",
      merchantName = "Whoop Membership",
      currentCost = 2490.0,
      billingCadence = BillingCadence.MONTHLY,
      classification = SubscriptionClassification.ZOMBIE_SUBSCRIPTION,
      recommendedAction = RecommendedAction.`1_CLICK_CANCEL`,
      targetNegotiatedPrice = 0.0,
      negotiationLeverage = "Hardware unused > 60 days",
      category = "Fitness",
      startDate = "May 10, 2026",
      renewalDate = "In 5 days",
      status = SubscriptionStatus.CANCELLED,
      isVirtualCardFrozen = true,
      gdprWipeRequested = true,
      notes = "Deprovisioned & data wipe invoked"
    ),
    Subscription(
      id = "sub_spotify_duo",
      merchantName = "Spotify Premium Duo",
      currentCost = 179.0,
      billingCadence = BillingCadence.MONTHLY,
      classification = SubscriptionClassification.ACTIVE_UTILITY,
      recommendedAction = RecommendedAction.KEEP,
      targetNegotiatedPrice = 149.0,
      negotiationLeverage = "Student / annual prepayment tier",
      category = "Music",
      startDate = "Aug 15, 2026",
      renewalDate = "In 14 days",
      status = SubscriptionStatus.ACTIVE,
      notes = "UPI Mandate via SBI"
    ),
    Subscription(
      id = "sub_disney_hotstar",
      merchantName = "Disney+ Hotstar Super",
      currentCost = 899.0,
      billingCadence = BillingCadence.ANNUAL,
      classification = SubscriptionClassification.ACTIVE_UTILITY,
      recommendedAction = RecommendedAction.KEEP,
      targetNegotiatedPrice = 699.0,
      negotiationLeverage = "Bundled with broadband",
      category = "Entertainment",
      startDate = "Jan 12, 2026",
      renewalDate = "In 22 days",
      status = SubscriptionStatus.ACTIVE,
      notes = "AutoPay via ICICI Bank"
    ),
    Subscription(
      id = "sub_cult_fit",
      merchantName = "Cult.fit Elite Pass",
      currentCost = 1499.0,
      billingCadence = BillingCadence.MONTHLY,
      classification = SubscriptionClassification.ZOMBIE_SUBSCRIPTION,
      recommendedAction = RecommendedAction.`1_CLICK_CANCEL`,
      targetNegotiatedPrice = 0.0,
      negotiationLeverage = "0 visits logged in last 45 days",
      category = "Fitness",
      startDate = "Jun 01, 2026",
      renewalDate = "In 4 days",
      status = SubscriptionStatus.ACTIVE,
      notes = "Zombie gym pass"
    ),
    Subscription(
      id = "sub_swiggy_one",
      merchantName = "Swiggy One",
      currentCost = 299.0,
      billingCadence = BillingCadence.MONTHLY,
      classification = SubscriptionClassification.ACTIVE_UTILITY,
      recommendedAction = RecommendedAction.KEEP,
      targetNegotiatedPrice = 199.0,
      negotiationLeverage = "Free delivery on orders above ₹149",
      category = "Food & Essentials",
      startDate = "Jul 20, 2026",
      renewalDate = "In 10 days",
      status = SubscriptionStatus.ACTIVE,
      notes = "AutoPay via HDFC"
    ),
    Subscription(
      id = "sub_chatgpt_plus",
      merchantName = "ChatGPT Plus (OpenAI)",
      currentCost = 1999.0,
      billingCadence = BillingCadence.MONTHLY,
      classification = SubscriptionClassification.ACTIVE_UTILITY,
      recommendedAction = RecommendedAction.AI_NEGOTIATE,
      targetNegotiatedPrice = 1499.0,
      negotiationLeverage = "Team workspace volume licensing",
      category = "AI Tools",
      startDate = "Mar 11, 2026",
      renewalDate = "In 18 days",
      status = SubscriptionStatus.ACTIVE,
      notes = "USD 20 equivalent"
    ),
    Subscription(
      id = "sub_adobe_creative",
      merchantName = "Adobe Creative Cloud",
      currentCost = 4230.0,
      billingCadence = BillingCadence.MONTHLY,
      classification = SubscriptionClassification.ZOMBIE_SUBSCRIPTION,
      recommendedAction = RecommendedAction.AI_NEGOTIATE,
      targetNegotiatedPrice = 2899.0,
      negotiationLeverage = "Only Photoshop & Illustrator accessed; downgrade to photography bundle",
      category = "Design",
      startDate = "Feb 05, 2026",
      renewalDate = "In 2 days",
      status = SubscriptionStatus.ACTIVE,
      notes = "Pre-debit alert received"
    ),
    Subscription(
      id = "sub_google_one",
      merchantName = "Google One 100GB",
      currentCost = 130.0,
      billingCadence = BillingCadence.MONTHLY,
      classification = SubscriptionClassification.ACTIVE_UTILITY,
      recommendedAction = RecommendedAction.KEEP,
      targetNegotiatedPrice = 110.0,
      negotiationLeverage = "Switch to annual payment for 16% discount",
      category = "Cloud & Dev",
      startDate = "Aug 02, 2026",
      renewalDate = "Oct 13, 2026",
      status = SubscriptionStatus.ACTIVE,
      notes = "Google Play AutoPay"
    ),
    Subscription(
      id = "sub_airtel_xstream",
      merchantName = "Airtel Xstream Fiber",
      currentCost = 1178.0,
      billingCadence = BillingCadence.MONTHLY,
      classification = SubscriptionClassification.ACTIVE_UTILITY,
      recommendedAction = RecommendedAction.AI_NEGOTIATE,
      targetNegotiatedPrice = 999.0,
      negotiationLeverage = "Match Jio Fiber 100 Mbps pricing",
      category = "Telecom & Broadband",
      startDate = "Sep 01, 2026",
      renewalDate = "In 8 days",
      chatbotUrl = "https://wa.me/919810101010",
      status = SubscriptionStatus.ACTIVE,
      notes = "Airtel broadband recurring"
    )
  )

  val initialEnterpriseSeats: List<EnterpriseSeat> = listOf(
    EnterpriseSeat(
      id = "seat_figma",
      appName = "Figma Enterprise",
      iconKey = "figma",
      totalSeats = 48,
      activeSeats = 34,
      idleSeats = 14,
      costPerSeatMonthly = 75.0,
      ssoProvider = "Okta SSO",
      isReclaimed = false,
      contractRenewalDate = "Nov 2026",
      complianceTier = "SOC2 Type II"
    ),
    EnterpriseSeat(
      id = "seat_datadog",
      appName = "Datadog APM",
      iconKey = "datadog",
      totalSeats = 25,
      activeSeats = 19,
      idleSeats = 6,
      costPerSeatMonthly = 130.0,
      ssoProvider = "Google Workspace SSO",
      isReclaimed = false,
      contractRenewalDate = "Dec 2026",
      complianceTier = "HIPAA / SOC2"
    ),
    EnterpriseSeat(
      id = "seat_github",
      appName = "GitHub Enterprise",
      iconKey = "github",
      totalSeats = 60,
      activeSeats = 52,
      idleSeats = 8,
      costPerSeatMonthly = 21.0,
      ssoProvider = "Azure AD",
      isReclaimed = false,
      contractRenewalDate = "Jan 2027",
      complianceTier = "ISO 27001"
    ),
    EnterpriseSeat(
      id = "seat_slack",
      appName = "Slack Business+",
      iconKey = "slack",
      totalSeats = 85,
      activeSeats = 75,
      idleSeats = 10,
      costPerSeatMonthly = 15.0,
      ssoProvider = "Okta SSO",
      isReclaimed = false,
      contractRenewalDate = "Oct 2026",
      complianceTier = "SOC2 / FedRAMP"
    )
  )

  val initialTransactions: List<Transaction> = listOf(
    Transaction(
      id = "tx_netflix_sep",
      date = "2026-09-23",
      merchant = "Netflix Entertainment",
      amount = 649.0,
      category = "Entertainment",
      accountName = "HDFC Salary A/C",
      isRecurring = true,
      cadenceDays = 30
    ),
    Transaction(
      id = "tx_netflix_aug",
      date = "2026-08-23",
      merchant = "Netflix Entertainment",
      amount = 649.0,
      category = "Entertainment",
      accountName = "HDFC Salary A/C",
      isRecurring = true,
      cadenceDays = 30
    ),
    Transaction(
      id = "tx_spotify_sep",
      date = "2026-09-18",
      merchant = "Spotify India",
      amount = 179.0,
      category = "Music",
      accountName = "SBI Savings A/C",
      isRecurring = true,
      cadenceDays = 30
    ),
    Transaction(
      id = "tx_spotify_aug",
      date = "2026-08-18",
      merchant = "Spotify India",
      amount = 179.0,
      category = "Music",
      accountName = "SBI Savings A/C",
      isRecurring = true,
      cadenceDays = 30
    ),
    Transaction(
      id = "tx_jio_sep",
      date = "2026-09-28",
      merchant = "Reliance Jio Infocomm",
      amount = 999.0,
      category = "Telecom & Broadband",
      accountName = "Kotak Mahindra Bank",
      isRecurring = true,
      cadenceDays = 30
    ),
    Transaction(
      id = "tx_cult_sep",
      date = "2026-09-01",
      merchant = "Cult.fit Elite",
      amount = 1499.0,
      category = "Fitness",
      accountName = "Axis Bank Debit",
      isRecurring = true,
      cadenceDays = 30
    )
  )

  val sampleRawTransactions: List<Transaction> get() = initialTransactions

  val initialRenewalAlerts: List<RenewalAlert> = listOf(
    RenewalAlert(
      id = "alert_scm_1",
      subscriptionId = "sub_netflix_4k",
      merchantName = "Netflix Premium",
      currentCost = 649.0,
      billingCadence = BillingCadence.MONTHLY,
      renewalDate = "In 3 days",
      daysRemaining = 3,
      alertTitle = "AutoPay Renewal in 3 Days",
      alertBody = "Netflix Premium ₹649 will be auto-debited via HDFC Bank on Oct 27, 2026.",
      timestamp = "Today, 10:30 AM",
      channel = "Supabase Cloud Messaging (SCM)",
      isDelivered = true,
      priority = "HIGH"
    ),
    RenewalAlert(
      id = "alert_scm_2",
      subscriptionId = "sub_adobe_creative",
      merchantName = "Adobe Creative Cloud",
      currentCost = 4230.0,
      billingCadence = BillingCadence.MONTHLY,
      renewalDate = "In 2 days",
      daysRemaining = 2,
      alertTitle = "High-Cost Mandate Alert",
      alertBody = "Adobe CC ₹4,230/mo due soon. Flagged as Zombie license - review before AutoPay.",
      timestamp = "Yesterday, 3:15 PM",
      channel = "Supabase Cloud Messaging (SCM)",
      isDelivered = true,
      priority = "CRITICAL"
    )
  )

  val sampleUpiMandateMessagesText: String = """
HDFC: UPI AutoPay mandate created for Rs. 649.00 towards Netflix Entertainment on 23-Oct-2026. Ref: UMN9823412. Mandate ID: 98124
SBI: Rs 179.00 debited from A/C XX4921 via UPI AutoPay for Spotify India recurring subscription. UMN: 18273645.
ICICI: UPI AutoPay mandate created for Rs 299.00 to Novi Digital (Disney+ Hotstar) on 12-Oct-2026.
AXIS: Pre-debit notice: UPI AutoPay of Rs 1499.00 scheduled for Curefit Healthcare (Cult.fit) on 28-Oct-2026.
KOTAK: UPI AutoPay recurring debit of Rs 999.00 to Reliance Jio Infocomm for Jio Fiber & Mobile.
PAYTM: UPI AutoPay mandate registered successfully for Rs 1999.00 towards OpenAI ChatGPT Plus.
PHONEPE: Mandate revoked / cancelled: UPI AutoPay for Rs 1499.00 towards Amazon Prime has been stopped.
HDFC: UPI AutoPay mandate created for Rs. 299.00 towards Swiggy One subscription. UMN: 4728192.
SBI: UPI AutoPay mandate created for Rs 149.00 for Google India Google One cloud storage.
""".trimIndent()

  val sampleAuditStatementText: String = sampleUpiMandateMessagesText
}
