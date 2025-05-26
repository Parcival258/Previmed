package com.andres_lasso.previmed.controller.medico.adapter

import HistoryViewHolder
import com.andres_lasso.previmed.controller.medico.adapter.VisitsHistoryAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.controller.medico.medicoDAO.VisitHistoryDao
import com.andres_lasso.previmed.databinding.ItemVisitsHistoryBinding

class VisitsHistoryAdapter(private val visitsHistoryList: List<VisitHistoryDao>) :
    RecyclerView.Adapter<HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        // Usamos View Binding en lugar de inflate directo
        val binding = ItemVisitsHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = visitsHistoryList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = visitsHistoryList.size
}