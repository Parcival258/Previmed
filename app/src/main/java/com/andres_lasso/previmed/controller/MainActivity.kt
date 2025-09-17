package com.andres_lasso.previmed.controller

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.controller.paciente.SolicitarVisitaActivity
import com.andres_lasso.previmed.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Abrir SolicitarVisitaActivity después de 2 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, SolicitarVisitaActivity::class.java))
            finish()
        }, 2000)
    }
}
