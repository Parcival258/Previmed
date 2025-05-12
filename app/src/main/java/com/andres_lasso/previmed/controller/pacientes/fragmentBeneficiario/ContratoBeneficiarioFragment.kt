package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.FragmentContratoBeneficiarioBinding


class ContratoBeneficiarioFragment : Fragment() {

    private var _binding: FragmentContratoBeneficiarioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentContratoBeneficiarioBinding.inflate(inflater, container, false)
        return binding.root
    }

}