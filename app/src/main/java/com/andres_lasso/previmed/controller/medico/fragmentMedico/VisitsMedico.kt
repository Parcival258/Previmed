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
import com.andres_lasso.previmed.databinding.FragmentVisitsMedicoBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.Paciente
import com.andres_lasso.previmed.model.Visita
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.launch

class VisitsMedico : Fragment() {

    private var _binding: FragmentVisitsMedicoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitsMedicoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerVisitas.layoutManager = LinearLayoutManager(requireContext())

        val idMedico = PreferenceHelper.getIdMedico(requireContext())
        if (idMedico != null && idMedico != -1) {
            obtenerVisitasPendientes(idMedico)
        } else {
            Toast.makeText(
                requireContext(),
                "⚠️ No se encontró ID del médico en preferencias",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun obtenerVisitasPendientes(idMedico: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.visitasApi.getVisitasPorMedico(idMedico)

                if (!isAdded) return@launch

                if (response.isSuccessful) {
                    val visitasPendientes = response.body()
                        ?.msj
                        ?.filter { it.estado == true }
                        ?: emptyList()

                    if (visitasPendientes.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "No hay visitas pendientes",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.recyclerVisitas.adapter = null
                        return@launch
                    }

                    val visitasCompletas = mutableListOf<Visita>()

                    for (visita in visitasPendientes) {
                        // Si no trae info de usuario del paciente, la completamos
                        if (visita.paciente?.usuario == null && visita.pacienteId != null) {
                            val paciente = obtenerPacientePorId(visita.pacienteId)
                            if (paciente != null) {
                                visitasCompletas.add(visita.copy(paciente = paciente))
                            } else {
                                visitasCompletas.add(visita)
                            }
                        } else {
                            visitasCompletas.add(visita)
                        }
                    }

                    binding.recyclerVisitas.adapter = VisitasPendientesAdapter(
                        visitas = visitasCompletas.toMutableList(),
                        onVerClick = { visita -> verVisita(visita) }
                    )

                } else {
                    Toast.makeText(
                        requireContext(),
                        "❌ Error al obtener visitas",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("VisitsMedico", "Error código HTTP: ${response.code()}")
                }

            } catch (e: Exception) {
                if (!isAdded) return@launch
                Log.e("VisitsMedico", "🚨 Error al obtener visitas: ${e.message}", e)
                Toast.makeText(
                    requireContext(),
                    "Error de conexión al obtener visitas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun obtenerPacientePorId(id: Int): Paciente? {
        return try {
            val response = RetrofitClient.pacienteApi.getPacienteById(id)
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                Log.e("VisitsMedico", "Error HTTP al obtener paciente: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("VisitsMedico", "🚨 Error al obtener paciente: ${e.message}", e)
            null
        }
    }

    private fun verVisita(visita: Visita) {
        val nombre = visita.paciente?.usuario?.let {
            "${it.nombre.orEmpty()} ${it.apellido.orEmpty()}".trim()
        } ?: "Paciente #${visita.pacienteId ?: "N/A"}"

        val barrio = visita.barrio?.nombreBarrio ?: "Sin barrio"
        val fecha = visita.fechaVisita
            ?.take(10)
            ?.split("-")
            ?.reversed()
            ?.joinToString("/")
            ?: "Sin fecha"

        val msg = """
            👤 Paciente: $nombre
            🧾 Descripción: ${visita.descripcion ?: "Sin descripción"}
            📍 Dirección: ${visita.direccion ?: "Sin dirección"}
            🏘 Barrio: $barrio
            📅 Fecha: $fecha
            ☎️ Teléfono: ${visita.telefono ?: "N/A"}
        """.trimIndent()

        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
