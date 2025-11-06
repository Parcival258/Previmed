package com.andres_lasso.previmed.controller.medico.fragmentMedico

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

class VisitasPendientesAdapter(
    private val visitas: List<Visita>,
    private val onVerClick: (Visita) -> Unit,
    private val onCancelarClick: (Visita) -> Unit
) : RecyclerView.Adapter<VisitasPendientesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtPacienteNombre)
        val txtDireccion: TextView = view.findViewById(R.id.txtDireccion)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
        val txtBarrio: TextView = view.findViewById(R.id.txtBarrio)
        val txtTelefono: TextView = view.findViewById(R.id.txtTelefono)
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val btnCancelar: ImageView = view.findViewById(R.id.btnCancelar)
        val cardDetalles: LinearLayout = view.findViewById(R.id.cardDetalles)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_visita_pendiente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val visita = visitas[position]

        // 👤 Nombre completo del paciente (con null safety)
        val usuario = visita.paciente?.usuario
        val nombrePaciente = when {
            !usuario?.nombre.isNullOrBlank() && !usuario?.apellido.isNullOrBlank() ->
                "${usuario?.nombre} ${usuario?.apellido}".trim()

            !usuario?.nombre.isNullOrBlank() ->
                usuario?.nombre ?: "Paciente"

            !usuario?.apellido.isNullOrBlank() ->
                usuario?.apellido ?: "Paciente"

            else ->
                "Paciente #${visita.pacienteId ?: visita.paciente?.idPaciente ?: "Desconocido"}"
        }

        // 🩺 Datos principales
        holder.txtNombre.text = nombrePaciente
        holder.txtDireccion.text = "📍 ${visita.direccion ?: "Sin dirección"}"
        holder.txtFecha.text = "🗓 ${visita.fechaVisita?.take(10) ?: "Sin fecha"}"

        // 🧾 Detalles
        holder.txtBarrio.text = "🏘 ${visita.barrio?.nombreBarrio ?: "Sin barrio"}"
        holder.txtTelefono.text = "☎️ ${visita.telefono ?: "No disponible"}"
        holder.txtDescripcion.text = "🧾 ${visita.descripcion ?: "Sin descripción"}"


        // ❌ Cancelar visita
        holder.btnCancelar.setOnClickListener {
            onCancelarClick(visita)
        }
    }

    override fun getItemCount(): Int = visitas.size
}
