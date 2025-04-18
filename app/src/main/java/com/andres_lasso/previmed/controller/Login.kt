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
import com.andres_lasso.previmed.Menu

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
        val irMenu = Intent(this, Menu::class.java);
        startActivity(irMenu);
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
