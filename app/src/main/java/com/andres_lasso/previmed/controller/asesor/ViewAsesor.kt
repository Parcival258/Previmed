package com.andres_lasso.previmed.controller.asesor

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.ContratoAsesorFragment
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.HomeAsesorFragment
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.PacientesAsesorFragment
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.PagosAsesorFragment
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.PlanesAsesorFragment
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.ContratoBeneficiarioFragment
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.HomeBeneficiarioFragment
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.PagosBeneficiarioFragment
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.VisitaBeneficiarioFragment
import com.andres_lasso.previmed.databinding.FragmentPagosAsesorBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ViewAsesor : AppCompatActivity() {

    private lateinit var bottom_navigation_asesor: BottomNavigationView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_asesor)

        bottom_navigation_asesor = findViewById(R.id.bottom_navigation_asesor)
        bottom_navigation_asesor.setOnItemSelectedListener { menuItem ->
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

                R.id.btn_planes_asesor -> {
                    replaceFragment(PlanesAsesorFragment())
                    true
                }

                R.id.btn_contrato_asesor -> {
                    replaceFragment(ContratoAsesorFragment())
                    true
                }

                else -> false
            }
        }
        replaceFragment(HomeAsesorFragment())
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.asesor_nav, fragment).commit()
    }
}