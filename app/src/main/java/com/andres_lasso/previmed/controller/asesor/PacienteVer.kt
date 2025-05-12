package com.andres_lasso.previmed.controller.asesor

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andres_lasso.previmed.R

class PacienteVer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paciente_ver)
        val button: Button = findViewById<Button>(R.id.botonContrato)
    }
    private fun toast(){
        Toast.makeText(this,"Toast", Toast.LENGTH_SHORT).show()
    }
}
