package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.PagosClass

class PagosAdapter(private val pagosOriginal: List<PagosClass>) :
    RecyclerView.Adapter<PagosViewHolder>() {

    private var pagosFiltrados: MutableList<PagosClass> = pagosOriginal.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PagosViewHolder(layoutInflater.inflate(R.layout.item_pagos, parent, false))
    }

    override fun onBindViewHolder(holder: PagosViewHolder, position: Int) {
        val item = pagosFiltrados[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = pagosFiltrados.size

    // Filtrar la lista por el titular del pago
    fun filter(query: String) {
        pagosFiltrados = if (query.isEmpty()) {
            pagosOriginal.toMutableList()
        } else {
            pagosOriginal.filter {
                it.titular.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}
