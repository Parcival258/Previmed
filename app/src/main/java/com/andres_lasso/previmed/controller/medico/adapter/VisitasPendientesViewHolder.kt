package com.andres_lasso.previmed.controller.medico.adapter

import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.controller.medico.medicoDAO.VisitasPendientesDao
import com.andres_lasso.previmed.databinding.ItemVisitaMedicoBinding
import java.time.format.DateTimeFormatter

class VisitasPendientesViewHolder(
    private val binding: ItemVisitaMedicoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(visita: VisitasPendientesDao) {
        binding.apply {
            // Nombre completo
            txtFullName.text = "${visita.name} ${visita.secondName} ${visita.lastName} ${visita.secondLastName}"

            // DNI
            txtDni.text = "DNI: ${visita.dni}"

            // Dirección
            txtAddress.text = "Dirección: ${visita.address}"

            // Fecha formateada
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            txtDate.text = "Fecha: ${visita.date.format(dateFormatter)}"

            // Hora
            txtHour.text = "Hora: ${visita.hour}"

            // Descripción
            txtDescription.text = visita.description
        }
    }
}