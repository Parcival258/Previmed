package com.andres_lasso.previmed.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.ViewMedico
import com.andres_lasso.previmed.controller.asesor.ViewAsesor
import com.andres_lasso.previmed.controller.pacientes.ViewBeneficiario
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.LoginRequest
import com.andres_lasso.previmed.model.LoginResponse
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.Normalizer

class Login : AppCompatActivity() {

    private lateinit var ctdocumento: EditText
    private lateinit var ctpassword: EditText
    private lateinit var btnLogin: Button

    // 🔤 Normaliza el rol eliminando tildes y convirtiendo a minúsculas
    private fun normalizeRole(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        val normalized = Normalizer.normalize(raw, Normalizer.Form.NFD)
        return normalized.replace("\\p{Mn}+".toRegex(), "").lowercase()
    }

    // 🚀 Redirige según el rol del usuario
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
            Toast.makeText(
                this,
                "Rol no reconocido. Contacte al administrador.",
                Toast.LENGTH_LONG
            ).show()
            Log.e("LOGIN", "Rol no reconocido: $role")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ctdocumento = findViewById(R.id.ctdocumento)
        ctpassword = findViewById(R.id.ctpassword)
        btnLogin = findViewById(R.id.loginButton)

        Log.d("LOGIN", "🚀 LoginActivity creado")

        // 🔐 Si ya hay token, entrar directo
        if (PreferenceHelper.hasToken(this)) {
            val savedRole = PreferenceHelper.getRole(this)
            Log.d("LOGIN", "Token existente, rol: $savedRole")

            if (!savedRole.isNullOrBlank()) {
                goToRoleActivity(normalizeRole(savedRole))
                return
            } else {
                PreferenceHelper.clearSession(this)
            }
        }

        // 🟢 Acción del botón de login
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

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()

                            if (body != null && body.jwt.isNotEmpty()) {
                                val roleNormalized = normalizeRole(body.data.rol.nombreRol)
                                val usuarioId = body.data.id

                                PreferenceHelper.saveToken(this@Login, body.jwt)
                                PreferenceHelper.saveRole(this@Login, roleNormalized)

                                Log.d(
                                    "LOGIN",
                                    "✅ Login exitoso - UUID: $usuarioId - Rol: $roleNormalized"
                                )

                                when (roleNormalized) {
                                    "paciente" -> obtenerIdPaciente(usuarioId, roleNormalized)
                                    "medico" -> obtenerIdMedico(usuarioId, roleNormalized)
                                    else -> {
                                        Toast.makeText(
                                            this@Login,
                                            body.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                        goToRoleActivity(roleNormalized)
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    this@Login,
                                    "Error: no se recibió token",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("LOGIN", "Respuesta sin JWT")
                            }

                        } else {
                            val errorBody = response.errorBody()?.string()
                            Toast.makeText(
                                this@Login,
                                "Credenciales incorrectas",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("LOGIN", "Error: ${response.code()} - $errorBody")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            this@Login,
                            "Error de conexión: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("LOGIN", "Fallo conexión", t)
                    }
                })
        }
    }

    // 🟢 MÉDICO → obtener id_medico con el usuario_id
    private fun obtenerIdMedico(usuarioId: String, role: String) {
        lifecycleScope.launch {
            try {
                Log.d("LOGIN", "🔍 Buscando médico con usuario_id: $usuarioId")

                val response = RetrofitClient.medicoApi.obtenerMedicoPorUsuario(usuarioId)

                if (response.isSuccessful && response.body() != null) {
                    val medicoResponse = response.body()
                    val medico = medicoResponse?.msj

                    if (medico != null) {
                        PreferenceHelper.saveIdMedico(this@Login, medico.id_medico)
                        Log.d("LOGIN", "✅ Médico encontrado: ${medico.id_medico}")
                        goToRoleActivity(role)
                    } else {
                        Toast.makeText(
                            this@Login,
                            "No se encontró médico",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@Login,
                        "Error al obtener médico",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Log.e("LOGIN", "❌ Error: ${e.message}")
            }
        }
    }

    // 🧍 PACIENTE → obtener id_paciente con el usuario_id
    private fun obtenerIdPaciente(usuarioId: String, role: String) {
        lifecycleScope.launch {
            try {
                Log.d("LOGIN", "🔍 Obteniendo perfil para UUID: $usuarioId")

                val response = RetrofitClient.pacienteApi
                    .getPacienteByUsuarioId(usuarioId)
                    .execute()

                if (response.isSuccessful && response.body() != null) {
                    val idPaciente = response.body()!!.data?.idPaciente

                    PreferenceHelper.saveIdPaciente(this@Login, idPaciente.toString())
                    Log.d("LOGIN", "✅ ID paciente guardado: $idPaciente")

                    goToRoleActivity(role)

                } else {
                    Toast.makeText(
                        this@Login,
                        "Error obteniendo datos del paciente",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(
                        "LOGIN",
                        "Error: ${response.code()} - ${response.errorBody()?.string()}"
                    )
                }

            } catch (e: Exception) {
                Toast.makeText(this@Login, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("LOGIN", "Error obteniendo paciente", e)
            }
        }
    }
}
