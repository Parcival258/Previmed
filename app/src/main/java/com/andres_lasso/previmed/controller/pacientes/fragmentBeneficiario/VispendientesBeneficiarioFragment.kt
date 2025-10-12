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
import com.andres_lasso.previmed.controller.pacientes.recycler.adapter.*
import com.andres_lasso.previmed.databinding.FragmentVispendientesBeneficiarioBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.utils.PreferenceHelper
import com.andres_lasso.previmed.utils.MedicoCache
import kotlinx.coroutines.launch

class VispendientesBeneficiarioFragment : Fragment() {

    private var _binding: FragmentVispendientesBeneficiarioBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VisitaViewModel
    private lateinit var adapter: VisitaAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentVispendientesBeneficiarioBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("VISITA_FRAG", "🚀 Fragmento iniciado")

        val idPacienteStr = PreferenceHelper.getIdPaciente(requireContext())
        val idPaciente = idPacienteStr?.toIntOrNull() ?: 0

        if (idPaciente == 0) {
            Log.e("VISITA_FRAG", "❌ ID paciente inválido")
            Toast.makeText(requireContext(), "No hay paciente autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("VISITA_FRAG", "✅ ID paciente: $idPaciente")

        // Configurar ViewModel
        val visitaRepo = VisitaRepo(RetrofitClient.visitas)
        viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T = VisitaViewModel(visitaRepo) as T
            }
        )[VisitaViewModel::class.java]

        // Configurar Adapter
        adapter = VisitaAdapter(emptyList()) { visita ->
            viewModel.cancelarVisita(visita.idVisita.toString(), idPaciente)
        }

        binding.rvVisitas.adapter = adapter
        binding.rvVisitas.layoutManager = LinearLayoutManager(requireContext())

        // PASO 1: Cargar caché de médicos PRIMERO
        lifecycleScope.launch {
            try {
                Log.d("VISITA_FRAG", "📡 Cargando médicos...")
                val resp = RetrofitClient.visitas.getMedicos()

                if (resp.isSuccessful) {
                    val lista = resp.body()?.data ?: emptyList()
                    MedicoCache.set(lista)
                    Log.d("VISITA_FRAG", "✅ Caché cargada con ${lista.size} médicos")

                    // Debug: mostrar algunos médicos
                    lista.take(3).forEach { medico ->
                        Log.d("VISITA_FRAG", "   ID: ${medico.idMedico}, Nombre: ${medico.usuario?.nombre} ${medico.usuario?.apellido}")
                    }

                    // PASO 2: DESPUÉS de cargar caché, obtener visitas
                    viewModel.obtenerVisitas(idPaciente)
                } else {
                    Log.e("VISITA_FRAG", "❌ Error al cargar médicos: ${resp.code()}")
                    Toast.makeText(requireContext(), "Error al cargar médicos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("VISITA_FRAG", "❌ Excepción: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // PASO 3: Observar las visitas
        viewModel.visitas.observe(viewLifecycleOwner) { listaVisitas ->
            Log.d("VISITA_FRAG", "📦 ${listaVisitas.size} visitas recibidas")

            // Debug: verificar que se obtienen los nombres
            listaVisitas.forEach { visita ->
                val nombre = MedicoCache.getNombre(visita.medicoId)
                Log.d("VISITA_FRAG", "   Visita ${visita.idVisita} - Médico ID ${visita.medicoId} = $nombre")
            }

            adapter.submitList(listaVisitas)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}