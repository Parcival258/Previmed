package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.Barrios
import com.andres_lasso.previmed.controller.asesor.PlanesView
import com.andres_lasso.previmed.databinding.ActivityBuscarContratoAsesorBinding
import com.andres_lasso.previmed.databinding.ActivityPlanesViewBinding
import com.andres_lasso.previmed.databinding.FragmentHomeAsesorBinding

class HomeAsesorFragment : Fragment() {
    private var _binding: FragmentHomeAsesorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeAsesorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPlanes.setOnClickListener {
            val ir_planes = Intent(requireContext(), PlanesView::class.java)
            startActivity(ir_planes)
        }

        binding.btnBarrios.setOnClickListener {
            val ir_barrios = Intent(requireContext(), Barrios::class.java)
            startActivity(ir_barrios)
        }

        binding.btnContratos.setOnClickListener {
            val ir_contratos = Intent(requireContext(), BuscarContratoAsesor::class.java)
            startActivity(ir_contratos)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
