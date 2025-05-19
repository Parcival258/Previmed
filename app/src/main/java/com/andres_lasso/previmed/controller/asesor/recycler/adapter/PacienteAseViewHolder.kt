package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R

class PacienteAseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nombre: TextView = itemView.findViewById(R.id.Nombre1)
    val doc: TextView = itemView.findViewById(R.id.textDoc)
    val direccion: TextView = itemView.findViewById(R.id.textDireccion)
    val plan: TextView = itemView.findViewById(R.id.textPlan)
    val botonVer: ImageButton = itemView.findViewById(R.id.BotonMirarPac)
}
