package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.andres_lasso.previmed.R
class VisitaBeneficiarioFragment : Fragment() {

    // Declaramos variables para los elementos del layout
    private lateinit var spinnerMedicos: Spinner
    private lateinit var checkMedico: CheckBox
    private lateinit var edDireccion: EditText
    private lateinit var btnSolicitar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflamos el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_visita_beneficiario, container, false)

        // Referenciamos los elementos de la interfaz con sus IDs
        spinnerMedicos = view.findViewById(R.id.spinnerMedicos)
        checkMedico = view.findViewById(R.id.checkMedico)
        edDireccion = view.findViewById(R.id.edDireccion)
        btnSolicitar = view.findViewById(R.id.btn_solicitar)

        // Lista de médicos para el Spinner
        val listaMedicos = listOf(
            "Seleccione un médico",
            "Dr. Alberto Lasso",
            "Dr. Pepe Perez",
            "Dra. Pepa Perez"
        )

        // Creamos un adaptador para mostrar la lista en el Spinner
        val adaptador = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaMedicos
        )
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedicos.adapter = adaptador

        // Por defecto el Spinner está apagado
        spinnerMedicos.isEnabled = false

        // Activamos o desactivamos el Spinner dependiendo del estado del CheckBox
        checkMedico.setOnCheckedChangeListener { _, isChecked ->
            spinnerMedicos.isEnabled = isChecked
        }


        btnSolicitar.setOnClickListener {
            val direccion = edDireccion.text.toString().trim()

            // Si la dirección está vacía mostramos un mensaje
            if (direccion.isEmpty()) {
                Toast.makeText(requireContext(), "La dirección es obligatoria", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si se seleccionó el checkbox, también validamos que haya elegido un médico válido
            if (checkMedico.isChecked && spinnerMedicos.selectedItemPosition == 0) {
                Toast.makeText(requireContext(), "Selecciona un médico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            Toast.makeText(requireContext(), "Solicitud enviada correctamente", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}