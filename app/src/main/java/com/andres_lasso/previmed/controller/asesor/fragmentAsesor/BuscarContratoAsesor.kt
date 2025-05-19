package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.andres_lasso.previmed.R

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnVerContrato = findViewById(R.id.btn_ver_contrato)
        btnImprimirContrato = findViewById(R.id.btn_imprimir_contrato)

        personaSeleccionada = personaDaniela // Selección por defecto

        // Selección de tarjetas
        findViewById<View>(R.id.nombre_daniela).setOnClickListener {
            personaSeleccionada = personaDaniela
        }

        findViewById<View>(R.id.nombre_marlio).setOnClickListener {
            personaSeleccionada = personaMarlio
        }

        // Acción del botón VER CONTRATO
        btnVerContrato.setOnClickListener {
            personaSeleccionada?.let { persona ->
                val fragment = ContratoAsesorFragment().apply {
                    arguments = Bundle().apply {
                        putString("nombre", persona.nombre)
                        putString("cedula", persona.cedula)
                    }
                }

                supportFragmentManager.commit {
                    replace(R.id.fragment_container_local, fragment)
                }
            }
        }

        // Acción del botón IMPRIMIR CONTRATO (opcional)
        btnImprimirContrato.setOnClickListener {
            // Implementación futura si aplica
        }
    }
}
