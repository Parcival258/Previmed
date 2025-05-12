package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.PlanesClass

class PlanesViewHolder (view: View): RecyclerView.ViewHolder(view) {

    val tipoPlan = view.findViewById<TextView>(R.id.tvTipoPlan)
    var descripcionPlan = view.findViewById<TextView>(R.id.tvDescripcionPlan)
    val precioPlan = view.findViewById<TextView>(R.id.tvPrecioPlan)

    fun render(planes: PlanesClass){
        tipoPlan.text = planes.tipoPlan
        descripcionPlan.text = planes.desripcionPlan
        precioPlan.text = planes.precioPlan
    }
}