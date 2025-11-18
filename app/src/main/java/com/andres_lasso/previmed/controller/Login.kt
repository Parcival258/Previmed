package com.andres_lasso.previmed.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationResult
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.ViewMedico
import com.andres_lasso.previmed.controller.asesor.ViewAsesor
import com.andres_lasso.previmed.controller.pacientes.ViewBeneficiario
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.LoginRequest
import com.andres_lasso.previmed.model.LoginResponse
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.Normalizer

class Login : AppCompatActivity() {

    private lateinit var ctdocumento: EditText
    private lateinit var ctpassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnBiometric: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var isBiometricInProgress = false

    private var isLoggingIn = false

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
            PreferenceHelper.clearSessionButKeepBiometric(this)
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
        btnBiometric = findViewById(R.id.btnBiometric)
        progressBar = findViewById(R.id.progressBar)

        Log.d("LOGIN", "🚀 LoginActivity creado")

        // Configurar autenticación biométrica
        setupBiometric()

        if (PreferenceHelper.hasToken(this)) {
            val savedRole = PreferenceHelper.getRole(this)
            Log.d("LOGIN", "Token existente, rol: $savedRole")

            if (!savedRole.isNullOrBlank()) {
                goToRoleActivity(normalizeRole(savedRole))
                return
            } else {
                PreferenceHelper.clearSessionButKeepBiometric(this)
            }
        }

        // Debug: Ver si hay datos biométricos guardados
        val docGuardado = PreferenceHelper.getDocumento(this)
        val passGuardada = PreferenceHelper.getPassword(this)
        Log.d("LOGIN", "📱 Datos biométricos al abrir: documento=$docGuardado, password=${if (passGuardada != null) "***" else "null"}")

        // Configurar botón biométrico
        configurarBotonBiometrico()

        btnLogin.setOnClickListener {
            if (isLoggingIn) return@setOnClickListener

            val documento = ctdocumento.text.toString().trim()
            val password = ctpassword.text.toString().trim()

            if (documento.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = LoginRequest(numeroDocumento = documento, password = password)
            iniciarSesion(request)
        }
    }

    private fun configurarBotonBiometrico() {
        val tieneDatos = PreferenceHelper.hasBiometricData(this)
        val disponible = isBiometricAvailable()

        when {
            tieneDatos && disponible -> {
                btnBiometric.visibility = View.VISIBLE
                btnBiometric.isEnabled = true
                btnBiometric.alpha = 1f
                Log.d("BIOMETRIC", "✅ Botón biométrico DISPONIBLE")
            }
            tieneDatos && !disponible -> {
                btnBiometric.visibility = View.VISIBLE
                btnBiometric.isEnabled = false
                btnBiometric.alpha = 0.5f
                btnBiometric.text = "Biometría no disponible"
            }
            else -> {
                btnBiometric.visibility = View.GONE
                Log.d("BIOMETRIC", "❌ No hay datos biométricos guardados")
            }
        }

        btnBiometric.setOnClickListener {
            if (!isBiometricInProgress && PreferenceHelper.hasBiometricData(this)) {
                isBiometricInProgress = true
                btnBiometric.isEnabled = false
                mostrarBiometricPrompt()
            }
        }
    }

