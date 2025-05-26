package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.ContratoAsesorActivity

data class Persona(val cedula: String, val nombre: String)

class BuscarContratoAsesor : AppCompatActivity() {

    private val personaDaniela = Persona("1061715858", "Daniela Fernanda Herrera Usuga")
    private val personaMarlio = Persona("1061786160", "Marlio Hernan Cañar Rosero")

    private lateinit var btnVerContrato: Button
    private lateinit var btnImprimirContrato: Button

    private var personaSeleccionada: Persona? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_contrato_asesor)

        btnVerContrato = findViewById(R.id.btn_ver_contrato)
        btnImprimirContrato = findViewById(R.id.btn_imprimir_contrato)

        val cardDaniela = findViewById<View>(R.id.card_daniela)
        val nombreDaniela = findViewById<View>(R.id.nombre_daniela)
        val cedulaDaniela = findViewById<View>(R.id.cedula_daniela)

        val cardMarlio = findViewById<View>(R.id.card_marlio)
        val nombreMarlio = findViewById<View>(R.id.nombre_marlio)
        val cedulaMarlio = findViewById<View>(R.id.cedula_marlio)

        // Función para manejar la selección y lanzamiento de impresión al hacer click en la tarjeta
        fun seleccionarYImprimir(persona: Persona, tarjeta: View) {
            personaSeleccionada = persona
            marcarSeleccion(tarjeta)
            // Lanzar impresión inmediatamente al tocar la tarjeta
            val intent = Intent(this, ContratoAsesorActivity::class.java).apply {
                putExtra("nombre", persona.nombre)
                putExtra("cedula", persona.cedula)
                putExtra("imprimir", true) // indica que debe imprimir
            }
            startActivity(intent)
        }

        // Click listeners para tarjetas - selecciona y lanza impresión
        val clickDaniela = View.OnClickListener {
            seleccionarYImprimir(personaDaniela, cardDaniela)
        }
        val clickMarlio = View.OnClickListener {
            seleccionarYImprimir(personaMarlio, cardMarlio)
        }

        // Asignar listeners a tarjeta y sus textos
        cardDaniela.setOnClickListener(clickDaniela)
        nombreDaniela.setOnClickListener(clickDaniela)
        cedulaDaniela.setOnClickListener(clickDaniela)

        cardMarlio.setOnClickListener(clickMarlio)
        nombreMarlio.setOnClickListener(clickMarlio)
        cedulaMarlio.setOnClickListener(clickMarlio)

        // Botón Ver Contrato: abre actividad para ver contrato (sin imprimir)
        btnVerContrato.setOnClickListener {
            personaSeleccionada?.let { persona ->
                val intent = Intent(this, ContratoAsesorActivity::class.java).apply {
                    putExtra("nombre", persona.nombre)
                    putExtra("cedula", persona.cedula)
                }
                startActivity(intent)
            }
        }

        // Botón Imprimir Contrato: imprime contrato de persona seleccionada
        btnImprimirContrato.setOnClickListener {
            personaSeleccionada?.let { persona ->
                val intent = Intent(this, ContratoAsesorActivity::class.java).apply {
                    putExtra("nombre", persona.nombre)
                    putExtra("cedula", persona.cedula)
                    putExtra("imprimir", true)
                }
                startActivity(intent)
            }
        }

        // Inicializar selección por defecto en Daniela
        personaSeleccionada = personaDaniela
        marcarSeleccion(cardDaniela)
    }

    private fun marcarSeleccion(cardViewSeleccionado: View) {
        val cardDaniela = findViewById<View>(R.id.card_daniela)
        val cardMarlio = findViewById<View>(R.id.card_marlio)

        cardDaniela.isSelected = false
        cardMarlio.isSelected = false

        cardViewSeleccionado.isSelected = true
    }
}
