package com.andres_lasso.previmed.controller.medico.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.controller.medico.medicoDAO.VisitasPendientesDao
import com.andres_lasso.previmed.databinding.ItemVisitaMedicoBinding

class VisitasPendientesAdapter(
    private val visitas: List<VisitasPendientesDao>
) : RecyclerView.Adapter<VisitasPendientesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitasPendientesViewHolder {
        val binding = ItemVisitaMedicoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VisitasPendientesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VisitasPendientesViewHolder, position: Int) {
        val visita = visitas[position]
        holder.bind(visita)
    }

    override fun getItemCount(): Int = visitas.size
}