package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.andres_lasso.previmed.R


class HomeBeneficiarioFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_beneficiario, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Saludo del beneficiario:
        val nombreBeneficiario:String = "Edinson"
        val saludo = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "¡Buenos días"
            in 12..17 -> "¡Buenas tardes"
            else -> "¡Buenas noches"
        }
        val saludoTextView:TextView = view.findViewById(R.id.saludo_beneficiario)
        saludoTextView.text = "$saludo, $nombreBeneficiario!"
        //Estado del servivio
        val estado: Boolean = true
        val estaServicio: TextView = view.findViewById(R.id.estado_servicio)
        val indicadorEstado: View = view.findViewById(R.id.indicador_estado)
        if (estado) {
            estaServicio.text = "Te encuentras activo"
            indicadorEstado.setBackgroundResource(R.drawable.circle_green)
        } else {
            estaServicio.text = "Te encuentras inactivo, comunícate"
            indicadorEstado.setBackgroundResource(R.drawable.circle_red)
        }

    }

}