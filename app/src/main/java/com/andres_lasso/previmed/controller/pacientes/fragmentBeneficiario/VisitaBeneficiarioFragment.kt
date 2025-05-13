package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.andres_lasso.previmed.databinding.FragmentVisitaBeneficiarioBinding

class VisitaBeneficiarioFragment : Fragment() {

    private var _binding: FragmentVisitaBeneficiarioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVisitaBeneficiarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lista de médicos
        val listaMedicos = listOf(
            "Seleccione un médico",
            "Dr. Alberto Lasso",
            "Dr. Pepe Perez",
            "Dra. Pepa Perez"
        )

        // Adaptador para el Spinner
        val adaptador = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaMedicos
        )
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMedicos.adapter = adaptador

        // Por defecto, el spinner está deshabilitado
        binding.spinnerMedicos.isEnabled = false

        // Activar/desactivar spinner según el checkbox
        binding.checkMedico.setOnCheckedChangeListener { _, isChecked ->
            binding.spinnerMedicos.isEnabled = isChecked
        }

        // Botón solicitar
        binding.btnSolicitar.setOnClickListener {
            val direccion = binding.edDireccion.text.toString().trim()

            if (direccion.isEmpty()) {
                Toast.makeText(requireContext(), "La dirección es obligatoria", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.checkMedico.isChecked && binding.spinnerMedicos.selectedItemPosition == 0) {
                Toast.makeText(requireContext(), "Selecciona un médico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(requireContext(), "Solicitud enviada correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
