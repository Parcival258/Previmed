package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.FragmentHomeAsesorBinding

class HomeAsesorFragment : Fragment() {

    private var _binding: FragmentHomeAsesorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeAsesorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 👉 AL TOCAR EL BOTÓN TE LLEVA A LA VISTA DE PAGOS
        binding.btnPendientes.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.asesor_nav, PagosAsesorFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
