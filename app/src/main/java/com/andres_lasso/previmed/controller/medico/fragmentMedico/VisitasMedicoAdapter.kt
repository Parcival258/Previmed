package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.R.attr.elevation
import android.R.attr.scaleX
import android.R.attr.text
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.MedicoUpdateRequest
import com.andres_lasso.previmed.model.Visita
import com.andres_lasso.previmed.model.VisitaUpdateRequest
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class VisitasMedicoAdapter(
    private val visitas: MutableList<Visita>,
    private val onVerClick: (Visita) -> Unit,
    private val onActualizarLista: () -> Unit
) : RecyclerView.Adapter<VisitasMedicoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtPacienteNombre)
        val txtDireccion: TextView = view.findViewById(R.id.txtDireccion)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
        val txtDiaGrande: TextView = view.findViewById(R.id.txtDiaGrande)
        val txtMes: TextView = view.findViewById(R.id.txtMes)
        val txtBarrio: TextView = view.findViewById(R.id.txtBarrio)
        val txtTelefono: TextView = view.findViewById(R.id.txtTelefono)
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val txtEstado: TextView = view.findViewById(R.id.txtEstado)
        val btnVerDetalles: MaterialButton = view.findViewById(R.id.btnVerDetalles)
        val btnIniciarFinalizar: MaterialButton = view.findViewById(R.id.btnIniciarFinalizar)
        val cardDetalles: LinearLayout = view.findViewById(R.id.cardDetalles)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_visita_pendiente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val visita = visitas[position]

        // 🔹 Datos del paciente
        holder.txtNombre.text = toTitleCase(getNombreSeguro(visita))
        holder.txtDireccion.text = "📍 ${visita.direccion ?: "Sin dirección"}"

        // 🔹 Fecha: cuadro azul + texto completo
        val fechaCruda = visita.fechaVisita ?: ""   // ej: 2025-11-26T17:55:33.03932

        if (fechaCruda.length >= 10) {
            val anio = fechaCruda.substring(0, 4)
            val mesNumStr = fechaCruda.substring(5, 7)
            val diaStr = fechaCruda.substring(8, 10)

            val mesNum = mesNumStr.toIntOrNull() ?: 0
            val nombreMes = getNombreMes(mesNum)

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

        holder.txtBarrio.text = "🏘 ${visita.barrio?.nombreBarrio ?: "Sin barrio"}"
        holder.txtTelefono.text = "☎️ ${visita.telefono ?: "N/A"}"
        holder.txtDescripcion.text = "🧾 ${visita.descripcion ?: "Sin descripción"}"
        holder.txtEstado.text = "📋 Activa"

        // 🔹 Obtener preferencias
        val prefs = holder.itemView.context.getSharedPreferences("visitas", Context.MODE_PRIVATE)
        val visitaActivaId = prefs.getString("visita_activa", null)
        val esActiva = visitaActivaId == visita.idVisita.toString()

        // 🔹 Configurar visibilidad de botones
        holder.btnIniciarFinalizar.visibility = View.VISIBLE  // Mostrar iniciar/finalizar

        // 🔹 Configurar estado del botón Iniciar/Finalizar
        configurarBotonEstado(holder, esActiva)

        // 🔹 Click en Ver Detalles
        holder.btnVerDetalles.setOnClickListener {
            toggleDetallesAnimado(holder.cardDetalles)
            onVerClick(visita)
        }

        // 🔹 Click en Iniciar/Finalizar Visita
        holder.btnIniciarFinalizar.setOnClickListener {
            if (esActiva) {
                finalizarVisita(holder.itemView.context, visita, prefs, holder.adapterPosition)
            } else {
                iniciarVisita(holder.itemView.context, visita, prefs, holder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = visitas.size

    // 🔹 Configurar aspecto del botón según estado
    private fun configurarBotonEstado(holder: ViewHolder, esActiva: Boolean) {
        if (esActiva) {
            holder.btnIniciarFinalizar.setIcon(holder.itemView.context.getDrawable(R.drawable.ic_stop))
            holder.btnIniciarFinalizar.setIconTintResource(android.R.color.white)
            holder.btnIniciarFinalizar.setBackgroundColor(holder.itemView.context.getColor(android.R.color.holo_green_light))
            holder.btnIniciarFinalizar.text = "Finalizar"
        } else {
            holder.btnIniciarFinalizar.setIcon(holder.itemView.context.getDrawable(R.drawable.ic_play_arrow))
            holder.btnIniciarFinalizar.setIconTintResource(R.color.AZUL_GENERAL)
            holder.btnIniciarFinalizar.setBackgroundColor(holder.itemView.context.getColor(android.R.color.white))
        }
    }

    // 🔹 Iniciar Visita
    private fun iniciarVisita(context: Context, visita: Visita, prefs: android.content.SharedPreferences, position: Int) {
        prefs.edit().putString("visita_activa", visita.idVisita.toString()).apply()
        PreferenceHelper.setVisitaActiva(context, true)
        context.sendBroadcast(Intent("ACTUALIZAR_ESTADO_MEDICO"))
        Toast.makeText(context, "✅ Visita iniciada", Toast.LENGTH_SHORT).show()

        actualizarEstadoVisita(visita.idVisita!!, true)
        actualizarEstadoMedico(visita.medicoId!!, disponibilidad = true, estado = false)

        notifyItemChanged(position)
    }

    // 🔹 Finalizar Visita
    private fun finalizarVisita(context: Context, visita: Visita, prefs: android.content.SharedPreferences, position: Int) {
        prefs.edit().remove("visita_activa").apply()
        PreferenceHelper.setVisitaActiva(context, false)
        context.sendBroadcast(Intent("ACTUALIZAR_ESTADO_MEDICO"))
        Toast.makeText(context, "✅ Visita finalizada", Toast.LENGTH_SHORT).show()

        actualizarEstadoVisita(visita.idVisita!!, false)
        actualizarEstadoMedico(visita.medicoId!!, disponibilidad = true, estado = true)

        visitas.removeAt(position)
        notifyItemRemoved(position)
        onActualizarLista()
    }

    // 🔹 Actualizar estado de la visita en el servidor
    private fun actualizarEstadoVisita(idVisita: Int, estado: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitClient.visitasApi.actualizarVisita(idVisita, VisitaUpdateRequest(estado))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 🔹 Actualizar estado del médico en el servidor
    private fun actualizarEstadoMedico(idMedico: Int, disponibilidad: Boolean, estado: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitClient.medicoApi.actualizarMedico(idMedico, MedicoUpdateRequest(disponibilidad, estado))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 🔹 Obtener nombre del mes
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

    // 🔹 Obtener nombre seguro del paciente
    private fun getNombreSeguro(visita: Visita): String {
        val usuario = visita.paciente?.usuario
        return if (!usuario?.nombre.isNullOrBlank() || !usuario?.apellido.isNullOrBlank())
            "${usuario?.nombre ?: ""} ${usuario?.apellido ?: ""}".trim()
        else "Paciente #${visita.pacienteId}"
    }

    // 🔹 Convertir a Title Case
    private fun toTitleCase(text: String): String {
        return text.lowercase(Locale.getDefault()).split(" ").joinToString(" ") {
            it.replaceFirstChar { char -> char.uppercase() }
        }
    }

    // 🔹 Toggle detalles con animación
    private fun toggleDetallesAnimado(view: View) {
        if (view.visibility == View.GONE) {
            view.visibility = View.VISIBLE
            val anim = AlphaAnimation(0f, 1f)
            anim.duration = 300
            view.startAnimation(anim)
        } else {
            val anim = AlphaAnimation(1f, 0f)
            anim.duration = 200
            view.startAnimation(anim)
            view.visibility = View.GONE
        }
    }
}