package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.Membresia

class MembresiaAdapter(
    private var lista: List<Membresia>,
    private val onItemClick: (Membresia) -> Unit
) : RecyclerView.Adapter<MembresiaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumeroContrato: TextView = view.findViewById(R.id.tvNumeroContrato)
        val tvFormaPago: TextView = view.findViewById(R.id.tvFormaPago)
        val tvNombrePaciente: TextView = view.findViewById(R.id.tvNombrePaciente)
        val tvDocumento: TextView = view.findViewById(R.id.tvDocumento)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(lista[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_membresia, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        val pacienteNombre = item.membresiaPaciente
            ?.firstOrNull()?.paciente?.usuario?.let { u ->
                listOfNotNull(u.nombre, u.segundoNombre, u.apellido, u.segundoApellido)
                    .joinToString(" ")
            } ?: "Sin asignar"

        val documento = item.membresiaPaciente
            ?.firstOrNull()?.paciente?.usuario?.numeroDocumento ?: "N/A"

        holder.tvNumeroContrato.text = "Contrato: ${item.numeroContrato ?: "N/A"}"
        holder.tvFormaPago.text = "Pago: ${item.formaPago ?: "N/A"}"
        holder.tvNombrePaciente.text = "Paciente: $pacienteNombre"
        holder.tvDocumento.text = "Documento: $documento"
        holder.tvEstado.text = if (item.estado == true) "Activo" else "Inactivo"
    }

    fun actualizarLista(nuevaLista: List<Membresia>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
