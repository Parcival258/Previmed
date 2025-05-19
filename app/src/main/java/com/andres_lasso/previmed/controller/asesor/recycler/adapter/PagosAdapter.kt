package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.PagosClass

class PagosAdapter(private  val pagos:List<PagosClass>): RecyclerView.Adapter<PagosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PagosViewHolder(layoutInflater.inflate(R.layout.item_pagos, parent, false))
    }

    override fun onBindViewHolder(holder: PagosViewHolder, position: Int) {
        val item = pagos[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return pagos.size
    }
}