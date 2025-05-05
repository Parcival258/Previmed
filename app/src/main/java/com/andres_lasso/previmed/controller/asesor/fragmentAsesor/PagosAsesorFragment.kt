package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.PagosAdd
import com.andres_lasso.previmed.databinding.FragmentPagosAsesorBinding

class PagosAsesorFragment : Fragment() {
    private var _binding:FragmentPagosAsesorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPagosAsesorBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnIrPagosAdd.setOnClickListener {
            val ir_pagos_add = Intent(requireContext(), PagosAdd::class.java)
            startActivity(ir_pagos_add)
        }
    }

}