package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.Persona

class PersonaAdapter(
    private var listaPersonas: List<Persona>,
    private val onItemClick: (Persona) -> Unit,
    private val onDownloadClick: (Persona) -> Unit
) : RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder>() {

    private var personaSeleccionada: Persona? = null

    fun actualizarLista(nuevaLista: List<Persona>) {
        listaPersonas = nuevaLista
        notifyDataSetChanged()
    }

    fun setPersonaSeleccionada(persona: Persona) {
        personaSeleccionada = persona
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_persona, parent, false)
        return PersonaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonaViewHolder, position: Int) {
        val persona = listaPersonas[position]
        holder.bind(persona, persona == personaSeleccionada)
    }

    override fun getItemCount() = listaPersonas.size

    inner class PersonaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvCedula: TextView = itemView.findViewById(R.id.tvCedula)
        private val ivDownload: ImageView = itemView.findViewById(R.id.ivDownload)

        fun bind(persona: Persona, estaSeleccionada: Boolean) {
            tvNombre.text = persona.nombre
            tvCedula.text = persona.cedula


            // Click en nombre o cédula
            itemView.setOnClickListener {
                onItemClick(persona)
            }

            // Click en icono de descarga
            ivDownload.setOnClickListener {
                onDownloadClick(persona)
            }
        }
    }
}
