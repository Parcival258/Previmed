package com.andres_lasso.previmed.workers

import android.content.Context
import android.app.NotificationManager
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.andres_lasso.previmed.R
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PdfDownloadWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        return try {
            val numeroContrato = inputData.getString("numeroContrato") ?: "Contrato"
            val pdfContent = inputData.getString("pdfContent") ?: ""

            // Mostrar notificación de descarga en progreso
            mostrarNotificacion("Descargando PDF...", "Generando $numeroContrato", 0, false)

            // Crear PDF
            val downloadDir = applicationContext.getExternalFilesDir("Previmed")
                ?: File(applicationContext.cacheDir, "Previmed")

            if (!downloadDir.exists()) {
                downloadDir.mkdirs()
            }

            val file = File(downloadDir, "Contrato_$numeroContrato.pdf")
            val writer = PdfWriter(file)
            val pdfDoc = PdfDocument(writer)
            val documento = Document(pdfDoc)

            val fuente = PdfFontFactory.createFont(StandardFonts.HELVETICA)
            val fuenteBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)

            // Agregar contenido
            documento.add(
                Paragraph("CONTRATO DE AFILIACIÓN")
                    .setFont(fuenteBold)
                    .setFontSize(16f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20f)
            )

            // Información general
            documento.add(
                Paragraph(pdfContent)
                    .setFont(fuente)
                    .setFontSize(10f)
                    .setMarginBottom(12f)
            )

            // Separador
            documento.add(Paragraph("\n"))

            // Cláusulas
            documento.add(
                Paragraph("TÉRMINOS Y CONDICIONES")
                    .setFont(fuenteBold)
                    .setFontSize(12f)
                    .setMarginBottom(12f)
            )

            val clausulas = listOf(
                applicationContext.getString(R.string.contrato_clausula_1),
                applicationContext.getString(R.string.contrato_clausula_2),
                applicationContext.getString(R.string.contrato_clausula_3),
                applicationContext.getString(R.string.contrato_clausula_4),
                applicationContext.getString(R.string.contrato_clausula_5),
                applicationContext.getString(R.string.contrato_clausula_6),
                applicationContext.getString(R.string.contrato_clausula_7),
                applicationContext.getString(R.string.contrato_clausula_8),
                applicationContext.getString(R.string.contrato_clausula_9),
                applicationContext.getString(R.string.contrato_clausula_10),
                applicationContext.getString(R.string.contrato_clausula_11),
                applicationContext.getString(R.string.contrato_clausula_12),
                applicationContext.getString(R.string.contrato_clausula_13),
                applicationContext.getString(R.string.contrato_clausula_14),
                applicationContext.getString(R.string.contrato_clausula_15),
                applicationContext.getString(R.string.contrato_clausula_16),
                applicationContext.getString(R.string.contrato_clausula_17),
                applicationContext.getString(R.string.contrato_clausula_18)
            )

            clausulas.forEach { clausula ->
                documento.add(
                    Paragraph(clausula)
                        .setFont(fuente)
                        .setFontSize(9f)
                        .setMarginBottom(10f)
                )
            }

            documento.close()

            // Notificación de éxito con Heads-up
            mostrarNotificacion(
                "✅ PDF Descargado",
                "Contrato_$numeroContrato.pdf",
                100,
                true,
                file
            )

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()

            // Notificación de error con Heads-up
            mostrarNotificacion(
                "❌ Error en la descarga",
                "No se pudo descargar el PDF",
                0,
                true
            )

            Result.retry()
        }
    }

    private fun mostrarNotificacion(
        titulo: String,
        mensaje: String,
        progreso: Int = 0,
        final: Boolean = false,
        file: File? = null
    ) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = 1001
        val channelId = "PDF_CHANNEL"

        // Crear canal con IMPORTANCE_HIGH para Heads-up
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId,
                "Descargas de PDF",
                NotificationManager.IMPORTANCE_HIGH  // ← Heads-up
            ).apply {
                description = "Notificaciones de descarga de PDFs"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setSmallIcon(R.drawable.ic_download)
            .setProgress(100, progreso, !final)
            .setAutoCancel(final)
            .setPriority(NotificationCompat.PRIORITY_HIGH)  // ← Heads-up
            .setCategory(NotificationCompat.CATEGORY_EVENT)

        // Si es final y hay archivo, agregar intent para abrir
        if (final && file != null) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                val uri = androidx.core.content.FileProvider.getUriForFile(
                    applicationContext,
                    "${applicationContext.packageName}.provider",
                    file
                )
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            val pendingIntent = android.app.PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
            )
            builder.setContentIntent(pendingIntent)
        }

        val notification = builder.build()
        notificationManager.notify(notificationId, notification)
    }
}