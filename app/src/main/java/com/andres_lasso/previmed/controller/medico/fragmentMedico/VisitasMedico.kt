package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.andres_lasso.previmed.controller.medico.adapter.VisitasPendientesAdapter
import com.andres_lasso.previmed.controller.medico.visitasPendientes
import com.andres_lasso.previmed.databinding.FragmentVisitasMedicoBinding

class VisitasMedico : Fragment() {

    private var _binding: FragmentVisitasMedicoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VisitasPendientesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitasMedicoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRecyclerView()
    }

    private fun setupUI() {
        // Configurar el texto del usuario (puedes obtenerlo de SharedPreferences o ViewModel)
        binding.username.text = "Hola, Dr. Médico"

        // Mostrar el número de pacientes restantes basado en los datos temporales
        binding.pacientesRestantesCount.text = visitasPendientes.size.toString()
    }

    private fun setupRecyclerView() {
        // datos del provider
        adapter = VisitasPendientesAdapter(visitasPendientes)

        binding.recyclerViewVisitas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@VisitasMedico.adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}