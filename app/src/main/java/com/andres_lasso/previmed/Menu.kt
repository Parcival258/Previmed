package com.andres_lasso.previmed

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andres_lasso.previmed.controller.medico.NavMedico
import com.andres_lasso.previmed.controller.pacientes.ViewBeneficiario

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        //Intento para ir a la vista beneficiario
        val irBene: Button = findViewById(R.id.irBeneficiario);
        irBene.setOnClickListener{
            val irBen = Intent(this, ViewBeneficiario::class.java)
            startActivity(irBen);
        }
        //ir a la vista medico
        findViewById<Button>(R.id.irMedico).setOnClickListener {
            startActivity(Intent(this, NavMedico::class.java))
        }
    }
}