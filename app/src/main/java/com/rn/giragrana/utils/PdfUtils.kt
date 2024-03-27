package com.rn.giragrana.utils

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.view.View
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream

object PdfUtils {
    private const val A4_WIDTH = 595
    private const val A4_HEIGHT = 842

    fun exportToPdf(listView: ListView, title: String, file: File) {
        val pdfDocument = PdfDocument()
        val adapter = listView.adapter

        var currentPage = 1
        var itemsOnPage = 0
        var yPos = 0f

        val pageWidth = A4_WIDTH.toFloat()
        val pageHeight = A4_HEIGHT.toFloat()
        val margin = 50f
        val elementSpacing = 20f


        val titlePaint = Paint().apply {
            color = Color.BLACK
            isFakeBoldText
            textSize = 36f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
        }
        val titleBounds = Rect()
        titlePaint.getTextBounds(title, 0, title.length, titleBounds)
        val titleX = pageWidth / 2
        val titleY = margin + titleBounds.height()


        val titlePage = pdfDocument.startPage(PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, 1).create())
        val canvasTitle = titlePage.canvas
        canvasTitle.drawText(title, titleX, titleY, titlePaint)
        pdfDocument.finishPage(titlePage)

        yPos += titleBounds.height() + margin

        for (i in 0 until adapter.count) {
            val view = adapter.getView(i, null, listView)
            view.measure(
                View.MeasureSpec.makeMeasureSpec(A4_WIDTH, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)

            val elementHeight = view.measuredHeight.toFloat()
            val remainingHeightOnPage = pageHeight - yPos

            if (remainingHeightOnPage < elementHeight + margin) {
                currentPage++
                yPos = 0f
            }

            val page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, currentPage).create())
            val canvas = page.canvas
            yPos += margin
            view.draw(canvas)
            yPos += elementHeight + elementSpacing
            pdfDocument.finishPage(page)
        }

        val outputStream = FileOutputStream(file)
        pdfDocument.writeTo(outputStream)

        pdfDocument.close()
        Snackbar.make(listView, "PDF exportado com sucesso:)", Snackbar.LENGTH_SHORT).show()
    }
}