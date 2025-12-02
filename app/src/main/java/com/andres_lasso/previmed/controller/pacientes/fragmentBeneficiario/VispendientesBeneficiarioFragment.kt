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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andres_lasso.previmed.controller.pacientes.recycler.adapter.VisitaAdapter
import com.andres_lasso.previmed.controller.pacientes.recycler.adapter.VisitaRepo
import com.andres_lasso.previmed.controller.pacientes.recycler.adapter.VisitaViewModel
import com.andres_lasso.previmed.databinding.FragmentVispendientesBeneficiarioBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.utils.MedicoCache
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.launch

class VispendientesBeneficiarioFragment : Fragment() {

    private var _binding: FragmentVispendientesBeneficiarioBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VisitaViewModel
    private lateinit var adapter: VisitaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVispendientesBeneficiarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("VISITA_FRAG", "Fragmento iniciado correctamente")

        // 🔹 Obtener ID del paciente logueado
        val idPaciente = PreferenceHelper.getIdPaciente(requireContext())?.toIntOrNull() ?: 0
        if (idPaciente == 0) {
            Toast.makeText(requireContext(), "❌ No hay paciente autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        // 🔹 Configurar el ViewModel
        val visitaRepo = VisitaRepo(RetrofitClient.visitasApi)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                VisitaViewModel(visitaRepo) as T
        })[VisitaViewModel::class.java]

        // 🔹 Configurar RecyclerView
        adapter = VisitaAdapter(emptyList())

        binding.rvVisitas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVisitas.adapter = adapter

        // 🔹 Cargar médicos primero, luego visitas
        lifecycleScope.launch {
            try {
                val respuesta = RetrofitClient.visitasApi.getMedicos()
                if (respuesta.isSuccessful) {
                    MedicoCache.set(respuesta.body()?.data ?: emptyList())
                    Log.d("VISITA_FRAG", "✅ Médicos cargados en cache")
                    viewModel.obtenerVisitas(idPaciente)
                } else {
                    Toast.makeText(requireContext(), "❌ Error al obtener médicos", Toast.LENGTH_SHORT).show()
                    Log.e("VISITA_FRAG", "Error HTTP: ${respuesta.code()}")
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "❌ Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("VISITA_FRAG", "🚨 Error al cargar médicos", e)
            }
        }

        // 🔹 Observar cambios en las visitas
        viewModel.visitas.observe(viewLifecycleOwner) { visitas ->
            if (visitas.isEmpty()) {
                Toast.makeText(requireContext(), "✅ No hay visitas pendientes", Toast.LENGTH_SHORT).show()
            }
            adapter.submitList(visitas)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}