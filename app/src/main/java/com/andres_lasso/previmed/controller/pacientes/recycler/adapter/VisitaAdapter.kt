package com.andres_lasso.previmed.controller.pacientes.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.Visita
import com.andres_lasso.previmed.utils.MedicoCache
import com.google.android.material.button.MaterialButton

class VisitaAdapter(
    private var visitas: List<Visita>,
    private val onCancelarClick: (Visita) -> Unit
) : RecyclerView.Adapter<VisitaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Encabezado
        val txtPacienteNombre: TextView = view.findViewById(R.id.txtPacienteNombre)
        val txtDireccion: TextView = view.findViewById(R.id.txtDireccion)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)

        // Cuadro azul de la fecha
        val txtDiaGrande: TextView = view.findViewById(R.id.txtDiaGrande)
        val txtMes: TextView = view.findViewById(R.id.txtMes)

        // Detalles expandibles
        val cardDetalles: LinearLayout = view.findViewById(R.id.cardDetalles)
        val txtBarrio: TextView = view.findViewById(R.id.txtBarrio)
        val txtTelefono: TextView = view.findViewById(R.id.txtTelefono)
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val txtEstado: TextView = view.findViewById(R.id.txtEstado)

        // Botones
        val btnVerDetalles: MaterialButton = view.findViewById(R.id.btnVerDetalles)
        val btnCancelar: ImageView = view.findViewById(R.id.btnCancelar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_visita_pendiente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val visita = visitas[position]

        // 🔹 Nombre del médico
        val nombreMedico = MedicoCache.getNombre(visita.medicoId ?: -1)
        holder.txtPacienteNombre.text = nombreMedico ?: "Médico desconocido"

        // 🔹 Dirección
        holder.txtDireccion.text = "📍 ${visita.direccion ?: "Dirección no disponible"}"

        // 🔹 Fecha: cuadro azul + texto completo
        val fechaCruda = visita.fechaVisita ?: ""   // ej: 2025-11-26T17:55:33.03932

        if (fechaCruda.length >= 10) {
            val anio = fechaCruda.substring(0, 4)
            val mesNumStr = fechaCruda.substring(5, 7)
            val diaStr = fechaCruda.substring(8, 10)

            val mesNum = mesNumStr.toIntOrNull() ?: 0
            val nombreMes = getNombreMes(mesNum)      // Ej: "Nov" o "Noviembre"

            // Línea con fecha completa
            holder.txtFecha.text = "🗓 $diaStr/$mesNumStr/$anio"

            // Cuadro azul
            holder.txtDiaGrande.text = diaStr
            holder.txtMes.text = nombreMes
        } else {
            holder.txtFecha.text = "🗓 Sin fecha"
            holder.txtDiaGrande.text = "--"
            holder.txtMes.text = ""
        }

        // 🔹 Detalles (igual que en el lado del médico)
        holder.txtBarrio.text = "🏘 ${visita.barrio?.nombreBarrio ?: "Sin barrio"}"
        holder.txtTelefono.text = "☎️ ${visita.telefono ?: "No disponible"}"
        holder.txtDescripcion.text = "🧾 ${visita.descripcion ?: "Sin descripción"}"
        holder.txtEstado.text = "📋 ${if (visita.estado == true) "Activa" else "Inactiva"}"

        // 🔹 Botón "Ver detalles" → expandir/ocultar
        holder.btnVerDetalles.setOnClickListener {
            toggleDetalles(holder.cardDetalles)
        }

        // También al tocar la card completa
        holder.itemView.setOnClickListener {
            toggleDetalles(holder.cardDetalles)
        }

        // 🔹 Botón cancelar
        holder.btnCancelar.setOnClickListener {
            onCancelarClick(visita)
        }
    }

    override fun getItemCount(): Int = visitas.size

    fun submitList(nuevaLista: List<Visita>) {
        visitas = nuevaLista
        notifyDataSetChanged()
    }

    // --- Helpers ---

    private fun getNombreMes(mes: Int): String {
        return when (mes) {
            1 -> "Ene"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Abr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Ago"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dic"
            else -> ""
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
