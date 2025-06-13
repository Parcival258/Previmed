package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.PagosClass

class PagosAdapter(
    private var pagos: List<PagosClass>
) : RecyclerView.Adapter<PagosViewHolder>() {

    fun updatePagos(newList: List<PagosClass>) {
        pagos = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PagosViewHolder(layoutInflater.inflate(R.layout.item_pagos, parent, false))
    }

    override fun onBindViewHolder(holder: PagosViewHolder, position: Int) {
        val item = pagos[position]
        holder.render(item)

        val expand_card_pagos = holder.binding.expandCardPagos
        val item_card_pagos = holder.binding.itemCardPagos
        val textVerDetallesPagos = holder.binding.textVerDetalles
        val iconArrow = holder.binding.iconArrow
        val monto = holder.binding.tvMonto

        holder.binding.btnVerDetallesPagos.setOnClickListener {
            val isVisible = expand_card_pagos.visibility == View.VISIBLE

            TransitionManager.beginDelayedTransition(item_card_pagos, AutoTransition())

            if (isVisible) {
                expand_card_pagos.visibility = View.GONE
                textVerDetallesPagos.text = "Ver más"
                iconArrow.animate().rotation(0f).start()
            } else {
                expand_card_pagos.visibility = View.VISIBLE
                textVerDetallesPagos.text = "Ver menos"
                iconArrow.animate().rotation(180f).start()
            }
        }

        monto.text = "$ ${item.monto}"
    }

    override fun getItemCount(): Int = pagos.size
}
