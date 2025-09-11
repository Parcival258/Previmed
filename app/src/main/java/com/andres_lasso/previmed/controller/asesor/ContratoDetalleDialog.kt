package com.andres_lasso.previmed.controller.asesor

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.Membresia

class ContratoDetalleDialog(
    // Aquí recibe el objeto con la información de la membresía que queremos mostrar
    private val membresia: Membresia
) : DialogFragment() {

    fun formatearFecha(fechaIso: String?): String {
        return fechaIso?.split("T")?.get(0) ?: ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Crear el constructor del diálogo
        val builder = AlertDialog.Builder(requireContext())

        // Inflar (cargar) el layout XML que define la apariencia del diálogo
        val view = requireActivity().layoutInflater.inflate(R.layout.activity_contrato_detalle_dialog, null)

        // Aquí se asignan los textos de la membresía a los campos de texto visibles del diálogo
        view.findViewById<TextView>(R.id.tvNumeroContrato).text = membresia.numeroContrato ?: ""
        view.findViewById<TextView>(R.id.tvFormaPago).text = membresia.formaPago ?: ""
        view.findViewById<TextView>(R.id.tvFechaInicio).text = formatearFecha(membresia.fechaInicio)
        view.findViewById<TextView>(R.id.tvFechaFin).text = formatearFecha(membresia.fechaFin)
        view.findViewById<TextView>(R.id.tvFirma).text = membresia.firma ?: ""

        // Muestra "Activo" o "Inactivo" según el estado de la membresía
        view.findViewById<TextView>(R.id.tvEstado).text = if (membresia.estado) "Activo" else "Inactivo"

        // Aquí se crea un texto con la información de los pacientes asociados, juntándola para mostrarla legible
        val pacientes = membresia.membresiaPaciente?.mapNotNull { mp ->
            mp.paciente?.usuario?.let {
                "Nombre: ${it.nombre} ${it.apellido}\nDocumento: ${it.numeroDocumento}\nEmail: ${it.email}"
            }
        }?.joinToString("\n\n") ?: "No hay pacientes asociados" // Si no hay pacientes, muestra este mensaje

        // Asignar ese texto a la vista que muestra los pacientes
        view.findViewById<TextView>(R.id.tvPacientesAsociados).text = pacientes

        // Configura el botón para descargar el PDF, que al pulsarlo:
        // - Crea un Intent para abrir la actividad del contrato
        // - Envía los datos necesarios para mostrar el contrato personalizado
        // - Cierra el diálogo
        view.findViewById<Button>(R.id.btnDescargarPdf).setOnClickListener {
            val intent = Intent(requireContext(), ContratoAsesorActivity::class.java).apply {
                putExtra("numeroContrato", membresia.numeroContrato)
                putExtra("formaPago", membresia.formaPago)
                putExtra("fechaInicio", membresia.fechaInicio)
                putExtra("fechaFin", membresia.fechaFin)
                putExtra("firma", membresia.firma)
                putExtra("estado", membresia.estado)
                putExtra("pacientesAsociados", pacientes)
            }
            startActivity(intent) // Abre la actividad para mostrar y descargar el PDF
            dismiss() // Cierra el diálogo actual
        }

        // Asocia el layout al diálogo y agrega un botón "Cerrar" para cerrar el diálogo sin acción
        builder.setView(view)
        builder.setNegativeButton("Cerrar") { dialog, _ -> dialog.dismiss() }

        // Finalmente, crea y devuelve el diálogo para que se muestre en pantalla
        return builder.create()
    }
}
