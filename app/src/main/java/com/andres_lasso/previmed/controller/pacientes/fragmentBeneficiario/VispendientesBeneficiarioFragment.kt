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
import com.andres_lasso.previmed.model.Visita
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog

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
    fun actualizarVisitas(nuevaLista: List<Visita>) {
        adapter.submitList(nuevaLista)
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

        // Configurar ViewModel
        val visitaRepo = VisitaRepo(RetrofitClient.visitasApi)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                VisitaViewModel(visitaRepo) as T
        })[VisitaViewModel::class.java]

        // Configurar RecyclerView y Adapter
        adapter = VisitaAdapter(emptyList()) { visita ->
            showConfirmDialog(visita, idPaciente)
        }

        binding.rvVisitas.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVisitas.adapter = adapter

        // Cargar la caché de médicos antes de las visitas
        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.visitasApi.getMedicos()
                if (resp.isSuccessful) {
                    val lista = resp.body()?.data ?: emptyList()
                    MedicoCache.set(lista)
                    viewModel.obtenerVisitas(idPaciente)
                } else {
                    Toast.makeText(requireContext(), "Error al cargar médicos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Observar las visitas
        viewModel.visitas.observe(viewLifecycleOwner) { visitas ->
            adapter.submitList(visitas)
        }


    }

    private fun showConfirmDialog(visita: Visita, idPaciente: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cancelar visita")
            .setMessage("¿Deseas cancelar esta visita?")
            .setPositiveButton("Sí") { _, _ ->
                viewModel.cancelarVisita(visita.idVisita.toString(), idPaciente)
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
