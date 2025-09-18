package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.Plan

class PlanesAdapter(private val listaPlanes: MutableList<Plan>) : RecyclerView.Adapter<PlanesAdapter.PlanViewHolder>() {

    class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tipoPlanTextView: TextView = itemView.findViewById(R.id.tvTipoPlan)
        val precioTextView: TextView = itemView.findViewById(R.id.tvPrecioPlan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_planes, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = listaPlanes[position]
        holder.tipoPlanTextView.text = plan.tipoPlan
        holder.precioTextView.text = plan.precio.toString()
    }

    override fun getItemCount(): Int = listaPlanes.size

    // Nuevo método para agregar un plan y actualizar el RecyclerView
    fun agregarPlan(plan: Plan) {
        listaPlanes.add(plan)
        notifyItemInserted(listaPlanes.size - 1)
    }
}
