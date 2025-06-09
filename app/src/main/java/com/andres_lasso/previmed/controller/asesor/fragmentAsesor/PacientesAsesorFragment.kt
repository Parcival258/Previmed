package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.AgregarPaciente
import com.andres_lasso.previmed.controller.asesor.recycler.PacienteClass
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.PacienteAseAdapter


class PacientesAsesorFragment : Fragment() {

    private lateinit var adapter: PacienteAseAdapter
    private val listaCompleta = mutableListOf<PacienteClass>()
    private val listaFiltrada = mutableListOf<PacienteClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pacientes_asesor, container, false)

        listaCompleta.addAll(listOf(
            PacienteClass("Alejandro Campo Mendoza", "25736897", "Popayán-Cauca", "Individual"),
            PacienteClass("María Pérez", "12345678", "Cali-Valle", "Familiar"),
            PacienteClass("Carlos López", "87654321", "Bogotá", "Empresarial")
        ))

        listaFiltrada.addAll(listaCompleta)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerPacientes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PacienteAseAdapter(listaFiltrada) { paciente ->
            Toast.makeText(requireContext(), "Paciente: ${paciente.idNombre}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        val searchView = view.findViewById<SearchView>(R.id.btn_Buscar_Usu)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarLista(newText)
                return true
            }
        })

        // 🔽 Aquí conectamos el botón al activity
        val botonAgregar = view.findViewById<Button>(R.id.BotonAgUsuario)
        botonAgregar.setOnClickListener {
            val intent = Intent(requireContext(), AgregarPaciente::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun filtrarLista(texto: String?) {
        listaFiltrada.clear()
        if (texto.isNullOrEmpty()) {
            listaFiltrada.addAll(listaCompleta)
        } else {
            val filtro = texto.lowercase()
            val filtrados = listaCompleta.filter {
                it.idNombre.lowercase().contains(filtro) ||
                        it.docPaciente.contains(filtro) ||
                        it.direccionPaciente.lowercase().contains(filtro) ||
                        it.planPaciente.lowercase().contains(filtro)
            }
            listaFiltrada.addAll(filtrados)
        }
        adapter.notifyDataSetChanged()
    }
}
