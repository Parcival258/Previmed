package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.Visita
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import com.andres_lasso.previmed.model.VisitaUpdateRequest
import com.andres_lasso.previmed.model.MedicoUpdateRequest

class VisitasPendientesAdapter(
    private val visitas: MutableList<Visita>,
    private val onVerClick: (Visita) -> Unit,
    private val onCancelarClick: (Visita) -> Unit,
    private val onActualizarLista: () -> Unit
) : RecyclerView.Adapter<VisitasPendientesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtPacienteNombre)
        val txtDireccion: TextView = view.findViewById(R.id.txtDireccion)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
        val txtBarrio: TextView = view.findViewById(R.id.txtBarrio)
        val txtTelefono: TextView = view.findViewById(R.id.txtTelefono)
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val txtEstado: TextView = view.findViewById(R.id.txtEstado)
        val btnVer: ImageView = view.findViewById(R.id.btnVer)
        val btnCancelar: ImageView = view.findViewById(R.id.btnCancelar)
        val btnIniciarFinalizar: ImageView = view.findViewById(R.id.btnIniciarFinalizar)
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
        holder.txtFecha.text = "🗓 ${visita.fechaVisita?.take(10) ?: "Sin fecha"}"
        holder.txtBarrio.text = "🏘 ${visita.barrio?.nombreBarrio ?: "Sin barrio"}"
        holder.txtTelefono.text = "☎️ ${visita.telefono ?: "No disponible"}"
        holder.txtDescripcion.text = "🧾 ${visita.descripcion ?: "Sin descripción"}"
        holder.txtEstado.text = "📋 ${if (visita.estado == true) "Activa" else "Inactiva"}"

        holder.btnVer.setOnClickListener {
            toggleDetalles(holder.cardDetalles)
            onVerClick(visita)
        }

        holder.btnCancelar.setOnClickListener { onCancelarClick(visita) }

        val prefs = holder.itemView.context.getSharedPreferences("visitas", Context.MODE_PRIVATE)
        val visitaActiva = prefs.getString("visita_activa", null)
        val esActiva = visitaActiva == visita.idVisita.toString()

        holder.btnIniciarFinalizar.setImageResource(
            if (esActiva) R.drawable.ic_stop else R.drawable.ic_play_arrow
        )

        holder.btnIniciarFinalizar.setOnClickListener {
            if (esActiva) {
                finalizarVisita(holder.itemView.context, visita, prefs)
            } else {
                iniciarVisita(holder.itemView.context, visita, prefs)
            }
        }
    }

    override fun getItemCount(): Int = visitas.size

    private fun iniciarVisita(context: Context, visita: Visita, prefs: android.content.SharedPreferences) {
        prefs.edit().putString("visita_activa", visita.idVisita.toString()).apply()
        PreferenceHelper.setVisitaActiva(context, true)

        // Enviar broadcast global
        val intent = Intent("ACTUALIZAR_ESTADO_MEDICO")
        context.sendBroadcast(intent)

        Toast.makeText(context, "Visita iniciada", Toast.LENGTH_SHORT).show()
        actualizarEstadoEnServidor(context, visita.idVisita ?: return, true)
        visita.medicoId?.let {
            actualizarEstadoMedico(context, it, disponibilidad = true, estado = false)
        }
    }

    private fun finalizarVisita(context: Context, visita: Visita, prefs: android.content.SharedPreferences) {
        prefs.edit().remove("visita_activa").apply()
        PreferenceHelper.setVisitaActiva(context, false)

        val intent = Intent("ACTUALIZAR_ESTADO_MEDICO")
        context.sendBroadcast(intent)

        Toast.makeText(context, "Visita finalizada", Toast.LENGTH_SHORT).show()
        actualizarEstadoEnServidor(context, visita.idVisita ?: return, false)
        visita.medicoId?.let {
            actualizarEstadoMedico(context, it, disponibilidad = true, estado = true)
        }

        visitas.remove(visita)
        notifyDataSetChanged()
        onActualizarLista()
    }

    private fun actualizarEstadoEnServidor(context: Context, idVisita: Int, estado: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val body = VisitaUpdateRequest(estado)
                val response = RetrofitClient.visitasApi.actualizarVisita(idVisita, body)
                if (response.isSuccessful) {
                    println("Estado visita actualizado correctamente.")
                } else {
                    println("Error al actualizar visita: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Error Retrofit: ${e.message}")
            }
        }
    }

    private fun actualizarEstadoMedico(context: Context, idMedico: Int, disponibilidad: Boolean, estado: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val body = MedicoUpdateRequest(disponibilidad, estado)
                val response = RetrofitClient.medicoApi.actualizarMedico(idMedico, body)
                if (response.isSuccessful) {
                    println("Médico actualizado correctamente.")
                } else {
                    println("Error al actualizar médico: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Error actualizando médico: ${e.message}")
            }
        }
    }

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
