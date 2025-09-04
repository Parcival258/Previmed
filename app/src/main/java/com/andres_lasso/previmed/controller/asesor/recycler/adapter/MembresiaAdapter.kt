package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.Membresia

class MembresiaAdapter(
    // Lista de membresías que se mostrará en la lista
    private var lista: List<Membresia>,
    // Función que se ejecuta cuando el usuario toca una membresía en la lista
    private val onMembresiaClick: (Membresia) -> Unit
) : RecyclerView.Adapter<MembresiaAdapter.ViewHolder>() {

    // ViewHolder contiene las vistas de cada fila o celda de la lista
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Referencias a los textos que muestran nombre y número de contrato en la fila
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvNumeroContrato: TextView = itemView.findViewById(R.id.tvNumeroContrato)

        init {
            // Cuando el usuario toca dicha fila se llama la función para manejar ese evento
            itemView.setOnClickListener {
                val position = adapterPosition
                // Verificamos que la posición sea válida
                if (position != RecyclerView.NO_POSITION) {
                    // Ejecutamos la acción pasada en el constructor con la membresía seleccionada
                    onMembresiaClick(lista[position])
                }
            }
        }
    }

    // Se crea la vista de cada fila usando el layout XML 'item_membresia'
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_membresia, parent, false)
        return ViewHolder(view)
    }

    // Devuelve la cantidad de elementos para mostrar en la lista
    override fun getItemCount() = lista.size

    // Aquí se asignan los datos reales a cada fila para mostrar los nombres y números
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val membresia = lista[position]
        // Se unen todos los nombres completos de pacientes de la membresía en un solo texto
        val nombres = membresia.membresiaPaciente?.mapNotNull { mp ->
            val u = mp.paciente?.usuario
            listOf(u?.nombre, u?.segundoNombre, u?.apellido, u?.segundoApellido)
                .filterNotNull().joinToString(" ")
        }?.joinToString(", ") ?: "Sin pacientes" // Si no hay pacientes muestra este texto
        holder.tvNombre.text = nombres

        // Muestra el número de contrato o "N/A" si no existe
        holder.tvNumeroContrato.text = membresia.numeroContrato ?: "N/A"
    }

    // Función para actualizar la lista completa y refrescar la vista
    fun actualizarLista(nuevaLista: List<Membresia>) {
        lista = nuevaLista
        notifyDataSetChanged() // Indica al RecyclerView que actualice todo
    }
}
