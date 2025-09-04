package com.andres_lasso.previmed

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andres_lasso.previmed.controller.Login
import com.andres_lasso.previmed.controller.asesor.ViewAsesor
import com.andres_lasso.previmed.controller.pacientes.ViewBeneficiario
import com.andres_lasso.previmed.utils.PreferenceHelper

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        //Botones de navegación
        val irBene: Button = findViewById(R.id.irBeneficiario);
        val irAsesor: Button = findViewById(R.id.irAsesor);
        val irMedico: Button = findViewById(R.id.irMedico)
        val btnLogout: Button = findViewById(R.id.btnLogout)


        irBene.setOnClickListener{
            val irBen = Intent(this, ViewBeneficiario::class.java)
            startActivity(irBen);
        }
        //ir a la vista medico
        irMedico.setOnClickListener {
            startActivity(Intent(this, ViewMedico::class.java))
        }

        // ir a la vista de asesor
        irAsesor.setOnClickListener{
            val ir = Intent(this, ViewAsesor::class.java)
            startActivity(ir);
        }

        btnLogout.setOnClickListener {
            PreferenceHelper.clearToken(this) // Borra el JWT
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish() // Cierra el menú para que no se pueda volver con "atrás"
        }



    }
}