package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.Membresia

class MembresiaAdapter(
    private var listaMembresias: List<Membresia>,
    private val onClick: (Membresia) -> Unit
) : RecyclerView.Adapter<MembresiaAdapter.MembresiaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembresiaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_membresia, parent, false)
        return MembresiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MembresiaViewHolder, position: Int) {
        val membresia = listaMembresias[position]
        holder.bind(membresia)
        holder.itemView.setOnClickListener { onClick(membresia) }
    }

    override fun getItemCount(): Int = listaMembresias.size

    @SuppressLint("NotifyDataSetChanged")
    fun actualizarLista(nuevaLista: List<Membresia>) {
        listaMembresias = nuevaLista
        notifyDataSetChanged()
    }

    class MembresiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNumeroContrato: TextView = itemView.findViewById(R.id.tvNumeroContrato)
        private val tvNombrePaciente: TextView = itemView.findViewById(R.id.tvNombrePaciente)
        private val tvDocumento: TextView = itemView.findViewById(R.id.tvDocumento)
        private val tvFormaPago: TextView = itemView.findViewById(R.id.tvFormaPago)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)

        @SuppressLint("SetTextI18n")
        fun bind(membresia: Membresia) {
            tvNumeroContrato.text = "Contrato: ${membresia.numeroContrato ?: "---"}"
            tvFormaPago.text = "Forma de pago: ${membresia.formaPago ?: "---"}"
            tvEstado.text = if (membresia.estado == true) "Activo" else "Inactivo"
            tvEstado.setTextColor(
                itemView.context.getColor(
                    if (membresia.estado == true) android.R.color.holo_green_dark
                    else android.R.color.holo_red_dark
                )
            )

            val paciente = membresia.membresiaPaciente?.firstOrNull()?.paciente?.usuario
            tvNombrePaciente.text = "Paciente: ${paciente?.nombre ?: "---"} ${paciente?.apellido ?: ""}"
            tvDocumento.text = "Documento: ${paciente?.numeroDocumento ?: "---"}"
        }
    }
}
