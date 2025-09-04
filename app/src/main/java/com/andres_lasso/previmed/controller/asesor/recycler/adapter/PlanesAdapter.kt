package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.Plan

class PlanesAdapter(private val listaPlanes: List<Plan>) :
    RecyclerView.Adapter<PlanesAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTipoPlan: TextView = itemView.findViewById(R.id.tvTipoPlan)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcionPlan)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecioPlan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_planes, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = listaPlanes[position]
        holder.tvTipoPlan.text = plan.tipoPlan
        holder.tvDescripcion.text = plan.descripcion
        holder.tvPrecio.text = plan.precio
    }

    override fun getItemCount(): Int = listaPlanes.size
}
