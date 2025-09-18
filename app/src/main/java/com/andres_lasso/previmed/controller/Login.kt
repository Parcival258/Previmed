package com.andres_lasso.previmed.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.ViewMedico
import com.andres_lasso.previmed.controller.asesor.ViewAsesor
import com.andres_lasso.previmed.controller.pacientes.ViewBeneficiario
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.LoginRequest
import com.andres_lasso.previmed.model.LoginResponse
import com.andres_lasso.previmed.utils.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.Normalizer

class Login : AppCompatActivity() {

    private lateinit var ctdocumento: EditText
    private lateinit var ctpassword: EditText
    private lateinit var btnLogin: Button

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
            val intent = Intent(this, destination).apply {
                putExtra("user_role", role)
            }
            startActivity(intent)
            finish()
        } else {
            PreferenceHelper.clearSession(this)
            Toast.makeText(this, "Rol no reconocido. Contacte al administrador.", Toast.LENGTH_LONG).show()
            Log.e("LOGIN", "Rol no reconocido: $role")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ctdocumento = findViewById(R.id.ctdocumento)
        ctpassword = findViewById(R.id.ctpassword)
        btnLogin = findViewById(R.id.loginButton)

        // 🚀 Solo entrar si hay token + rol válidos
        if (PreferenceHelper.hasToken(this)) {
            val savedRole = PreferenceHelper.getRole(this)
            if (!savedRole.isNullOrBlank()) {
                goToRoleActivity(normalizeRole(savedRole))
                return
            } else {
                PreferenceHelper.clearSession(this)
            }
        }

        btnLogin.setOnClickListener {
            val documento = ctdocumento.text.toString().trim()
            val password = ctpassword.text.toString().trim()

            if (documento.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = LoginRequest(numeroDocumento = documento, password = password)

            RetrofitClient.loginApi.loginUser(request)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null && body.jwt.isNotEmpty()) {
                                val roleNormalized = normalizeRole(body.data.rol.nombreRol)

                                PreferenceHelper.saveToken(this@Login, body.jwt)
                                PreferenceHelper.saveRole(this@Login, roleNormalized)

                                Toast.makeText(this@Login, body.message, Toast.LENGTH_LONG).show()

                                Log.d("API_LOGIN", "JWT: ${body.jwt}, UserId: ${body.data.id}, Documento: ${body.data.documento}, Rol normalizado: $roleNormalized")

                                goToRoleActivity(roleNormalized)
                            } else {
                                Toast.makeText(this@Login, "Error: no se recibió token", Toast.LENGTH_SHORT).show()
                                Log.e("API_LOGIN", "Respuesta sin JWT")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Toast.makeText(this@Login, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                            Log.e("API_LOGIN", "Error: ${response.code()} - $errorBody")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@Login, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("API_LOGIN", "Fallo conexión", t)
                    }
                })
        }
    }
}
