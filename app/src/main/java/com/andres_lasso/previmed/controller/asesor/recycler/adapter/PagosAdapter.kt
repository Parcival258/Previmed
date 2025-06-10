package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
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
        var expand_card_pagos = holder.binding.expandCardPagos;
        var item_card_pagos = holder.binding.itemCardPagos;
        var btnVerDetallerPagos = holder.binding.btnVerDetallesPagos;

        holder.binding.btnVerDetallesPagos.setOnClickListener(){
            if(expand_card_pagos.visibility == View.GONE){
                TransitionManager.beginDelayedTransition(item_card_pagos, AutoTransition())
                expand_card_pagos.visibility = View.VISIBLE;
                btnVerDetallerPagos.text = "Menos detalles"
            }else{
                TransitionManager.beginDelayedTransition(item_card_pagos, AutoTransition())
                expand_card_pagos.visibility = View.GONE;
                btnVerDetallerPagos.text = "Ver detalles"
            }
        }
    }

    override fun getItemCount(): Int {
        return pagos.size
    }
}