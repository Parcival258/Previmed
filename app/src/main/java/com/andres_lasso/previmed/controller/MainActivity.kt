package com.andres_lasso.previmed.controller

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.RegistroUsuario
import com.andres_lasso.previmed.ViewMedico
import com.andres_lasso.previmed.controller.asesor.ViewAsesor
import com.andres_lasso.previmed.controller.pacientes.ViewBeneficiario
import com.andres_lasso.previmed.utils.PreferenceHelper
import java.text.Normalizer

class MainActivity : AppCompatActivity() {

    private fun normalizeRole(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        val normalized = Normalizer.normalize(raw, Normalizer.Form.NFD)
        return normalized.replace("\\p{Mn}+".toRegex(), "").lowercase()
    }

    private fun goToRoleActivity(role: String) {
        val destination = when (role) {
            "beneficiario", "paciente" -> ViewBeneficiario::class.java
            "medico" -> ViewMedico::class.java
            "asesor" -> ViewAsesor::class.java
            else -> null
        }

        if (destination != null) {
            startActivity(Intent(this, destination))
        } else {
            PreferenceHelper.clearSession(this)
            startActivity(Intent(this, Login::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            val token = PreferenceHelper.getToken(this)
            val role = normalizeRole(PreferenceHelper.getRole(this))

            if (!token.isNullOrBlank() && role.isNotBlank()) {
                goToRoleActivity(role)
            } else {
                PreferenceHelper.clearSession(this)
                startActivity(Intent(this, RegistroUsuario::class.java))
            }

            finish()
        }, 2000)
    }
}
