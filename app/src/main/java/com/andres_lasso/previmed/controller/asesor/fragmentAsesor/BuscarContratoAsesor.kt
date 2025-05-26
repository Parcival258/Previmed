package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.ContratoAsesorActivity
import com.andres_lasso.previmed.model.Persona
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.PersonaAdapter

class BuscarContratoAsesor : AppCompatActivity() {

    private val listaPersonasOriginal = listOf(
        Persona("Daniela Fernanda Herrera Usuga", "1061715858"),
        Persona("Marlio Hernan Cañar Rosero", "1061786160")
    )

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PersonaAdapter
    private var personaSeleccionada: Persona? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_contrato_asesor)

        recyclerView = findViewById(R.id.recycler_personas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PersonaAdapter(
            listaPersonasOriginal,
            onItemClick = { persona ->
                personaSeleccionada = persona
                adapter.setPersonaSeleccionada(persona)
                abrirContrato(persona)  // Abrir contrato al click en nombre o cédula
            },
            onDownloadClick = { persona ->
                abrirContrato(persona, descargar = true) // Descargar contrato con ícono
            }
        )

        recyclerView.adapter = adapter
        adapter.actualizarLista(listaPersonasOriginal)

        findViewById<Button>(R.id.btn_ver_contrato).setOnClickListener {
            personaSeleccionada?.let { abrirContrato(it) }
        }

        findViewById<Button>(R.id.btn_imprimir_contrato).setOnClickListener {
            personaSeleccionada?.let { abrirContrato(it, imprimir = true) }
        }

        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtro = newText?.lowercase()?.trim() ?: ""
                val resultado = listaPersonasOriginal.filter {
                    it.nombre.lowercase().contains(filtro) || it.cedula.contains(filtro)
                }
                adapter.actualizarLista(resultado)
                personaSeleccionada = null
                return true
            }
        })

        personaSeleccionada = listaPersonasOriginal.firstOrNull()
        personaSeleccionada?.let {
            adapter.setPersonaSeleccionada(it)
        }
    }

    private fun abrirContrato(persona: Persona, imprimir: Boolean = false, descargar: Boolean = false) {
        val intent = Intent(this, ContratoAsesorActivity::class.java).apply {
            putExtra("modoContrato", "personalizado")
            putExtra("nombre", persona.nombre)
            putExtra("cedula", persona.cedula)
            putExtra("imprimir", imprimir)
            putExtra("descargar", descargar)
        }
        startActivity(intent)
    }
}
