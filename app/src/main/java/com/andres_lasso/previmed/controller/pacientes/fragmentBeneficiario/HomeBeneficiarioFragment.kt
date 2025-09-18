package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.Login
import com.andres_lasso.previmed.controller.pacientes.Beneficiarios
import com.andres_lasso.previmed.databinding.FragmentHomeBeneficiarioBinding
import com.andres_lasso.previmed.utils.PreferenceHelper

class HomeBeneficiarioFragment : Fragment() {
    private var _binding: FragmentHomeBeneficiarioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBeneficiarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ===== Saludo del beneficiario =====
        val nombreBeneficiario = "Edinson"
        val saludo = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "¡Buenos días"
            in 12..17 -> "¡Buenas tardes"
            else -> "¡Buenas noches"
        }
        binding.saludoBeneficiario.text = "$saludo, $nombreBeneficiario!"

        // ===== Estado del servicio =====
        val estado = true
        if (estado) {
            binding.estadoServicio.text = "Te encuentras activo"
            binding.indicadorEstado.setBackgroundResource(R.drawable.circle_green)
        } else {
            binding.estadoServicio.text = "Te encuentras inactivo, comunícate"
            binding.indicadorEstado.setBackgroundResource(R.drawable.circle_red)
        }

        // ===== Botón Mis Beneficiarios =====
        binding.irBeneficiarios.setOnClickListener {
            val intent = Intent(requireContext(), Beneficiarios::class.java)
            startActivity(intent)
        }

        // ===== Botón Cerrar Sesión =====
        binding.btnLogout.setOnClickListener {
            try {
                // Limpiar sesión completa
                PreferenceHelper.clearSession(requireContext())

                // Mensaje de confirmación
                Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()

                // Abrir Login y limpiar stack
                val intent = Intent(requireContext(), Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
