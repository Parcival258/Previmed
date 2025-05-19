package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.PlanesClass
import com.andres_lasso.previmed.databinding.ItemPlanesBinding

class PlanesViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemPlanesBinding.bind(view)

    fun render(planes: PlanesClass){
        binding.tvTipoPlan.text = planes.tipoPlan
        binding.tvDescripcionPlan.text = planes.desripcionPlan
        binding.tvPrecioPlan.text = planes.precioPlan
    }
}