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

        Log.d("VISITA_FRAG", "🚀 Fragmento de visitas pendientes iniciado")

        // Obtener el ID del paciente desde preferencias
        val idPacienteStr = PreferenceHelper.getIdPaciente(requireContext())
        val idPaciente = idPacienteStr?.toIntOrNull() ?: 0

        if (idPaciente == 0) {
            Log.e("VISITA_FRAG", "❌ ID paciente inválido o no encontrado")
            Toast.makeText(requireContext(), "No hay paciente autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("VISITA_FRAG", "✅ ID paciente obtenido: $idPaciente")

        // Configurar ViewModel
        val visitaRepo = VisitaRepo(RetrofitClient.visitasApi)
        viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    VisitaViewModel(visitaRepo) as T
            }
        )[VisitaViewModel::class.java]

        // Configurar RecyclerView y Adapter
        adapter = VisitaAdapter(emptyList()) { visita ->
            val idMedicoSeguro = visita.medicoId ?: 0
            viewModel.cancelarVisita(visita.idVisita.toString(), idMedicoSeguro)
        }

        binding.rvVisitas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVisitas.adapter = adapter

        // Paso 1️⃣: Cargar la caché de médicos antes de las visitas
        lifecycleScope.launch {
            try {
                Log.d("VISITA_FRAG", "📡 Cargando médicos desde el servidor...")
                val resp = RetrofitClient.visitasApi.getMedicos()

                if (resp.isSuccessful) {
                    val lista = resp.body()?.data ?: emptyList()
                    MedicoCache.set(lista)
                    Log.d("VISITA_FRAG", "✅ Caché cargada con ${lista.size} médicos")

                    // Debug: mostrar los primeros médicos
                    lista.take(3).forEach { medico ->
                        Log.d(
                            "VISITA_FRAG",
                            "   Médico ID: ${medico.id_medico}, Nombre: ${medico.usuario?.nombre} ${medico.usuario?.apellido}"
                        )
                    }

                    // Paso 2️⃣: Obtener visitas del paciente
                    viewModel.obtenerVisitas(idPaciente)
                } else {
                    Log.e("VISITA_FRAG", "❌ Error al cargar médicos: ${resp.code()}")
                    Toast.makeText(requireContext(), "Error al cargar médicos", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Log.e("VISITA_FRAG", "❌ Excepción al cargar médicos: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Paso 3️⃣: Observar las visitas y actualizar el RecyclerView
        viewModel.visitas.observe(viewLifecycleOwner) { listaVisitas ->
            Log.d("VISITA_FRAG", "📦 ${listaVisitas.size} visitas recibidas")

            listaVisitas.forEach { visita ->
                val idMedico = visita.medicoId
                val nombreMedico = if (idMedico != null) {
                    MedicoCache.getNombre(idMedico)
                } else {
                    "Médico no asignado"
                }

                Log.d(
                    "VISITA_FRAG",
                    "   Visita ${visita.idVisita} → Médico ID: ${idMedico ?: "null"} → Nombre: $nombreMedico"
                )
            }

            adapter.submitList(listaVisitas)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
