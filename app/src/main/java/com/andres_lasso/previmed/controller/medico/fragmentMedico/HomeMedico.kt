package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.FragmentHomeMedicoBinding

/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [HomeMedico.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeMedico : Fragment() {

    //binding para el fragment
    private var _binding: FragmentHomeMedicoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeMedicoBinding.inflate(inflater, container, false)
        return binding.root

    }
}