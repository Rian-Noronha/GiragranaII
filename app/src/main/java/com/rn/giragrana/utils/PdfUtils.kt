package com.rn.giragrana.utils
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.view.View
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
object PdfUtils {
    fun exportToPdf(listView: ListView, file: File) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(612, 792, 1).create() // Tamanho da página padrão em pontos (612 x 792)
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val adapter = listView.adapter
        val paint = Paint().apply {
            color = Color.BLACK
        }

        var yPos = 50f
        for (i in 0 until adapter.count) {
            val view = adapter.getView(i, null, listView)
            view.measure(
                View.MeasureSpec.makeMeasureSpec(canvas.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            canvas.save()
            canvas.translate(0f, yPos)
            view.draw(canvas)
            canvas.restore()
            yPos += view.measuredHeight.toFloat() + 20f
        }

        pdfDocument.finishPage(page)

        val outputStream = FileOutputStream(file)
        pdfDocument.writeTo(outputStream)

        pdfDocument.close()
        Snackbar.make(listView, "Imprimindo com sucesso:)", Snackbar.LENGTH_SHORT).show()
    }
}