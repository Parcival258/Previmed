package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.PacientesAsesorFragment
import com.andres_lasso.previmed.controller.asesor.recycler.PacienteClass

data class PacienteAseAdapter(
    private val listaPacientes: List<PacienteClass>,
    private val onClick: (PacienteClass) -> Unit
) : RecyclerView.Adapter<PacienteAseAdapter.PacienteViewHolder>() {

    class PacienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.Nombre1)
        val doc: TextView = itemView.findViewById(R.id.textDoc)
        val direccion: TextView = itemView.findViewById(R.id.textDireccion)
        val plan: TextView = itemView.findViewById(R.id.textPlan)
        val botonVer: ImageButton = itemView.findViewById(R.id.BotonMirarPac)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pacientesasesor, parent, false)
        return PacienteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val paciente = listaPacientes[position]

        holder.nombre.text = paciente.idNombre
        holder.doc.text = "Doc: ${paciente.docPaciente}"
        holder.direccion.text = "Dirección: ${paciente.direccionPaciente}"
        holder.plan.text = "Plan: ${paciente.planPaciente}"

        holder.botonVer.setOnClickListener {
            onClick(paciente)
        }
    }

    override fun getItemCount(): Int = listaPacientes.size
}

