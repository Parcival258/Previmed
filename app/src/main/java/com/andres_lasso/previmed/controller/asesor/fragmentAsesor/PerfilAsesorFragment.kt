package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.controller.Login
import com.andres_lasso.previmed.controller.asesor.Barrios
import com.andres_lasso.previmed.controller.asesor.PlanesView
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.BuscarContratoAsesor
import com.andres_lasso.previmed.databinding.FragmentPerfilAsesorBinding
import com.andres_lasso.previmed.utils.PreferenceHelper

class PerfilAsesorFragment : Fragment() {

    private var _binding: FragmentPerfilAsesorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilAsesorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ---- Botón PLANES ----
        binding.btnPlanesPerfil.setOnClickListener {
            startActivity(Intent(requireContext(), PlanesView::class.java))
        }

        // ---- Botón BARRIOS ----
        binding.btnBarriosPerfil.setOnClickListener {
            startActivity(Intent(requireContext(), Barrios::class.java))
        }

        // ---- Botón CONTRATOS ----
        binding.btnContratosPerfil.setOnClickListener {
            startActivity(Intent(requireContext(), BuscarContratoAsesor::class.java))
        }

        // ---- CERRAR SESIÓN ----
        binding.btnLogoutPerfil.setOnClickListener {
            PreferenceHelper.clearSessionButKeepBiometric(requireContext())

            val intent = Intent(requireContext(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
