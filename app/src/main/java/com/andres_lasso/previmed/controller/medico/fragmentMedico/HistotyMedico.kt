package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.FragmentHistotyMedicoBinding


class HistotyMedico : Fragment() {

    //binding para el fragment
    private var _binding: FragmentHistotyMedicoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistotyMedicoBinding.inflate(inflater,  container, false)
        return binding.root
    }
}