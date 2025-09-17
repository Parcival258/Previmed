package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.Login
import com.andres_lasso.previmed.utils.PreferenceHelper

class HomeMedicoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_medico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("HomeMedicoFragment", "onViewCreated ejecutado")

        // ===== Botón Cerrar Sesión =====
        val btnLogout = view.findViewById<AppCompatButton>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            Log.d("HomeMedicoFragment", "Botón Cerrar Sesión clickeado")
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
                Log.e("HomeMedicoFragment", "Error al cerrar sesión", e)
                Toast.makeText(requireContext(), "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
            }
        }

        // ===== Botón Cambiar Estado =====
        val btnEstado = view.findViewById<AppCompatButton>(R.id.btn_estado)
        btnEstado.setOnClickListener {
            Log.d("HomeMedicoFragment", "Botón Cambiar Estado clickeado")
            Toast.makeText(requireContext(), "Cambiar estado clickeado", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeMedicoFragment().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }
}
