package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.andres_lasso.previmed.controller.pacientes.recycler.adapter.*
import com.andres_lasso.previmed.databinding.FragmentVispendientesBeneficiarioBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.utils.PreferenceHelper

class VispendientesBeneficiarioFragment : Fragment() {

    private var _binding: FragmentVispendientesBeneficiarioBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VisitaViewModel
    private lateinit var adapter: VisitaAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentVispendientesBeneficiarioBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("VISITA_FRAG", "🚀 Fragmento VispendientesBeneficiarioFragment creado")

        val idPacienteStr = PreferenceHelper.getIdPaciente(requireContext())
        Log.d("VISITA_FRAG", "Valor crudo de PreferenceHelper: '$idPacienteStr'")

        val idPaciente = idPacienteStr?.toIntOrNull() ?: 0
        Log.d("VISITA_FRAG", "ID paciente convertido: $idPaciente")

        if (idPaciente == 0) {
            Log.e("VISITA_FRAG", "❌ ID paciente es 0 o inválido")
            Toast.makeText(requireContext(), "No hay paciente autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("VISITA_FRAG", "✅ ID paciente válido: $idPaciente")

        val visitaRepo = VisitaRepo(RetrofitClient.visitas)
        viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T = VisitaViewModel(visitaRepo) as T
            }
        )[VisitaViewModel::class.java]

        adapter = VisitaAdapter(emptyList()) { visita ->
            viewModel.cancelarVisita(visita.idVisita.toString(), idPaciente)
        }

        binding.rvVisitas.adapter = adapter
        binding.rvVisitas.layoutManager = LinearLayoutManager(requireContext())

        viewModel.visitas.observe(viewLifecycleOwner) { listaVisitas ->
            Log.d("VISITA_FRAG", "📦 Visitas recibidas: ${listaVisitas.size}")
            adapter.submitList(listaVisitas)
        }

        Log.d("VISITA_FRAG", "📡 Llamando a viewModel.obtenerVisitas($idPaciente)")
        viewModel.obtenerVisitas(idPaciente)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}