    private fun setupBiometric() {
        val executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d("BIOMETRIC", "✅ Autenticación biométrica exitosa")
                    isBiometricInProgress = false
                    procesarLoginBiometrico()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    isBiometricInProgress = false
                    btnBiometric.isEnabled = true

                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                        Log.e("BIOMETRIC", "Error: $errString")
                        Toast.makeText(
                            this@Login,
                            "Error: $errString",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    isBiometricInProgress = false
                    btnBiometric.isEnabled = true
                    Toast.makeText(
                        this@Login,
                        "Intenta de nuevo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación Biométrica")
            .setSubtitle("Usa tu huella digital o reconocimiento facial")
            .setDescription("Acerca tu dedo al sensor")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    private fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun mostrarBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }

    private fun procesarLoginBiometrico() {
        val documento = PreferenceHelper.getDocumento(this)
        val password = PreferenceHelper.getPassword(this)

        if (documento.isNullOrBlank() || password.isNullOrBlank()) {
            Toast.makeText(this, "Error: Datos biométricos inválidos", Toast.LENGTH_SHORT).show()
            PreferenceHelper.clearDocumento(this)
            PreferenceHelper.clearPassword(this)
            configurarBotonBiometrico()
            return
        }

        Log.d("BIOMETRIC", "🔓 Iniciando sesión con documento: $documento")

        // Llenar campos automáticamente
        ctdocumento.setText(documento)
        ctpassword.setText(password)

        // Pequeña pausa visual
        ctdocumento.postDelayed({
            val request = LoginRequest(numeroDocumento = documento, password = password)
            iniciarSesion(request)
        }, 300)
    }

    private fun iniciarSesion(request: LoginRequest) {
        isLoggingIn = true
        btnLogin.isEnabled = false
        btnBiometric.isEnabled = false
        progressBar.visibility = View.VISIBLE
        btnLogin.text = "Iniciando sesión..."

        RetrofitClient.loginApi.loginUser(request)
            .enqueue(object : Callback<LoginResponse> {

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    isLoggingIn = false
                    btnLogin.isEnabled = true
                    btnBiometric.isEnabled = true
                    progressBar.visibility = View.GONE
                    btnLogin.text = "Ingresar"

                    try {
                        if (response.isSuccessful) {

                            val body = response.body()
                            if (body == null) {
                                Toast.makeText(this@Login, "Error al procesar respuesta.", Toast.LENGTH_SHORT).show()
                                return
                            }

                            if (body.jwt.isNullOrEmpty() || body.data == null) {
                                Toast.makeText(this@Login, body.msg ?: "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                                return
                            }

                            val roleNormalized = normalizeRole(body.data.rol?.nombreRol)
                            val usuarioId = body.data.id

                            // ✔ Guardamos token y rol
                            PreferenceHelper.saveToken(this@Login, body.jwt)
                            PreferenceHelper.saveRole(this@Login, roleNormalized)
                            PreferenceHelper.saveIdAsesor(this@Login, usuarioId)
                            PreferenceHelper.saveUsuarioId(this@Login, usuarioId)

                            // 🔒 Guardar credenciales para login biométrico
                            Log.d("LOGIN", "💾 Guardando credenciales: documento=${request.numeroDocumento}")
                            PreferenceHelper.saveDocumento(this@Login, request.numeroDocumento)
                            PreferenceHelper.savePassword(this@Login, request.password)

                            Log.d("LOGIN", "✅ Login exitoso - UUID: $usuarioId - Rol: $roleNormalized")

                            when (roleNormalized) {
                                "paciente" -> obtenerIdPaciente(usuarioId, roleNormalized)
                                "medico" -> obtenerIdMedico(usuarioId, roleNormalized)
                                else -> {
                                    Toast.makeText(this@Login, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                                    goToRoleActivity(roleNormalized)
                                }
                            }

                        } else {
                            Toast.makeText(this@Login, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(this@Login, "Error inesperado: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    isLoggingIn = false
                    btnLogin.isEnabled = true
                    btnBiometric.isEnabled = true
                    progressBar.visibility = View.GONE
                    btnLogin.text = "Ingresar"
                    isBiometricInProgress = false

                    Toast.makeText(this@Login, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun obtenerIdMedico(usuarioId: String, role: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.medicoApi.obtenerMedicoPorUsuario(usuarioId)
                if (response.isSuccessful && response.body()?.msj != null) {
                    val medico = response.body()!!.msj
                    PreferenceHelper.saveIdMedico(this@Login, medico.id_medico)
                    goToRoleActivity(role)
                } else {
                    Toast.makeText(this@Login, "No se encontró médico", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LOGIN", "Error: ${e.message}")
            }
        }
    }

    private fun obtenerIdPaciente(usuarioId: String, role: String) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.pacienteApi.getPacienteByUsuarioId(usuarioId).execute()
                }

                if (response.isSuccessful && response.body()?.data != null) {
                    val idPaciente = response.body()!!.data!!.idPaciente
                    PreferenceHelper.saveIdPaciente(this@Login, idPaciente.toString())
                    PreferenceHelper.saveUsuarioId(this@Login, usuarioId)
                    goToRoleActivity(role)
                } else {
                    Toast.makeText(this@Login, "Error obteniendo paciente", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@Login, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}