package com.andres_lasso.previmed.controller.asesor

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.PagosAsesorFragment
import com.andres_lasso.previmed.databinding.ActivityPagosAddBinding

class PagosAdd : AppCompatActivity() {
    private lateinit var binding: ActivityPagosAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagosAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titulares = listOf("","Andres","Jorge","Jesus","Daniela","sofia","jeison")
        val tipos_de_pago = listOf("","Efectivo", "Transacción", "Debito automatico")

        // Crear un ArrayAdapter para el Spinner de titulares
        val titularAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titulares)
        titularAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTitular.adapter = titularAdapter

        // Crear un ArrayAdapter para el Spinner de tipos de pago
        val tipoPagoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos_de_pago)
        tipoPagoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFormaPago.adapter = tipoPagoAdapter

        binding.btnSavePago.setOnClickListener{
            val ir_pagos = Intent(this, PagosAsesorFragment::class.java)
            startActivity(ir_pagos)
            finish()
            Toast.makeText(this, "Pago Agregado exitosamene ✔️", Toast.LENGTH_SHORT).show()
        }

    }
}