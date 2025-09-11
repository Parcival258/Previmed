package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.BarriosClass

class BarriosAdapter(private var barrios: MutableList<BarriosClass>) :
    RecyclerView.Adapter<BarriosAdapter.BarrioViewHolder>() {

    inner class BarrioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvNombre: TextView = view.findViewById(R.id.tvBarrioNombre)
        fun bind(barrio: BarriosClass) {
            tvNombre.text = barrio.nombreBarrio
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarrioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_barrios, parent, false)
        return BarrioViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarrioViewHolder, position: Int) {
        holder.bind(barrios[position])
    }

    override fun getItemCount(): Int = barrios.size

    fun actualizarLista(nuevaLista: List<BarriosClass>) {
        barrios.clear()
        barrios.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
