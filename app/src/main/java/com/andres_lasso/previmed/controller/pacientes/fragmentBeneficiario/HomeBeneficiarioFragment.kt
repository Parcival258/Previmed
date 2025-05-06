package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.pacientes.Beneficiarios
import com.andres_lasso.previmed.databinding.FragmentHomeBeneficiarioBinding


class HomeBeneficiarioFragment : Fragment() {
    private var  _binding: FragmentHomeBeneficiarioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBeneficiarioBinding.inflate(inflater, container, false)
        return binding.root
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
        val saludoTextView:TextView = binding.saludoBeneficiario
        saludoTextView.text = "$saludo, $nombreBeneficiario!"
        //Estado del servivio
        val estado: Boolean = true
        val estaServicio: TextView = binding.estadoServicio
        val indicadorEstado: View = binding.indicadorEstado
        if (estado) {
            estaServicio.text = "Te encuentras activo"
            indicadorEstado.setBackgroundResource(R.drawable.circle_green)
        } else {
            estaServicio.text = "Te encuentras inactivo, comunícate"
            indicadorEstado.setBackgroundResource(R.drawable.circle_red)
        }

        val btnIrBene: Button = binding.irBeneficiarios

        btnIrBene.setOnClickListener {
            val irBenefi = Intent(requireContext(), Beneficiarios::class.java);
            startActivity(irBenefi);
            return@setOnClickListener;
        }
    }

}