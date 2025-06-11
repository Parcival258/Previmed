package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.FragmentHomeMedicoBinding

class HomeMedico : Fragment(R.layout.fragment_home_medico) {

    private var _binding: FragmentHomeMedicoBinding? = null
    private val binding get() = _binding!!

    private enum class EstadoMedico(
        val texto: String,
        val colorTexto: Int,
        val drawableBoton: Int,
        val drawableIndicador: Int
    ) {
        DISPONIBLE(
            "Disponible",
            R.color.green,
            R.drawable.btn_disponible,
            R.drawable.circle_disponible
        ),
        OCUPADO(
            "Ocupado",
            R.color.naranja,
            R.drawable.btn_ocupado,
            R.drawable.circle_ocupado
        ),
        NO_DISPONIBLE(
            "No Disponible",
            R.color.red,
            R.drawable.btn_no_disponible,
            R.drawable.circle_no_disponible
        );

        fun siguiente() = values()[(ordinal + 1) % values().size]
    }

    private var estadoActual = EstadoMedico.DISPONIBLE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeMedicoBinding.bind(view)

        binding.btnEstado.setOnClickListener {
            cambiarEstado()
        }

        // Inicializar con estado por defecto
        actualizarUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cambiarEstado() {
        estadoActual = estadoActual.siguiente()
        actualizarUI()
    }

    private fun actualizarUI() {
        binding.apply {
            btnEstado.apply {
                text = "Estado: ${estadoActual.texto}"
                setBackgroundResource(estadoActual.drawableBoton)
            }

            txtEstadoActual.apply {
                text = estadoActual.texto
                setTextColor(ContextCompat.getColor(requireContext(), estadoActual.colorTexto))
            }

            indicadorEstado.setBackgroundResource(estadoActual.drawableIndicador)
        }
    }
}