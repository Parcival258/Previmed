package com.andres_lasso.previmed.controller.asesor

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.ActivityContratoAsesorBinding

class ContratoAsesorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContratoAsesorBinding
    private lateinit var webView: WebView

    private val palabrasNegritas = listOf(
        "CLAUSULAS CONTRACTUALES:", "PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA",
        "SEXTA", "SEPTIMA", "OCTAVA", "NOVENA", "DÉCIMA", "DÉCIMA PRIMERA",
        "DÉCIMA SEGUNDA", "DÉCIMA TERCERA", "DÉCIMA CUARTA", "DÉCIMA QUINTA",
        "DÉCIMA SEXTA", "DÉCIMA SÉPTIMA", "DÉCIMA OCTAVA",
        "OBJETO DEL CONTRATO:", "DESCRIPCION DEL SERVICIO", "VIGENCIA DEL CONTRATO",
        "CARACTERISTICAS DEL SERVICIO", "EXCLUSION DE USUARIOS DEL CONTRATO:",
        "PARAGRAFO PRIMERO", "PARAGRAFO SEGUNDO",
        "ADQUISICION DEL DERECHO DE USUARIO", "TARIFA:", "FORMA Y PERIODO DE PAGO:",
        "INTERESES DE MORA", "OBLIGACIONES", "RESPONSABILIDAD", "CAUSALES DE EXONERACION:",
        "VERACIDAD DE LA INFORMACION", "NOTIFICACIONES", "AREA DE COBERTURA",
        "TERMINACION DEL CONTRATO", "MODIFICACIÓN AL CONTRATO.", "NEGACION DEL SERVICIO",
        "PREVIMED", "PREVIMED TU MEDICO EN CASA", "EL USUARIO", "BENEFICIARIOS", "PACIENTE",
        "EL CONTRATANTE", "EL USUARIO y/o los USUARIOS",
        "Leandro Realpe Cisneros", "Representante Legal"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContratoAsesorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Leer datos dinámicos pasados por Intent
        val numeroContrato = intent.getStringExtra("numeroContrato") ?: ""
        val formaPago = intent.getStringExtra("formaPago") ?: ""
        val fechaInicio = intent.getStringExtra("fechaInicio") ?: ""
        val fechaFin = intent.getStringExtra("fechaFin") ?: ""
        val firma = intent.getStringExtra("firma") ?: ""
        val estado = intent.getBooleanExtra("estado", false)
        val pacientesAsociados = intent.getStringExtra("pacientesAsociados") ?: "No hay pacientes asociados"

        // Asignar textos dinámicos
        binding.tvNumeroContrato.text = numeroContrato
        binding.tvFormaPago.text = formaPago
        binding.tvFechaInicio.text = fechaInicio
        binding.tvFechaFin.text = fechaFin
        binding.tvFirma.text = firma
        binding.tvEstado.text = if (estado) "Activo" else "Inactivo"
        binding.tvPacientesAsociados.text = pacientesAsociados

        // Título e introducción con negritas aplicadas
        binding.tituloContrato.text = getString(R.string.title_contrato)
        binding.clausulaIntroduccion.text = aplicarNegritas(getString(R.string.contrato_introduccion))

        val clausulasViews = listOf(
            binding.clausula1, binding.clausula2, binding.clausula3, binding.clausula4,
            binding.clausula5, binding.clausula6, binding.clausula7, binding.clausula8,
            binding.clausula9, binding.clausula10, binding.clausula11, binding.clausula12,
            binding.clausula13, binding.clausula14, binding.clausula15, binding.clausula16,
            binding.clausula17, binding.clausula18
        )

        // Cargar las cláusulas con negritas
        clausulasViews.forEachIndexed { index, textView ->
            val id = resources.getIdentifier("contrato_clausula_${index + 1}", "string", packageName)
            if (id != 0) {
                textView.text = aplicarNegritas(getString(id))
            } else {
                textView.text = ""
            }
        }

        // Textos fijos
        binding.firmacontratoTexto.text = getString(R.string.firmacontrato_texto)
        binding.contratoRepresentante.text = getString(R.string.contrato_representante)
        binding.contratoRepresentanteLegal.text = getString(R.string.contrato_representantelegal)
        binding.contratoRepresentantePrevi.text = getString(R.string.contrato_representanteprevi)

        // Botón para descargar PDF
        binding.btnDescargar.setOnClickListener {
            descargarContratoPDF()
        }
    }

    // Aplica negritas en palabras clave dentro de un texto
    private fun aplicarNegritas(texto: String): SpannableString {
        val spannable = SpannableString(texto)
        palabrasNegritas.forEach { palabra ->
            var startIndex = texto.indexOf(palabra)
            while (startIndex >= 0) {
                val endIndex = startIndex + palabra.length
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                startIndex = texto.indexOf(palabra, endIndex)
            }
        }
        return spannable
    }

    // Aplica negritas en HTML
    private fun negritasHtml(texto: String): String {
        var resultado = texto
        palabrasNegritas.forEach { palabra ->
            resultado = resultado.replace(
                palabra,
                "<b>$palabra</b>",
                ignoreCase = false
            )
        }
        return resultado
    }

    // Descarga el contrato como PDF usando WebView y PrintManager
    private fun descargarContratoPDF() {
        webView = WebView(this)

        val clausulasHtml = StringBuilder()
        clausulasHtml.append("<h2>Contrato - Previmed</h2>")
        clausulasHtml.append("<p>${negritasHtml(getString(R.string.contrato_introduccion))}</p>")

        for (i in 1..18) {
            val resId = resources.getIdentifier("contrato_clausula_$i", "string", packageName)
            if (resId != 0) {
                clausulasHtml.append("<p>${negritasHtml(getString(resId))}</p>")
            }
        }

        clausulasHtml.append("<p>${getString(R.string.firmacontrato_texto)}</p>")
        clausulasHtml.append("<p>${getString(R.string.contrato_representante)}</p>")
        clausulasHtml.append("<p>${getString(R.string.contrato_representantelegal)}</p>")
        clausulasHtml.append("<p>${getString(R.string.contrato_representanteprevi)}</p>")

        webView.loadDataWithBaseURL(null, clausulasHtml.toString(), "text/html", "UTF-8", null)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
                val jobName = "Contrato_Previmed_PDF"
                val printAdapter = webView.createPrintDocumentAdapter(jobName)
                printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
            }
        }
    }
}
