package com.andres_lasso.previmed.controller.asesor

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andres_lasso.previmed.R


class AgregarPaciente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_paciente)
        val button:Button = findViewById<Button>(R.id.botonAgPaciente)
    }
    private fun toast(){
        Toast.makeText(this,"Toast",Toast.LENGTH_SHORT).show()
    }
}