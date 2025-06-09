package com.andres_lasso.previmed.controller

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.ActivityLoginBinding
import com.andres_lasso.previmed.ViewMedico
import com.andres_lasso.previmed.controller.asesor.ViewAsesor
import com.andres_lasso.previmed.controller.pacientes.ViewBeneficiario

class Login : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupValidationListeners()
        setupLoginButton()
    }


    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {

            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación de usuarios de prueba
            when {
                email == "paciente@mail.com" && password == "Pac123" -> {
                    startActivity(Intent(this, ViewBeneficiario::class.java))
                }
                email == "medico@mail.com" && password == "Med123" -> {
                    startActivity(Intent(this, ViewMedico::class.java))
                }
                email == "asesor@mail.com" && password == "Ase123" -> {
                    startActivity(Intent(this, ViewAsesor::class.java))
                }
                else -> {
                    Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun setupValidationListeners() {

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val emailText = s.toString()
                binding.emailLayout.endIconDrawable = if (isValidEmail(emailText)) {
                    ContextCompat.getDrawable(this@Login, R.drawable.ic_check)
                } else null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val passwordText = s.toString()
                binding.passwordLayout.endIconDrawable = if (passwordText.length >= 6) {
                    ContextCompat.getDrawable(this@Login, R.drawable.ic_check)
                } else null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
