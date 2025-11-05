package com.andres_lasso.previmed.controller.medico.adapter

import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.databinding.ItemVisitsHistoryBinding
import com.andres_lasso.previmed.model.Visita

class HistoryViewHolder(private val binding: ItemVisitsHistoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(visita: Visita) {
        // 🧠 Construir nombre completo del paciente
        val nombreCompleto = visita.paciente?.usuario?.let {
            "${it.nombre ?: ""} ${it.apellido ?: ""}".trim()
        } ?: "Paciente desconocido"

        // 🗓️ Formatear fecha ISO → DD/MM/YYYY
        val fechaFormateada = visita.fechaVisita?.takeIf { it.isNotBlank() }?.let {
            try {
                it.take(10).split("-").reversed().joinToString("/")
            } catch (_: Exception) {
                "Formato inválido"
            }
        } ?: "Sin fecha"

        // 🩺 Mostrar la información en la tarjeta
        binding.txtPacienteHistorial.text = "👤 $nombreCompleto"
        binding.txtDescripcionHistorial.text = "🧾 ${visita.descripcion ?: "Sin descripción"}"
        binding.txtFechaHistorial.text = "📅 $fechaFormateada"
        binding.txtDireccionHistorial.text = "📍 ${visita.direccion ?: "Sin dirección"}"
        binding.txtBarrioHistorial.text = "🏘 ${visita.barrio?.nombreBarrio ?: "Sin barrio"}"
        binding.txtTelefonoHistorial.text = "☎️ ${visita.telefono ?: "No disponible"}"
    }
}
