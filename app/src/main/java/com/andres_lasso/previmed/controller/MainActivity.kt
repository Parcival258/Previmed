package com.andres_lasso.previmed.controller

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andres_lasso.previmed.Menu
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.RegistroPaciente
import com.andres_lasso.previmed.interfaces.ApiService
import com.andres_lasso.previmed.utils.PreferenceHelper

class   MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ocultar barra superior (status bar) para que parezca pantalla splash completa
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Esperar 2 segundos mostrando el logo
        Handler(Looper.getMainLooper()).postDelayed({
            val token = PreferenceHelper.getToken(this)

            if (token != null) {
                // Si hay sesión activa, mandar al menú principal
                startActivity(Intent(this, Menu::class.java))
            } else {
                // Si no hay token, ir al login
                startActivity(Intent(this, Login::class.java))
            }

            finish()
        }, 2000)
    }
}