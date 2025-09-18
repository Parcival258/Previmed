package com.andres_lasso.previmed.controller.asesor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.HomeAsesorFragment
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.PacientesAsesorFragment
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.PagosAsesorFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ViewAsesor : AppCompatActivity() {

    private lateinit var bottomNavigationAsesor: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_asesor)

        bottomNavigationAsesor = findViewById(R.id.bottom_navigation_asesor)

        // Listener de navegación
        bottomNavigationAsesor.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_home_asesor -> {
                    replaceFragment(HomeAsesorFragment())
                    true
                }
                R.id.btn_pacientes_asesor -> {
                    replaceFragment(PacientesAsesorFragment())
                    true
                }
                R.id.btn_pagos_asesor -> {
                    replaceFragment(PagosAsesorFragment())
                    true
                }
                else -> false
            }
        }

        // Fragment por defecto
        replaceFragment(HomeAsesorFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.asesor_nav, fragment)
            .commit()
    }
}
