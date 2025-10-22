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
import com.andres_lasso.previmed.RegistroUsuario
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.PacienteAseAdapter
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.ApiResponse
import com.andres_lasso.previmed.model.PacienteData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PacientesAsesorFragment : Fragment() {

    private lateinit var adapter: PacienteAseAdapter
    private val listaCompleta = mutableListOf<PacienteData>()
    private val listaFiltrada = mutableListOf<PacienteData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pacientes_asesor, container, false)

        val btnAgregarUsuario = view.findViewById<Button>(R.id.btn_agregarPacientess)
        btnAgregarUsuario.setOnClickListener {
            val intent = Intent(requireContext(), RegistroUsuario::class.java)
            startActivity(intent)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerPacientes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PacienteAseAdapter(listaFiltrada) { paciente ->
            val nombre = paciente.usuario?.nombre ?: "Desconocido"
            Toast.makeText(requireContext(), "Paciente: $nombre", Toast.LENGTH_SHORT).show()
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

        cargarPacientes()

        return view
    }

    private fun cargarPacientes() {
        RetrofitClient.pacienteApi.getPacientes()
            .enqueue(object : Callback<ApiResponse<List<PacienteData>>> {
                override fun onResponse(
                    call: Call<ApiResponse<List<PacienteData>>>,
                    response: Response<ApiResponse<List<PacienteData>>>
                ) {
                    if (response.isSuccessful) {
                        val pacientes = response.body()?.data ?: emptyList()
                        listaCompleta.clear()
                        listaCompleta.addAll(pacientes)
                        listaFiltrada.clear()
                        listaFiltrada.addAll(pacientes)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al cargar pacientes (${response.code()})",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<List<PacienteData>>>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun filtrarLista(texto: String?) {
        listaFiltrada.clear()
        if (texto.isNullOrEmpty()) {
            listaFiltrada.addAll(listaCompleta)
        } else {
            val filtro = texto.lowercase()
            val filtrados = listaCompleta.filter { paciente ->
                val usuario = paciente.usuario
                val nombreCompleto = "${usuario?.nombre ?: ""} ${usuario?.apellido ?: ""}".lowercase()
                val documento = usuario?.numeroDocumento?.lowercase() ?: ""
                nombreCompleto.contains(filtro) || documento.contains(filtro)
            }
            listaFiltrada.addAll(filtrados)
        }
        adapter.notifyDataSetChanged()
    }
}
