package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.utils.PreferenceHelper

class PagosBeneficiarioFragment : Fragment() {

    private val viewModel: PagosBeneficiarioViewModel by viewModels()

    private lateinit var tvFechaInicioPagos: TextView
    private lateinit var tvFechaFinPagos: TextView
    private lateinit var progressPagos: ProgressBar

    private lateinit var tvFechaInicioContrato: TextView
    private lateinit var tvFechaFinContrato: TextView
    private lateinit var tvProximoPago: TextView
    private lateinit var tvTitular: TextView
    private lateinit var tvPlan: TextView
    private lateinit var tvBeneficios: TextView

    private lateinit var txtCargando: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pagos_beneficiario, container, false)

        tvFechaInicioPagos = view.findViewById(R.id.tvFechaInicioPagos)
        tvFechaFinPagos = view.findViewById(R.id.tvFechaFinPagos)
        progressPagos = view.findViewById(R.id.progressPagos)

        tvFechaInicioContrato = view.findViewById(R.id.tvFechaInicioContrato)
        tvFechaFinContrato = view.findViewById(R.id.tvFechaFinContrato)
        tvProximoPago = view.findViewById(R.id.tvProximoPago)
        tvTitular = view.findViewById(R.id.tvTitular)
        tvPlan = view.findViewById(R.id.tvPlan)
        tvBeneficios = view.findViewById(R.id.tvBeneficios)

        txtCargando = TextView(requireContext())

        val uuidPaciente = PreferenceHelper.getUsuarioId(requireContext())

        if (uuidPaciente != null) {
            viewModel.cargarPagoPorUUID(uuidPaciente)
        } else {
            txtCargando.text = "No se encontró el usuario"
        }

        observarDatos()
        return view
    }

    private fun observarDatos() {
        viewModel.pago.observe(viewLifecycleOwner) { pago ->
            if (pago != null) {

                // =============== CARD DE PROGRESO ===============
                tvFechaInicioPagos.text = formatearFecha(pago.fechaInicio)
                tvFechaFinPagos.text = formatearFecha(pago.fechaFin)

                // 🔢 Calcular progreso desde fechaInicio hasta fechaPago (próximo pago)
                try {
                    val inputFormat = java.text.SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        java.util.Locale.getDefault()
                    )

                    val fechaInicio = inputFormat.parse(pago.fechaInicio)
                    val fechaProximoPago = inputFormat.parse(pago.fechaPago)
                    val hoy = java.util.Date()

                    if (fechaInicio != null && fechaProximoPago != null) {
                        val totalMillis = fechaProximoPago.time - fechaInicio.time
                        val transcurridoMillis = hoy.time - fechaInicio.time

                        val progreso = if (totalMillis > 0) {
                            ((transcurridoMillis.toDouble() / totalMillis.toDouble()) * 100).toInt()
                        } else {
                            0
                        }

                        // Aseguramos que quede entre 0 y 100
                        progressPagos.progress = progreso.coerceIn(0, 100)
                    } else {
                        progressPagos.progress = 0
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    progressPagos.progress = 0
                }

                // =============== CARD DETALLE CONTRATO ===============
                tvFechaInicioContrato.text = formatearFecha(pago.fechaInicio)
                tvFechaFinContrato.text = formatearFecha(pago.fechaFin)
                tvProximoPago.text = formatearFecha(pago.fechaPago)
                tvTitular.text = pago.titular
                tvPlan.text = pago.tipoPlan
                tvBeneficios.text = pago.descripcionPlan

            } else {
                txtCargando.text = "No se pudo cargar la información"
            }
        }
    }

    private fun formatearFecha(fechaISO: String): String {
        return try {
            val inputFormat = java.text.SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                java.util.Locale.getDefault()
            )
            val outputFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            val date = inputFormat.parse(fechaISO)
            outputFormat.format(date ?: java.util.Date())
        } catch (e: Exception) {
            fechaISO
        }
    }
}
