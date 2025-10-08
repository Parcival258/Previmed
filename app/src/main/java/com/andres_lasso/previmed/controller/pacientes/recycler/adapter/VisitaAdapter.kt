package com.andres_lasso.previmed.controller.pacientes.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.databinding.ItemVisitaBinding
import com.andres_lasso.previmed.model.Visita

class VisitaAdapter(
    private var visitas: List<Visita>,
    private val onCancelClick: (Visita) -> Unit
) : RecyclerView.Adapter<VisitaAdapter.VisitaViewHolder>() {

    inner class VisitaViewHolder(val binding: ItemVisitaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitaViewHolder {
        val binding = ItemVisitaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VisitaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VisitaViewHolder, position: Int) {
        val visita = visitas[position]

        // Fecha corta: "2025-09-26"
        val fecha = visita.fechaVisita.take(10)
        holder.binding.tvFecha.text = "Visita Pendiente $fecha"

        // Nombre del doctor
        val nombreDoctor = visita.medico.usuario?.let {
            "Dr. ${it.nombre} ${it.apellido}"
        } ?: "Dr. Asignado"

        // Botón cancelar
        holder.binding.btnCancelar.setOnClickListener { onCancelClick(visita) }
    }

    override fun getItemCount() = visitas.size

    fun submitList(newList: List<Visita>) {
        visitas = newList
        notifyDataSetChanged()
    }
}