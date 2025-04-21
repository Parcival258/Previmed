package com.andres_lasso.previmed.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.ActivityLoginBinding
import android.util.Patterns
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import com.andres_lasso.previmed.Menu
import com.andres_lasso.previmed.ViewMedico
import com.andres_lasso.previmed.controller.asesor.ViewAsesor
import com.andres_lasso.previmed.controller.pacientes.ViewBeneficiario
import com.google.android.material.textfield.TextInputEditText

class Login : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupValidationListeners()

        //Intent para ir al menú por el momento es temporal *****
        val butonLogin: Button = findViewById(R.id.loginButton);

        //ir al menu:
        butonLogin.setOnClickListener {

            //Implementación de la navegación en el Login:
            val emailEditText: TextInputEditText = findViewById(R.id.emailEditText);
            val email: String = emailEditText.text.toString();

            val passwordEditText: TextInputEditText = findViewById(R.id.passwordEditText);
            val password: String = passwordEditText.text.toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (email == "paciente@mail.com" && password == "Pac123") {
                val irBeneficiario = Intent(this, ViewBeneficiario::class.java);
                startActivity(irBeneficiario);
                return@setOnClickListener
            }
            if (email == "medico@mail.com" && password == "Med123") {
                val irMed = Intent(this, ViewMedico::class.java);
                startActivity(irMed);
                return@setOnClickListener
            }
            if (email == "asesor@mail.com" && password == "Ase123") {
                val irAse = Intent(this, ViewAsesor::class.java);
                startActivity(irAse);
                return@setOnClickListener
            }
            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para activar los check dinámicos
    private fun setupValidationListeners() {

        // Validación de email
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val emailText = s.toString()
                if (isValidEmail(emailText)) {
                    binding.emailLayout.endIconDrawable = ContextCompat.getDrawable(
                        this@Login,
                        R.drawable.ic_check
                    )
                } else {
                    binding.emailLayout.endIconDrawable = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Validación de contraseña
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val passwordText = s.toString()
                if (passwordText.length >= 6) {
                    binding.passwordLayout.endIconDrawable = ContextCompat.getDrawable(
                        this@Login,
                        R.drawable.ic_check
                    )
                } else {
                    binding.passwordLayout.endIconDrawable = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Validador de email
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
