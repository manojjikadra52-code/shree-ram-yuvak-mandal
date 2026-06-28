package com.example.data

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PdfReportData(
    val monthName: String,
    val year: Int,
    val totalAavak: Int,
    val totalJavak: Int,
    val netBalance: Int,
    val incomeList: List<Pair<String, Int>>,
    val expenseList: List<Pair<String, Int>>,
    val advanceList: List<Pair<String, Int>>,
    val pendingList: List<Pair<String, String>>
)

object PdfGenerator {
    fun generateMonthlyReport(context: Context, data: PdfReportData): File? {
        val pdfDocument = PdfDocument()
        
        // Page Context helper to handle dynamic multi-page generation seamlessly
        class PageContext {
            var pageNumber = 1
            var pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
            var page = pdfDocument.startPage(pageInfo)
            var canvas = page.canvas
            
            fun finishCurrentPage() {
                val paint = Paint()
                paint.color = Color.rgb(180, 180, 180)
                paint.textSize = 8f
                paint.isFakeBoldText = false
                paint.isAntiAlias = true
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText("Shree Ram Yuvak Mandal Accounts | Page $pageNumber | Jay Shree Ram", 297.5f, 815f, paint)
                
                pdfDocument.finishPage(page)
            }
            
            fun startNextPage() {
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                
                // Draw elegant top header bar on new pages
                val paint = Paint()
                paint.color = Color.rgb(230, 81, 0) // traditional saffron
                paint.textSize = 10f
                paint.isFakeBoldText = true
                paint.isAntiAlias = true
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText("Shree Ram Yuvak Mandal - Hisab Report (Continued)", 40f, 40f, paint)
                
                paint.color = Color.rgb(224, 224, 224)
                canvas.drawLine(40f, 48f, 555f, 48f, paint)
            }
        }
        
        val contextHolder = PageContext()
        var canvas = contextHolder.canvas
        val paint = Paint()
        
        // Brand Color Palette
        val brandColor = Color.rgb(230, 81, 0) // Deep Orange / Saffron
        val darkGray = Color.rgb(33, 33, 33)
        val lightGrayBg = Color.rgb(245, 245, 245)
        
        // 1. Draw elegant Header
        paint.color = brandColor
        paint.textSize = 24f
        paint.isFakeBoldText = true
        paint.isAntiAlias = true
        canvas.drawText("Shree Ram Yuvak Mandal", 40f, 60f, paint)
        
        // Subtitle Info
        paint.color = darkGray
        paint.textSize = 11f
        paint.isFakeBoldText = false
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        canvas.drawText("Mandal Hisab Report | Generated On: ${dateFormat.format(Date())}", 40f, 85f, paint)
        
        paint.color = brandColor
        paint.textSize = 13f
        paint.isFakeBoldText = true
        val periodText = if (data.year > 0) "Report Month: ${data.monthName} ${data.year}" else "Report Period: ${data.monthName}"
        canvas.drawText(periodText, 40f, 108f, paint)
        
        // Divider line below header
        paint.color = Color.rgb(224, 224, 224)
        canvas.drawLine(40f, 120f, 555f, 120f, paint)
        
        // 2. Summary Block
        paint.color = lightGrayBg
        canvas.drawRect(40f, 135f, 555f, 205f, paint)
        
        paint.color = brandColor // Saffron bar on Left for summary elegance
        canvas.drawRect(40f, 135f, 45f, 205f, paint)
        
        paint.color = darkGray
        paint.textSize = 10f
        paint.isFakeBoldText = true
        canvas.drawText("CASH VIEW SUMMARY (ટૂંકી વિગત)", 55f, 153f, paint)
        
        paint.isFakeBoldText = false
        paint.textSize = 10f
        canvas.drawText("Total Incoming (કુલ આવક):  ₹ ${data.totalAavak}/-", 55f, 173f, paint)
        canvas.drawText("Total Outgoing (કુલ જાવક):  ₹ ${data.totalJavak}/-", 55f, 191f, paint)
        
        paint.isFakeBoldText = true
        paint.color = brandColor
        canvas.drawText("Mandal Net Cash:  ₹ ${data.netBalance}/-", 320f, 182f, paint)
        
        var yPos = 225f
        
        // 3. Prepare Double-Entry Cash Book items
        // Column items represented as row data structures
        data class RowItem(
            val type: Int, // 0: Column Title / Main Total, 1: SubHeader Group, 2: Regular Data row, 3: Blank Spacer
            val text: String = "",
            val amount: Int = 0
        )
        
        fun getRowHeight(item: RowItem?): Float {
            if (item == null) return 14f
            return when (item.type) {
                0 -> 22f
                1 -> 18f
                2 -> 14f
                3 -> 10f
                else -> 14f
            }
        }
        
        // Build Left column (Aavak Receipts)
        val leftList = mutableListOf<RowItem>()
        leftList.add(RowItem(type = 0, text = "આવક / જમા વિગત (Receipts / Aavak)"))
        
        leftList.add(RowItem(type = 1, text = "સભ્યોનું યોગદાન (Member Contributions)"))
        if (data.incomeList.isEmpty()) {
            leftList.add(RowItem(type = 2, text = "(કોઈ ફાળો જમા નથી)", amount = 0))
        } else {
            for (item in data.incomeList) {
                leftList.add(RowItem(type = 2, text = item.first, amount = item.second))
            }
        }
        
        leftList.add(RowItem(type = 3))
        
        // Advance group inside left-hand column
        leftList.add(RowItem(type = 1, text = "અગાઉથી આવેલ રકમ (Advance Income)"))
        if (data.advanceList.isEmpty()) {
            leftList.add(RowItem(type = 2, text = "(કોઈ એડવાન્સ જમા નથી)", amount = 0))
        } else {
            for (item in data.advanceList) {
                leftList.add(RowItem(type = 2, text = item.first + " (Adv)", amount = item.second))
            }
        }
        
        leftList.add(RowItem(type = 3))
        leftList.add(RowItem(type = 0, text = "કુલ જમા આવક (Total Revenue)", amount = data.totalAavak))
        
        // Build Right column (Javak Expenses)
        val rightList = mutableListOf<RowItem>()
        rightList.add(RowItem(type = 0, text = "જાવક / ખર્ચ વિગત (Payments / Javak)"))
        
        rightList.add(RowItem(type = 1, text = "ચૂકવેલા ખર્ચની વિગતો (Expenses)"))
        if (data.expenseList.isEmpty()) {
            rightList.add(RowItem(type = 2, text = "(કોઈ ખર્ચ નોંધાયેલ નથી)", amount = 0))
        } else {
            for (item in data.expenseList) {
                rightList.add(RowItem(type = 2, text = item.first, amount = item.second))
            }
        }
        
        // To align Ledger totals, pad right column containing fewer rows with spacers so both totals sit at base side-by-side!
        val leftSizeWithTotal = leftList.size
        val rightSizeWithoutTotal = rightList.size
        val paddingNeeded = leftSizeWithTotal - rightSizeWithoutTotal - 1
        if (paddingNeeded > 0) {
            repeat(paddingNeeded) {
                rightList.add(RowItem(type = 3))
            }
        }
        rightList.add(RowItem(type = 0, text = "કુલ ઉધાર ખર્ચ (Total Expenses)", amount = data.totalJavak))
        
        val maxRows = maxOf(leftList.size, rightList.size)
        
        // Border & divider configuration
        val borderPaint = Paint().apply {
            color = Color.rgb(200, 200, 200)
            style = Paint.Style.STROKE
            strokeWidth = 0.8f
        }
        val textPaint = Paint().apply {
            color = Color.rgb(33, 33, 33)
            textSize = 9f
            isAntiAlias = true
        }
        val amountPaint = Paint().apply {
            color = Color.rgb(33, 33, 33)
            textSize = 9f
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
        }
        
        // Draw the top boundary of the Cash Ledger Table
        canvas.drawLine(40f, yPos, 555f, yPos, borderPaint)
        
        // Loop and render parallel double-entry rows
        for (i in 0 until maxRows) {
            val leftItem = leftList.getOrNull(i)
            val rightItem = rightList.getOrNull(i)
            val rowHeight = maxOf(getRowHeight(leftItem), getRowHeight(rightItem))
            
            // Handle automatic pagebreak if rows overflow the page height
            if (yPos + rowHeight > 790f) {
                contextHolder.finishCurrentPage()
                contextHolder.startNextPage()
                canvas = contextHolder.canvas // update current reference
                yPos = 65f
                // Draw top boundary on the new page
                canvas.drawLine(40f, yPos, 555f, yPos, borderPaint)
            }
            
            // 1. Draw Left Cell Background box
            val leftBgPaint = Paint()
            leftBgPaint.color = when (leftItem?.type) {
                0 -> Color.rgb(255, 236, 179) // Header orange-gold bg
                1 -> Color.rgb(255, 248, 225) // Subheader soft gold bg
                else -> Color.WHITE
            }
            canvas.drawRect(40f, yPos, 297.5f, yPos + rowHeight, leftBgPaint)
            
            // 2. Draw Right Cell Background box
            val rightBgPaint = Paint()
            rightBgPaint.color = when (rightItem?.type) {
                0 -> Color.rgb(255, 236, 179)
                1 -> Color.rgb(255, 248, 225)
                else -> Color.WHITE
            }
            canvas.drawRect(297.5f, yPos, 555f, yPos + rowHeight, rightBgPaint)
            
            // 3. Draw Left Item details
            if (leftItem != null) {
                val fSize = when (leftItem.type) {
                    0 -> 9.5f
                    1 -> 8.5f
                    else -> 8f
                }
                textPaint.textSize = fSize
                textPaint.isFakeBoldText = (leftItem.type == 0 || leftItem.type == 1)
                textPaint.color = when (leftItem.type) {
                    0 -> Color.rgb(230, 81, 0)
                    1 -> Color.rgb(191, 54, 12)
                    else -> Color.rgb(33, 33, 33)
                }
                
                val yText = yPos + (rowHeight + fSize) / 2f - 1.5f
                if (leftItem.type == 0 || leftItem.type == 1) {
                    canvas.drawText(leftItem.text, 46f, yText, textPaint)
                    if (leftItem.amount != 0) {
                        amountPaint.textSize = fSize
                        amountPaint.isFakeBoldText = true
                        amountPaint.color = textPaint.color
                        canvas.drawText("₹ ${leftItem.amount}/-", 288f, yText, amountPaint)
                    }
                } else if (leftItem.type == 2) {
                    val displayStr = if (leftItem.text.length > 32) leftItem.text.take(30) + ".." else leftItem.text
                    canvas.drawText(displayStr, 46f, yText, textPaint)
                    
                    amountPaint.textSize = fSize
                    amountPaint.isFakeBoldText = false
                    amountPaint.color = Color.rgb(33, 33, 33)
                    canvas.drawText("₹ ${leftItem.amount}/-", 288f, yText, amountPaint)
                }
            }
            
            // 4. Draw Right Item details
            if (rightItem != null) {
                val fSize = when (rightItem.type) {
                    0 -> 9.5f
                    1 -> 8.5f
                    else -> 8f
                }
                textPaint.textSize = fSize
                textPaint.isFakeBoldText = (rightItem.type == 0 || rightItem.type == 1)
                textPaint.color = when (rightItem.type) {
                    0 -> Color.rgb(230, 81, 0)
                    1 -> Color.rgb(191, 54, 12)
                    else -> Color.rgb(33, 33, 33)
                }
                
                val yText = yPos + (rowHeight + fSize) / 2f - 1.5f
                if (rightItem.type == 0 || rightItem.type == 1) {
                    canvas.drawText(rightItem.text, 304f, yText, textPaint)
                    if (rightItem.amount != 0) {
                        amountPaint.textSize = fSize
                        amountPaint.isFakeBoldText = true
                        amountPaint.color = textPaint.color
                        canvas.drawText("₹ ${rightItem.amount}/-", 546f, yText, amountPaint)
                    }
                } else if (rightItem.type == 2) {
                    val displayStr = if (rightItem.text.length > 32) rightItem.text.take(30) + ".." else rightItem.text
                    canvas.drawText(displayStr, 304f, yText, textPaint)
                    
                    amountPaint.textSize = fSize
                    amountPaint.isFakeBoldText = false
                    amountPaint.color = Color.rgb(33, 33, 33)
                    canvas.drawText("₹ ${rightItem.amount}/-", 546f, yText, amountPaint)
                }
            }
            
            // 5. Draw Table borders/grids
            canvas.drawLine(40f, yPos, 40f, yPos + rowHeight, borderPaint) // Left border
            canvas.drawLine(297.5f, yPos, 297.5f, yPos + rowHeight, borderPaint) // Center divider line
            canvas.drawLine(555f, yPos, 555f, yPos + rowHeight, borderPaint) // Right border
            canvas.drawLine(40f, yPos + rowHeight, 555f, yPos + rowHeight, borderPaint) // Row divider line
            
            yPos += rowHeight
        }
        
        // 4. Pending Member Payments placed cleanly at the very end
        yPos += 20f
        
        if (yPos + 35f > 790f) {
            contextHolder.finishCurrentPage()
            contextHolder.startNextPage()
            canvas = contextHolder.canvas
            yPos = 65f
        }
        
        // Title Banner for Pending Dues
        val headerRectPaint = Paint().apply {
            color = Color.rgb(255, 235, 235) // clean soft warning red
            isAntiAlias = true
        }
        canvas.drawRect(40f, yPos, 555f, yPos + 22f, headerRectPaint)
        
        val headerBorderPaint = Paint().apply {
            color = Color.rgb(239, 154, 154) // red stroke
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }
        canvas.drawRect(40f, yPos, 555f, yPos + 22f, headerBorderPaint)
        
        val headerTextPaint = Paint().apply {
            color = Color.rgb(198, 40, 40) // deep emphasis red
            textSize = 10f
            isFakeBoldText = true
            isAntiAlias = true
        }
        canvas.drawText("બાકી સભ્યોની વિગત (Pending Member Payments / Dues)", 50f, yPos + 14.5f, headerTextPaint)
        
        yPos += 26f
        
        val pendingTextPaint = Paint().apply {
            color = Color.rgb(33, 33, 33)
            textSize = 8.5f
            isAntiAlias = true
        }
        val pendingAlertPaint = Paint().apply {
            color = Color.rgb(198, 40, 40)
            textSize = 8f
            isFakeBoldText = true
            isAntiAlias = true
        }
        
        if (data.pendingList.isEmpty()) {
            if (yPos + 18f > 790f) {
                contextHolder.finishCurrentPage()
                contextHolder.startNextPage()
                canvas = contextHolder.canvas
                yPos = 65f
            }
            canvas.drawText("✅ બધા સભ્યોનો ફાળો પૂર્ણ જમા છે! (All members are fully paid!)", 48f, yPos + 11f, pendingTextPaint)
            yPos += 18f
        } else {
            for (item in data.pendingList) {
                if (yPos + 18f > 790f) {
                    contextHolder.finishCurrentPage()
                    contextHolder.startNextPage()
                    canvas = contextHolder.canvas
                    yPos = 65f
                }
                
                // Draw a nice subtle red bullet point and member name
                canvas.drawText("• ${item.first}", 48f, yPos + 11f, pendingTextPaint)
                
                // Calculate position for warning details right next to name
                val nameWidth = pendingTextPaint.measureText("• ${item.first}")
                val detailsX = 48f + nameWidth + 12f
                
                canvas.drawText("[બાકી EMI: ${item.second}]", detailsX, yPos + 11f, pendingAlertPaint)
                
                yPos += 15f
            }
        }
        
        // Finish the final page
        contextHolder.finishCurrentPage()
        
        // Write PDF report to file on disk
        val filename = "Shree_Ram_Mandal_Report_${data.monthName.replace(" ", "_")}.pdf"
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (directory != null && !directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, filename)
        
        return try {
            val fos = FileOutputStream(file)
            pdfDocument.writeTo(fos)
            fos.close()
            pdfDocument.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            null
        }
    }
}
