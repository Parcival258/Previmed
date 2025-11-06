package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.MembresiaXUserResponse
import com.andres_lasso.previmed.utils.PreferenceHelper
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ContratoBeneficiarioFragment : Fragment() {

    private val viewModel: ContratoViewModel by viewModels()

    private lateinit var txtNumeroContrato: TextView
    private lateinit var txtTipoPlan: TextView
    private lateinit var txtFechaInicio: TextView
    private lateinit var txtFechaFin: TextView
    private lateinit var txtPrecio: TextView
    private lateinit var txtBeneficiarios: TextView
    private lateinit var txtCargando: TextView
    private lateinit var btnGenerarPDF: Button

    private var membresiaActual: MembresiaXUserResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_contrato_beneficiario, container, false)

        txtNumeroContrato = view.findViewById(R.id.txtNumeroContrato)
        txtTipoPlan = view.findViewById(R.id.txtPlan)
        txtFechaInicio = view.findViewById(R.id.tvFechaInicio)
        txtFechaFin = view.findViewById(R.id.tvFechaFin)
        txtPrecio = view.findViewById(R.id.tvPrecioPlan)
        txtBeneficiarios = view.findViewById(R.id.txtBeneficios)
        txtCargando = view.findViewById(R.id.txtCargando)
        btnGenerarPDF = view.findViewById(R.id.btnGenerarPDF)

        val uuidPaciente = PreferenceHelper.getUsuarioId(requireContext())
        if (uuidPaciente != null) {
            viewModel.cargarMembresiaPorUUID(uuidPaciente)
        } else {
            txtCargando.text = "No se encontró el usuario"
        }

        observarDatos()

        btnGenerarPDF.setOnClickListener {
            membresiaActual?.let {
                generarPDFyDescargar(it)
            } ?: run {
                Toast.makeText(requireContext(), "No hay contrato para generar", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun observarDatos() {
        viewModel.membresia.observe(viewLifecycleOwner) { membresia ->
            if (membresia != null) {
                membresiaActual = membresia
                val m = membresia.membresia
                val plan = m.plan

                txtNumeroContrato.text = "Número de contrato: ${m.numeroContrato}"
                txtTipoPlan.text = "Plan: ${plan.tipoPlan}"
                txtFechaInicio.text = "Fecha inicio: ${formatearFecha(m.fechaInicio)}"
                txtFechaFin.text = "Fecha fin: ${formatearFecha(m.fechaFin)}"
                txtPrecio.text = "Precio: $${plan.precio}"
                txtBeneficiarios.text = "Cantidad de beneficiarios: ${plan.cantidadBeneficiarios}"

                txtCargando.visibility = View.GONE
            } else {
                txtCargando.text = "No se pudo cargar la información del contrato."
            }
        }
    }

    private fun generarPDFyDescargar(membresia: MembresiaXUserResponse) {
        lifecycleScope.launch {
            try {
                val nombreArchivo = "Contrato_${membresia.membresia.numeroContrato}.pdf"

                // Guardar en Descargas
                val downloadDir = File(android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS), "Previmed")
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs()
                }
                val file = File(downloadDir, nombreArchivo)

                // Crear PDF con iText7
                val writer = PdfWriter(file)
                val pdfDoc = PdfDocument(writer)
                val documento = Document(pdfDoc)

                val fuente = PdfFontFactory.createFont(StandardFonts.HELVETICA)
                val fuenteBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)

                // Título
                val titulo = Paragraph("CONTRATO DE AFILIACIÓN")
                    .setFont(fuenteBold)
                    .setFontSize(16f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20f)
                documento.add(titulo)

                // Información general
                val info = listOf(
                    "Número de Contrato: ${membresia.membresia.numeroContrato}",
                    "Plan: ${membresia.membresia.plan.tipoPlan}",
                    "Precio: $${membresia.membresia.plan.precio}",
                    "Beneficiarios: ${membresia.membresia.plan.cantidadBeneficiarios}",
                    "Fecha de Inicio: ${formatearFecha(membresia.membresia.fechaInicio)}",
                    "Fecha de Fin: ${formatearFecha(membresia.membresia.fechaFin)}",
                    "Descripción: ${membresia.membresia.plan.descripcion}"
                )

                info.forEach { linea ->
                    documento.add(
                        Paragraph(linea)
                            .setFont(fuente)
                            .setFontSize(11f)
                            .setMarginBottom(8f)
                    )
                }

                // Separador
                documento.add(Paragraph("\n"))

                // Introducción
                documento.add(
                    Paragraph(resources.getString(R.string.contrato_introduccion))
                        .setFont(fuente)
                        .setFontSize(10f)
                        .setMarginBottom(12f)
                )

                // Cláusulas
                documento.add(
                    Paragraph("TÉRMINOS Y CONDICIONES")
                        .setFont(fuenteBold)
                        .setFontSize(12f)
                        .setMarginBottom(12f)
                )

                val clausulas = listOf(
                    resources.getString(R.string.contrato_clausula_1),
                    resources.getString(R.string.contrato_clausula_2),
                    resources.getString(R.string.contrato_clausula_3),
                    resources.getString(R.string.contrato_clausula_4),
                    resources.getString(R.string.contrato_clausula_5),
                    resources.getString(R.string.contrato_clausula_6),
                    resources.getString(R.string.contrato_clausula_7),
                    resources.getString(R.string.contrato_clausula_8),
                    resources.getString(R.string.contrato_clausula_9),
                    resources.getString(R.string.contrato_clausula_10),
                    resources.getString(R.string.contrato_clausula_11),
                    resources.getString(R.string.contrato_clausula_12),
                    resources.getString(R.string.contrato_clausula_13),
                    resources.getString(R.string.contrato_clausula_14),
                    resources.getString(R.string.contrato_clausula_15),
                    resources.getString(R.string.contrato_clausula_16),
                    resources.getString(R.string.contrato_clausula_17),
                    resources.getString(R.string.contrato_clausula_18)
                )

                clausulas.forEach { clausula ->
                    documento.add(
                        Paragraph(clausula)
                            .setFont(fuente)
                            .setFontSize(9f)
                            .setMarginBottom(10f)
                    )
                }

                // Separador final
                documento.add(Paragraph("\n"))

                // Firma
                documento.add(
                    Paragraph(resources.getString(R.string.firmacontrato_texto))
                        .setFont(fuenteBold)
                        .setFontSize(10f)
                        .setMarginBottom(16f)
                )

                documento.add(
                    Paragraph("Fecha: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
                        .setFont(fuente)
                        .setFontSize(10f)
                        .setMarginBottom(20f)
                )

                documento.close()

                // Descargar automáticamente
                descargarPDF(requireContext(), file)

                Toast.makeText(requireContext(), "PDF guardado en Descargas/Previmed", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error al generar PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatearFecha(fechaISO: String): String {
        return try {
            val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
            val outputFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            val date = inputFormat.parse(fechaISO)
            outputFormat.format(date ?: java.util.Date())
        } catch (e: Exception) {
            fechaISO
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun descargarPDF(context: Context, file: File) {
        // Solo intenta abrir si existe app
        try {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        } catch (e: Exception) {
            // Si no hay app, simplemente se guardó en Descargas
            android.util.Log.d("PDF", "No hay app para abrir, pero se guardó en: ${file.absolutePath}")
        }
    }
}