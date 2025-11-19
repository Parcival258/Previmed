package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.PacienteData

class PacienteAseAdapter(
    private val listaPacientes: List<PacienteData>,
    private val onClick: (PacienteData) -> Unit
) : RecyclerView.Adapter<PacienteAseAdapter.PacienteViewHolder>() {

    inner class PacienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.Nombre1)
        val doc: TextView = itemView.findViewById(R.id.textDoc)

        init {
            itemView.setOnClickListener {
                onClick(listaPacientes[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pacientesasesor, parent, false)
        return PacienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val paciente = listaPacientes[position]
        val usuario = paciente.usuario

        // Usamos llamadas seguras y valores por defecto
        val nombre = usuario?.nombre ?: "Sin nombre"
        val apellido = usuario?.apellido ?: ""
        val numeroDocumento = usuario?.numeroDocumento ?: ""

        val nombreCompleto = "$nombre $apellido".trim()
        holder.nombre.text = nombreCompleto
        holder.doc.text = "Doc: $numeroDocumento"
    }

    override fun getItemCount(): Int = listaPacientes.size
}
