package com.andres_lasso.previmed.controller.asesor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.HomeAsesorFragment

class AgregarPaciente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_paciente)

        val button: Button = findViewById(R.id.botonAgPaciente)
        button.setOnClickListener {
            val intent = Intent(this, HomeAsesorFragment::class.java)
            startActivity(intent)
        }
    }
}
