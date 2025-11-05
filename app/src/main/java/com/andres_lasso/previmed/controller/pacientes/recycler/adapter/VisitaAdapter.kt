package com.andres_lasso.previmed.controller.pacientes.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.Visita
import com.andres_lasso.previmed.utils.MedicoCache

class VisitaAdapter(
    private var visitas: List<Visita>,
    private val onCancelarClick: (Visita) -> Unit
) : RecyclerView.Adapter<VisitaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtPacienteNombre: TextView = view.findViewById(R.id.txtPacienteNombre)
        val txtDireccion: TextView = view.findViewById(R.id.txtDireccion)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
        val btnCancelar: ImageView = view.findViewById(R.id.btnCancelar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_visita_pendiente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val visita = visitas[position]

        // Mostrar el nombre del médico (si existe)
        val nombreMedico = MedicoCache.getNombre(visita.medicoId ?: -1)
        holder.txtPacienteNombre.text = nombreMedico ?: "Médico desconocido"

        // Dirección y fecha
        holder.txtDireccion.text = "📍 ${visita.direccion ?: "Dirección no disponible"}"
        holder.txtFecha.text = "🗓 ${visita.fechaVisita?.take(10) ?: "Sin fecha"}"

        // Botón para cancelar
        holder.btnCancelar.setOnClickListener { onCancelarClick(visita) }
    }

    override fun getItemCount(): Int = visitas.size

    fun submitList(nuevaLista: List<Visita>) {
        visitas = nuevaLista
        notifyDataSetChanged()
    }
}
