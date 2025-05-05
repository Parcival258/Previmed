package com.andres_lasso.previmed.controller.asesor

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.ActivityPagosViewBinding


class PagosView : AppCompatActivity() {
    private lateinit var binding: ActivityPagosViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityPagosViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIrPagosAdd.setOnClickListener{
            val ir_pagos_add = Intent(this, PagosAdd::class.java)
            startActivity(ir_pagos_add)
        }

    }
}