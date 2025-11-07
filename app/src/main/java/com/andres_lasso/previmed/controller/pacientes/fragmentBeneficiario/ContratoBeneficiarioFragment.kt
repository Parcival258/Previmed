package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.MembresiaXUserResponse
import com.andres_lasso.previmed.utils.PreferenceHelper
import com.andres_lasso.previmed.workers.PdfDownloadWorker


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
        inflater: LayoutInflater,
        container: ViewGroup?,
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
                descargarPDFConWorkManager(it)
            } ?: run {
                Toast.makeText(
                    requireContext(),
                    "No hay contrato para generar",
                    Toast.LENGTH_SHORT
                ).show()
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

    private fun descargarPDFConWorkManager(membresia: MembresiaXUserResponse) {
        // Para Android 13+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        } else {
            iniciarDescarga(membresia)
        }
    }

    private fun iniciarDescarga(membresia: MembresiaXUserResponse) {
        Toast.makeText(requireContext(), " ⬇️ Descargado, Revisa tus nofificaciones", Toast.LENGTH_SHORT).show()

        val pdfContent = buildString {
            append("Número de Contrato: ${membresia.membresia.numeroContrato}\n")
            append("Plan: ${membresia.membresia.plan.tipoPlan}\n")
            append("Precio: $${membresia.membresia.plan.precio}\n")
            append("Beneficiarios: ${membresia.membresia.plan.cantidadBeneficiarios}\n")
            append("Fecha de Inicio: ${formatearFecha(membresia.membresia.fechaInicio)}\n")
            append("Fecha de Fin: ${formatearFecha(membresia.membresia.fechaFin)}\n")
            append("Descripción: ${membresia.membresia.plan.descripcion}\n\n")
            append(resources.getString(R.string.contrato_introduccion))
        }

        val workData = Data.Builder()
            .putString("numeroContrato", membresia.membresia.numeroContrato)
            .putString("pdfContent", pdfContent)
            .build()

        val pdfDownloadRequest = OneTimeWorkRequestBuilder<PdfDownloadWorker>()
            .setInputData(workData)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(pdfDownloadRequest)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                membresiaActual?.let { iniciarDescarga(it) }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permiso denegado para descargar el PDF",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun formatearFecha(fechaISO: String): String {
        return try {
            val inputFormat =
                java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
            val outputFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            val date = inputFormat.parse(fechaISO)
            outputFormat.format(date ?: java.util.Date())
        } catch (e: Exception) {
            fechaISO
        }
    }
}