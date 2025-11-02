package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.BarriosClass

class BarriosAdapter(private var barrios: MutableList<BarriosClass>) :
    RecyclerView.Adapter<BarriosAdapter.BarrioViewHolder>() {

    inner class BarrioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvBarrioNombre)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstadoBarrio)

        fun bind(barrio: BarriosClass) {
            tvNombre.text = barrio.nombreBarrio
            tvEstado.text = if (barrio.estado) "Activo" else "Inactivo"

            val color = if (barrio.estado)
                ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark)
            else
                ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark)

            tvEstado.setTextColor(color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarrioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_barrios, parent, false)
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
