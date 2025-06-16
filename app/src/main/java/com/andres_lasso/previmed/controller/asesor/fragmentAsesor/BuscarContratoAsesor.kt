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
    )//Define una lista fija de objetos Persona que contiene nombre y cédula de dos personas. Es la lista original sin filtrar

    private lateinit var recyclerView: RecyclerView//la vista donde se mostrarán las personas.
    private lateinit var adapter: PersonaAdapter// el adaptador personalizado que conecta los datos con el RecyclerView.
    private var personaSeleccionada: Persona? = null//guarda temporalmente la persona que el usuario selecciona.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_contrato_asesor)

        recyclerView = findViewById(R.id.recycler_personas)//Se busca el RecyclerView desde el XML
        recyclerView.layoutManager = LinearLayoutManager(this)//se le asigna un LinearLayoutManager para mostrar la lista en forma vertical.

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
        adapter.actualizarLista(listaPersonasOriginal)//Se asigna el adaptador al RecyclerView y se muestra inicialmente toda la lista original

        // Buscamos el botón con ID 'btn_ver_contrato' en el layout y le asignamos un evento de clic
        findViewById<Button>(R.id.btn_ver_contrato).setOnClickListener {
            // Si 'personaSeleccionada' no es nulo, se ejecuta el bloque 'let'
            // 'it' representa el valor de 'personaSeleccionada'
            personaSeleccionada?.let {
                // Llamamos a la función 'abrirContrato' y le pasamos la persona seleccionada
                abrirContrato(it)
            }
        }


        findViewById<Button>(R.id.btn_imprimir_contrato).setOnClickListener {// Buscamos el botón con ID 'btn_imprimir_contrato' en el layout y le asignamos un evento de clic

            personaSeleccionada?.let {// Llamamos a la función 'abrirContrato' pasando la persona y un parámetro adicional 'imprimir = true'
                // Esto puede usarse para indicar que se debe mostrar directamente la opción de imprimir el contrato
                abrirContrato(it, imprimir = true) }
        }

        val searchView = findViewById<SearchView>(R.id.search_view)// Obtenemos la referencia al SearchView del layout
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {// Configuramos un listener que se activará cada vez que se escriba texto en el SearchView
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
