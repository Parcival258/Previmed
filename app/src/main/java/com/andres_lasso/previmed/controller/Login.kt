package com.andres_lasso.previmed.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.Menu
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.LoginRequest
import com.andres_lasso.previmed.model.LoginResponse
import com.andres_lasso.previmed.utils.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var ctdocumento: EditText
    private lateinit var ctpassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar vistas
        ctdocumento = findViewById(R.id.ctdocumento)
        ctpassword = findViewById(R.id.ctpassword)
        btnLogin = findViewById(R.id.loginButton)

        // Si ya hay token, ir al menú
        if (PreferenceHelper.hasToken(this)) {
            startActivity(Intent(this, Menu::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            val documento = ctdocumento.text.toString().trim()
            val password = ctpassword.text.toString().trim()

            if (documento.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear request correctamente con @SerializedName
            val request = LoginRequest(
                numeroDocumento = documento,
                password = password
            )

            RetrofitClient.loginApi.loginUser(request)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null && body.jwt.isNotEmpty()) {

                                // Guardar token
                                PreferenceHelper.saveToken(this@Login, body.jwt)

                                Toast.makeText(
                                    this@Login,
                                    body.message,
                                    Toast.LENGTH_LONG
                                ).show()

                                Log.d(
                                    "API_LOGIN",
                                    "JWT: ${body.jwt}, UserId: ${body.data.id}, Documento: ${body.data.documento}"
                                )

                                // Ir al menú
                                val intent = Intent(this@Login, Menu::class.java)
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(
                                    this@Login,
                                    "Error: no se recibió token",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("API_LOGIN", "Respuesta sin JWT")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Toast.makeText(
                                this@Login,
                                "Credenciales incorrectas",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(
                                "API_LOGIN",
                                "Error: ${response.code()} - $errorBody"
                            )
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            this@Login,
                            "Error de conexión: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("API_LOGIN", "Fallo conexión", t)
                    }
                })
        }
    }
}