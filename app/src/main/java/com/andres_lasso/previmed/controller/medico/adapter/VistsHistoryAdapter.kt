package com.andres_lasso.previmed.controller.medico.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.databinding.ItemVisitsHistoryBinding
import com.andres_lasso.previmed.model.Visita

class VisitsHistoryAdapter(
    private val visitas: List<Visita>
) : RecyclerView.Adapter<HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemVisitsHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val visita = visitas[position]
        holder.render(visita)
    }

    override fun getItemCount(): Int = visitas.size
}
