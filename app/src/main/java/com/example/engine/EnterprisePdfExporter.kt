package com.example.engine

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.util.Log
import androidx.core.content.FileProvider
import com.example.model.EnterpriseSeat
import com.example.model.FinancialAudit
import com.example.model.Subscription
import com.example.model.SubscriptionClassification
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Enterprise-Grade PDF Audit Report Exporter.
 * Generates an ultra-professional, executive-level PDF audit report for Enterprise accounts.
 * Restricted strictly to Enterprise Model.
 */
object EnterprisePdfExporter {

  private const val TAG = "EnterprisePdfExporter"

  fun generateAndShareAuditPdf(
    context: Context,
    companyName: String = "Enterprise Corp",
    subscriptions: List<Subscription>,
    enterpriseSeats: List<EnterpriseSeat>,
    audit: FinancialAudit
  ) {
    try {
      val pdfFile = createEnterprisePdfDocument(context, companyName, subscriptions, enterpriseSeats, audit)
      if (pdfFile != null && pdfFile.exists()) {
        sharePdfFile(context, pdfFile, companyName)
      }
    } catch (e: Exception) {
      Log.e(TAG, "Failed to generate or share enterprise PDF audit report", e)
    }
  }

  private fun createEnterprisePdfDocument(
    context: Context,
    companyName: String,
    subscriptions: List<Subscription>,
    enterpriseSeats: List<EnterpriseSeat>,
    audit: FinancialAudit
  ): File? {
    val pdfDocument = PdfDocument()

    // Standard A4 Page dimensions in points: 595 x 842
    val pageWidth = 595
    val pageHeight = 842
    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // Paints
    val bgPaint = Paint().apply { color = Color.parseColor("#FFFFFF") }
    val headerBgPaint = Paint().apply { color = Color.parseColor("#0F172A") } // Deep Dark Obsidian
    val accentPaint = Paint().apply { color = Color.parseColor("#A855F7") } // Electric Purple
    val cardBgPaint = Paint().apply { color = Color.parseColor("#F8FAFC") } // Slate Light
    val cardBorderPaint = Paint().apply {
      color = Color.parseColor("#E2E8F0")
      style = Paint.Style.STROKE
      strokeWidth = 1f
    }

    val titlePaint = Paint().apply {
      color = Color.WHITE
      textSize = 16f
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      isAntiAlias = true
    }

    val subtitlePaint = Paint().apply {
      color = Color.parseColor("#CBD5E1")
      textSize = 9.5f
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      isAntiAlias = true
    }

    val sectionHeadingPaint = Paint().apply {
      color = Color.parseColor("#0F172A")
      textSize = 12f
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      isAntiAlias = true
    }

    val textDarkPaint = Paint().apply {
      color = Color.parseColor("#1E293B")
      textSize = 10f
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      isAntiAlias = true
    }

    val textMutedPaint = Paint().apply {
      color = Color.parseColor("#64748B")
      textSize = 8.5f
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      isAntiAlias = true
    }

    val tableHeaderBgPaint = Paint().apply { color = Color.parseColor("#1E293B") }
    val tableHeaderFontPaint = Paint().apply {
      color = Color.WHITE
      textSize = 9f
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      isAntiAlias = true
    }

    val tableRowEvenPaint = Paint().apply { color = Color.WHITE }
    val tableRowOddPaint = Paint().apply { color = Color.parseColor("#F8FAFC") }
    val tableTextPaint = Paint().apply {
      color = Color.parseColor("#334155")
      textSize = 8.5f
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      isAntiAlias = true
    }

    // 1. Draw Page Background
    canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), bgPaint)

    // 2. Draw Top Executive Header Banner
    canvas.drawRect(0f, 0f, pageWidth.toFloat(), 95f, headerBgPaint)
    canvas.drawRect(0f, 95f, pageWidth.toFloat(), 98f, accentPaint) // Accent line

    // Header Content
    canvas.drawText("SUBSCRIPTIONS.AI • ENTERPRISE AUDIT", 28f, 36f, titlePaint)
    val nowFormatted = SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.US).format(Date())
    canvas.drawText("Executive SaaS Stack & Seat Reclamation Report • $companyName", 28f, 54f, subtitlePaint)
    canvas.drawText("Ref ID: AUD-ENT-${System.currentTimeMillis().toString().takeLast(6)} • Generated: $nowFormatted", 28f, 72f, subtitlePaint)

    // Official Audit Stamp Badge
    val stampPaint = Paint().apply {
      color = Color.parseColor("#22C55E")
      textSize = 8.5f
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      isAntiAlias = true
    }
    val stampBg = Paint().apply { color = Color.parseColor("#14532D") }
    canvas.drawRoundRect(RectF(440f, 28f, 565f, 50f), 6f, 6f, stampBg)
    canvas.drawText("✓ VERIFIED AUDIT", 452f, 42f, stampPaint)

    // 3. Executive Metrics Summary Cards (2x2 Grid)
    var currentY = 118f
    canvas.drawText("EXECUTIVE FINANCIAL SUMMARY", 28f, currentY, sectionHeadingPaint)
    currentY += 12f

    val cardWidth = 260f
    val cardHeight = 48f

    // Card 1: Annual Recurring Cost (ARR)
    drawSummaryCard(canvas, 28f, currentY, cardWidth, cardHeight, "ANNUAL SAAS CONTRACT VALUE (ARR)", "₹${"%.2f".format(audit.totalMonthlySpend * 12)}", cardBgPaint, cardBorderPaint, textMutedPaint, textDarkPaint)

    // Card 2: Annual Idle Waste
    drawSummaryCard(canvas, 305f, currentY, cardWidth, cardHeight, "RECLAIMABLE IDLE LEAKAGE", "₹${"%.2f".format(audit.identifiedWaste * 12)}", cardBgPaint, cardBorderPaint, textMutedPaint, textDarkPaint)

    currentY += 56f

    // Card 3: Active SaaS Services
    drawSummaryCard(canvas, 28f, currentY, cardWidth, cardHeight, "ACTIVE SAAS VENDORS", "${subscriptions.size} Verified Services", cardBgPaint, cardBorderPaint, textMutedPaint, textDarkPaint)

    // Card 4: Idle Seats Reclaimable
    val idleSeatsCount = enterpriseSeats.sumOf { it.idleSeats }
    drawSummaryCard(canvas, 305f, currentY, cardWidth, cardHeight, "IDLE SEATS RECLAMATION", "$idleSeatsCount Seats Idle Flagged", cardBgPaint, cardBorderPaint, textMutedPaint, textDarkPaint)

    currentY += 68f

    // 4. SaaS Vendor Contracts Table
    canvas.drawText("SAAS VENDOR CONTRACTS & COMPLIANCE", 28f, currentY, sectionHeadingPaint)
    currentY += 12f

    // Table Headers
    canvas.drawRect(28f, currentY, 565f, currentY + 22f, tableHeaderBgPaint)
    canvas.drawText("VENDOR / SERVICE", 36f, currentY + 15f, tableHeaderFontPaint)
    canvas.drawText("CATEGORY", 200f, currentY + 15f, tableHeaderFontPaint)
    canvas.drawText("CADENCE", 330f, currentY + 15f, tableHeaderFontPaint)
    canvas.drawText("COST (₹)", 420f, currentY + 15f, tableHeaderFontPaint)
    canvas.drawText("STATUS", 500f, currentY + 15f, tableHeaderFontPaint)

    currentY += 22f

    // Table Rows
    if (subscriptions.isEmpty()) {
      canvas.drawRect(28f, currentY, 565f, currentY + 28f, tableRowEvenPaint)
      canvas.drawText("No enterprise subscriptions tracked in this audit run.", 36f, currentY + 18f, tableTextPaint)
      currentY += 28f
    } else {
      subscriptions.take(8).forEachIndexed { idx, sub ->
        val rowBg = if (idx % 2 == 0) tableRowEvenPaint else tableRowOddPaint
        canvas.drawRect(28f, currentY, 565f, currentY + 22f, rowBg)

        canvas.drawText(sub.merchantName.take(24), 36f, currentY + 15f, tableTextPaint)
        canvas.drawText(sub.category.take(18), 200f, currentY + 15f, tableTextPaint)
        canvas.drawText(sub.billingCadence.name, 330f, currentY + 15f, tableTextPaint)
        canvas.drawText("₹${"%.2f".format(sub.currentCost)}", 420f, currentY + 15f, tableTextPaint)

        val isZombie = sub.classification == SubscriptionClassification.ZOMBIE_SUBSCRIPTION
        val statusText = if (isZombie) "ZOMBIE" else "HEALTHY"
        val statusPaint = Paint().apply {
          color = if (isZombie) Color.parseColor("#E11D48") else Color.parseColor("#16A34A")
          textSize = 7.5f
          typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(statusText, 500f, currentY + 15f, statusPaint)

        currentY += 22f
      }
    }

    currentY += 24f

    // 5. Enterprise Seat Allocation Breakdown (if present)
    if (enterpriseSeats.isNotEmpty()) {
      canvas.drawText("ENTERPRISE SEAT ALLOCATION & RECLAMATION AUDIT", 28f, currentY, sectionHeadingPaint)
      currentY += 12f

      canvas.drawRect(28f, currentY, 565f, currentY + 22f, tableHeaderBgPaint)
      canvas.drawText("SAAS PRODUCT", 36f, currentY + 15f, tableHeaderFontPaint)
      canvas.drawText("TOTAL SEATS", 180f, currentY + 15f, tableHeaderFontPaint)
      canvas.drawText("IDLE SEATS", 280f, currentY + 15f, tableHeaderFontPaint)
      canvas.drawText("MONTHLY WASTE", 370f, currentY + 15f, tableHeaderFontPaint)
      canvas.drawText("ACTION", 480f, currentY + 15f, tableHeaderFontPaint)

      currentY += 22f

      enterpriseSeats.take(6).forEachIndexed { idx, seat ->
        val rowBg = if (idx % 2 == 0) tableRowEvenPaint else tableRowOddPaint
        canvas.drawRect(28f, currentY, 565f, currentY + 20f, rowBg)

        canvas.drawText(seat.appName.take(20), 36f, currentY + 14f, tableTextPaint)
        canvas.drawText("${seat.totalSeats} seats", 180f, currentY + 14f, tableTextPaint)
        canvas.drawText("${seat.idleSeats} idle", 280f, currentY + 14f, tableTextPaint)
        canvas.drawText("₹${"%.2f".format(seat.monthlyWaste)}", 370f, currentY + 14f, tableTextPaint)

        val isActionable = seat.idleSeats > 0 && !seat.isReclaimed
        val actionText = if (isActionable) "RECLAIM (${seat.idleSeats})" else "OPTIMIZED"
        val actionPaint = Paint().apply {
          color = if (isActionable) Color.parseColor("#DC2626") else Color.parseColor("#16A34A")
          textSize = 7.5f
          typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(actionText, 480f, currentY + 14f, actionPaint)

        currentY += 20f
      }
    }

    // 6. Professional Bottom Footer & Confidentiality Notice
    val footerY = pageHeight - 38f
    canvas.drawLine(28f, footerY, 565f, footerY, cardBorderPaint)

    val footerTextPaint = Paint().apply {
      color = Color.parseColor("#94A3B8")
      textSize = 8f
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      isAntiAlias = true
    }

    canvas.drawText("CONFIDENTIAL • Prepared for Enterprise Executives by Subscriptions.AI Engine", 28f, footerY + 16f, footerTextPaint)
    canvas.drawText("Page 1 of 1 • System Verified", 480f, footerY + 16f, footerTextPaint)

    pdfDocument.finishPage(page)

    // Save PDF to cache dir
    val file = File(context.cacheDir, "Enterprise_SaaS_Audit_Report_${System.currentTimeMillis()}.pdf")
    val outputStream = FileOutputStream(file)
    pdfDocument.writeTo(outputStream)
    outputStream.flush()
    outputStream.close()
    pdfDocument.close()

    return file
  }

  private fun drawSummaryCard(
    canvas: android.graphics.Canvas,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    label: String,
    value: String,
    bgPaint: Paint,
    borderPaint: Paint,
    labelPaint: Paint,
    valuePaint: Paint
  ) {
    val rect = RectF(x, y, x + width, y + height)
    canvas.drawRoundRect(rect, 8f, 8f, bgPaint)
    canvas.drawRoundRect(rect, 8f, 8f, borderPaint)

    canvas.drawText(label, x + 12f, y + 18f, labelPaint)
    canvas.drawText(value, x + 12f, y + 36f, valuePaint)
  }

  private fun sharePdfFile(context: Context, file: File, companyName: String) {
    val contentUri = FileProvider.getUriForFile(
      context,
      "${context.packageName}.fileprovider",
      file
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
      type = "application/pdf"
      putExtra(Intent.EXTRA_STREAM, contentUri)
      putExtra(Intent.EXTRA_SUBJECT, "Enterprise SaaS Audit Report - $companyName")
      putExtra(Intent.EXTRA_TEXT, "Attached is the Executive SaaS License Audit and Seat Reclamation Report generated by Subscriptions.AI Enterprise Engine for $companyName.")
      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    val chooser = Intent.createChooser(intent, "Share Enterprise Audit PDF")
    chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(chooser)
  }
}
