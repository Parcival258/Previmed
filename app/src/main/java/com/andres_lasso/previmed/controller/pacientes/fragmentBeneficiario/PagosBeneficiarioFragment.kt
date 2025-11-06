package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.utils.PreferenceHelper

class PagosBeneficiarioFragment : Fragment() {

    private val viewModel: PagosBeneficiarioViewModel by viewModels()

    private lateinit var tvMonto: TextView
    private lateinit var tvFormaPago: TextView
    private lateinit var tvFechaPago: TextView
    private lateinit var tvEstadoPago: TextView
    private lateinit var tvTitular: TextView
    private lateinit var tvPlan: TextView
    private lateinit var tvDescripcionPlan: TextView
    private lateinit var txtCargando: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_pagos_beneficiario, container, false)

        tvMonto = view.findViewById(R.id.tvMonto)
        tvFormaPago = view.findViewById(R.id.tvFormaPago)
        tvFechaPago = view.findViewById(R.id.tvFechaPago)
        tvEstadoPago = view.findViewById(R.id.tvEstadoPago)
        tvTitular = view.findViewById(R.id.tvTitular)
        tvPlan = view.findViewById(R.id.tvPlan)
        tvDescripcionPlan = view.findViewById(R.id.tvDescripcionPlan)
        txtCargando = view.findViewById(R.id.txtCargando)

        val uuidPaciente = PreferenceHelper.getUsuarioId(requireContext())

        if (uuidPaciente != null) {
            Log.d("PAGOS_FRAG", "🔄 Cargando pagos para UUID: $uuidPaciente")
            viewModel.cargarPagoPorUUID(uuidPaciente)
        } else {
            Log.e("PAGOS_FRAG", "❌ No se encontró el UUID del paciente")
            txtCargando.text = "No se encontró el usuario"
        }

        observarDatos()
        return view
    }

    private fun observarDatos() {
        viewModel.pago.observe(viewLifecycleOwner) { pago ->
            if (pago != null) {
                tvMonto.text = "Monto: $${pago.precio}"
                tvFormaPago.text = "Forma de pago: ${pago.formaPago}"
                tvFechaPago.text = "Fecha de pago: ${formatearFecha(pago.fechaPago)}"
                tvEstadoPago.text = if (pago.fechaPago.isNotEmpty()) "Pagado" else "Pendiente"
                tvTitular.text = "Titular: ${pago.titular}"
                tvPlan.text = "Plan: ${pago.tipoPlan}"
                tvDescripcionPlan.text = "Descripción: ${pago.descripcionPlan}"

                txtCargando.visibility = View.GONE
                Log.d("PAGOS_FRAG", "✅ Datos de pago mostrados correctamente")
            } else {
                txtCargando.text = "No se pudo cargar la información del pago"
                Log.e("PAGOS_FRAG", "⚠️ No se encontró información de pago")
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
}
