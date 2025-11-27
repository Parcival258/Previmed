package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.Visita
import com.google.android.material.button.MaterialButton
import java.util.Locale

class VisitasPendientesAdapter(
    private val visitas: MutableList<Visita>,
    private val onVerClick: (Visita) -> Unit
) : RecyclerView.Adapter<VisitasPendientesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtPacienteNombre)
        val txtDireccion: TextView = view.findViewById(R.id.txtDireccion)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)          // texto pequeño
        val txtDiaGrande: TextView = view.findViewById(R.id.txtDiaGrande)  // número en el cuadro azul
        val txtBarrio: TextView = view.findViewById(R.id.txtBarrio)
        val txtTelefono: TextView = view.findViewById(R.id.txtTelefono)
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val txtEstado: TextView = view.findViewById(R.id.txtEstado)
        val btnVerDetalles: MaterialButton = view.findViewById(R.id.btnVerDetalles)
        val cardDetalles: LinearLayout = view.findViewById(R.id.cardDetalles)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_visita_pendiente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val visita = visitas[position]

        val nombreCompleto = toTitleCase(getNombreSeguro(visita))
        holder.txtNombre.text = nombreCompleto
        holder.txtDireccion.text = "📍 ${visita.direccion ?: "Sin dirección"}"

        // ---- FECHA: día grande + fecha completa ----
        val fechaCruda = visita.fechaVisita ?: ""           // ej: 2025-11-26T17:55:33.03932
        val soloFecha = if (fechaCruda.length >= 10) {
            fechaCruda.substring(0, 10)                     // 2025-11-26
        } else {
            "Sin fecha"
        }
        val diaNumero = if (fechaCruda.length >= 10) {
            fechaCruda.substring(8, 10)                     // 26
        } else {
            "--"
        }

        holder.txtFecha.text = "📅 $soloFecha"              // texto debajo del nombre
        holder.txtDiaGrande.text = diaNumero               // cuadro azul

        holder.txtBarrio.text = "🏘 ${visita.barrio?.nombreBarrio ?: "Sin barrio"}"
        holder.txtTelefono.text = "☎️ ${visita.telefono ?: "No disponible"}"
        holder.txtDescripcion.text = "🧾 ${visita.descripcion ?: "Sin descripción"}"
        holder.txtEstado.text = "📋 ${if (visita.estado == true) "Activa" else "Inactiva"}"

        // Botón "Ver detalles": expande/colapsa y notifica al fragment
        holder.btnVerDetalles.setOnClickListener {
            toggleDetalles(holder.cardDetalles)
            onVerClick(visita)
        }

        // También al tocar la card
        holder.itemView.setOnClickListener {
            toggleDetalles(holder.cardDetalles)
        }
    }

    override fun getItemCount(): Int = visitas.size

    fun actualizarDatos(nuevasVisitas: List<Visita>) {
        visitas.clear()
        visitas.addAll(nuevasVisitas)
        notifyDataSetChanged()
    }

    // --- Helpers ---

    private fun getNombreSeguro(visita: Visita): String {
        val paciente = visita.paciente
        val usuario = paciente?.usuario
        if (!usuario?.nombre.isNullOrBlank() || !usuario?.apellido.isNullOrBlank()) {
            return listOfNotNull(usuario?.nombre, usuario?.apellido)
                .filter { it.isNotBlank() }
                .joinToString(" ")
        }
        return "Paciente #${visita.pacienteId ?: paciente?.idPaciente ?: "Desconocido"}"
    }

    private fun toTitleCase(raw: String): String {
        val norm = raw.trim().lowercase(Locale.getDefault()).replace("\\s+".toRegex(), " ")
        return norm.split(" ").joinToString(" ") { part ->
            part.replaceFirstChar { c ->
                if (c.isLowerCase()) c.titlecase(Locale.getDefault()) else c.toString()
            }
        }
    }

    private fun toggleDetalles(view: View) {
        if (view.visibility == View.VISIBLE) {
            val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 200 }
            view.startAnimation(fadeOut)
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
            val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 200 }
            view.startAnimation(fadeIn)
        }
    }
}
