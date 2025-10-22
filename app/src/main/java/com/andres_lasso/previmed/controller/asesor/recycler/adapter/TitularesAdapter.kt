package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.PacienteData

class TitularesAdapter(
    private var listaPacientes: List<PacienteData>,
    private val onItemClick: (PacienteData) -> Unit
) : RecyclerView.Adapter<TitularesAdapter.TitularViewHolder>() {

    inner class TitularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvDocumento: TextView = itemView.findViewById(R.id.tvDocumento)

        fun bind(paciente: PacienteData) {
            val usuario = paciente.usuario
            val nombre = usuario?.nombre ?: ""
            val apellido = usuario?.apellido ?: ""
            val numeroDocumento = usuario?.numeroDocumento ?: ""

            // Mostrar nombre completo y documento
            tvNombre.text = "$nombre $apellido"
            tvDocumento.text = "Documento: $numeroDocumento"

            itemView.setOnClickListener { onItemClick(paciente) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitularViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_titular, parent, false)
        return TitularViewHolder(view)
    }

    override fun onBindViewHolder(holder: TitularViewHolder, position: Int) {
        holder.bind(listaPacientes[position])
    }

    override fun getItemCount(): Int = listaPacientes.size

    // 🔹 Método público para actualizar la lista
    fun actualizarLista(nuevaLista: List<PacienteData>) {
        listaPacientes = nuevaLista
        notifyDataSetChanged()
    }
}
