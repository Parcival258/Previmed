package com.andres_lasso.previmed.controller.pacientes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.ContratoBeneficiarioFragment
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.HomeBeneficiarioFragment
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.PagosBeneficiarioFragment
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.VisitaBeneficiarioFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ViewBeneficiario : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_beneficiario)

        bottomNavigationView = findViewById(R.id.bottom_navigation_pac)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.btn_home ->{
                    replaceFragment(HomeBeneficiarioFragment())
                    true
                }
                R.id.btn_visita ->{
                    replaceFragment(VisitaBeneficiarioFragment())
                    true
                }
                R.id.btn_contrato ->{
                    replaceFragment(ContratoBeneficiarioFragment())
                    true
                }
                R.id.btn_pago ->{
                    replaceFragment(PagosBeneficiarioFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeBeneficiarioFragment())

    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.paciente_nav, fragment).commit()
    }
}