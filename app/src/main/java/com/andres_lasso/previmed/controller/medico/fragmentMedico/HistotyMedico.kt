package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andres_lasso.previmed.controller.medico.adapter.VisitsHistoryAdapter
import com.andres_lasso.previmed.databinding.FragmentHistotyMedicoBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.Paciente
import com.andres_lasso.previmed.model.Visita
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.launch

class HistotyMedico : Fragment() {

    private var _binding: FragmentHistotyMedicoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistotyMedicoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listHistory.layoutManager = LinearLayoutManager(requireContext())

        val idMedico = PreferenceHelper.getIdMedico(requireContext())
        if (idMedico != null && idMedico != -1) {
            obtenerHistorialVisitas(idMedico)
        } else {
            Toast.makeText(requireContext(), "⚠️ No se encontró ID del médico", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerHistorialVisitas(idMedico: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.visitasApi.getVisitasPorMedico(idMedico)
                Log.d("HistotyMedico", "📡 Código: ${response.code()} - Mensaje: ${response.message()}")

                if (response.isSuccessful) {
                    val visitas = response.body()?.msj ?: emptyList()
                    val historial = visitas.filter { it.estado == false } // Solo las finalizadas

                    if (historial.isEmpty()) {
                        Toast.makeText(requireContext(), "No hay historial de visitas", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val historialCompleto = mutableListOf<Visita>()

                    // 🔍 Si el paciente no trae usuario, hacemos la llamada para obtenerlo
                    for (visita in historial) {
                        if (visita.paciente?.usuario == null && visita.pacienteId != null) {
                            val paciente = obtenerPacientePorId(visita.pacienteId)
                            if (paciente != null) {
                                historialCompleto.add(visita.copy(paciente = paciente))
                            } else {
                                historialCompleto.add(visita)
                            }
                        } else {
                            historialCompleto.add(visita)
                        }
                    }

                    Log.d("HistotyMedico", "📜 Historial cargado: ${historialCompleto.size} visitas")
                    binding.listHistory.adapter = VisitsHistoryAdapter(historialCompleto)
                    binding.totalPacientesCount.text = historialCompleto.size.toString()

                } else {
                    Toast.makeText(requireContext(), "❌ Error al cargar historial", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("HistotyMedico", "🚨 Error al obtener historial: ${e.message}", e)
                Toast.makeText(requireContext(), "Error al obtener historial", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun obtenerPacientePorId(id: Int): Paciente? {
        return try {
            val response = RetrofitClient.pacienteApi.getPacienteById(id)
            if (response.isSuccessful) {
                val paciente = response.body()?.data
                Log.d("HistotyMedico", "🧍 Paciente cargado: ${paciente?.usuario?.nombre} ${paciente?.usuario?.apellido}")
                paciente
            } else {
                Log.e("HistotyMedico", "❌ No se pudo obtener paciente $id")
                null
            }
        } catch (e: Exception) {
            Log.e("HistotyMedico", "🚨 Error al obtener paciente $id: ${e.message}")
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
