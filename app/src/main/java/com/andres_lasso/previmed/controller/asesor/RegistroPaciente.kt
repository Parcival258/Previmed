package com.andres_lasso.previmed.controller.asesor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.Login
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.RegisterRequest
import com.andres_lasso.previmed.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroPaciente : AppCompatActivity() {

    private lateinit var etDocumento: EditText
    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_paciente)

        etDocumento = findViewById(R.id.etdocumento)
        etNombre = findViewById(R.id.etNombre)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val request = RegisterRequest(
                numero_documento = etDocumento.text.toString(),
                password = etPassword.text.toString(),
                nombre = etNombre.text.toString(),
                email = etEmail.text.toString()
            )

            RetrofitClient.instance.registerUser(request)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@RegistroPaciente,
                                "Usuario creado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Redirigir al Login
                            val intent = Intent(this@RegistroPaciente, Login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@RegistroPaciente,
                                "Error: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(
                            this@RegistroPaciente,
                            "Fallo en la conexión: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}
