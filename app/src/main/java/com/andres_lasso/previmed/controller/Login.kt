package com.andres_lasso.previmed.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.LoginRequest
import com.andres_lasso.previmed.model.LoginResponse
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

        // Acción al presionar el botón de login
        btnLogin.setOnClickListener {
            val request = LoginRequest(
                numero_documento = ctdocumento.text.toString(),
                password = ctpassword.text.toString()
            )

            // Llamada a la API para login
            RetrofitClient.loginApi.loginUser(request)
                .enqueue(object : Callback<LoginResponse> {

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val body = response.body()!!

                            // Mostrar info del backend en un Toast
                            Toast.makeText(
                                this@Login,
                                "Login OK: token=${body.token}, rol=${body.rol}, msg=${body.message}",
                                Toast.LENGTH_LONG
                            ).show()

                            // Logcat para depuración
                            Log.d("API_LOGIN", "Respuesta completa: $body")

                            // TODO: Cambiar destino según el rol o pantalla principal
                            val intent = Intent(this@Login, Login::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            // Error con código y cuerpo
                            val errorBody = response.errorBody()?.string()
                            Toast.makeText(
                                this@Login,
                                "Error: ${response.code()} - $errorBody",
                                Toast.LENGTH_LONG
                            ).show()

                            Log.e("API_LOGIN", "Error: ${response.code()} - $errorBody")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            this@Login,
                            "Fallo en la conexión: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.e("API_LOGIN", "Fallo en conexión", t)
                    }
                })
        }
    }
}
