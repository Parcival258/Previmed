package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.FragmentPagosBeneficiarioBinding

class PagosBeneficiarioFragment : Fragment() {
    private var _binding: FragmentPagosBeneficiarioBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPagosBeneficiarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val contrato: TextView = view.findViewById(R.id.numero_contrato);
        val contrato: TextView = binding.contrato

        val numeroContrato: String = "25-94985-554"
        contrato.text = "Membresía $numeroContrato"


    }
}