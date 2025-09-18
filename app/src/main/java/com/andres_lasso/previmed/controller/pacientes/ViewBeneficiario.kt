package com.andres_lasso.previmed.controller.pacientes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.ContratoBeneficiarioFragment
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.HomeBeneficiarioFragment
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.PagosBeneficiarioFragment
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.VisitaBeneficiarioFragment
import com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario.VispendientesBeneficiarioFragment
import com.andres_lasso.previmed.databinding.ActivityViewBeneficiarioBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ViewBeneficiario : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityViewBeneficiarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBeneficiarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.bottomNavigationPac

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_home -> {
                    replaceFragment(HomeBeneficiarioFragment())
                    true
                }
                R.id.btn_visita -> {
                    replaceFragment(VisitaBeneficiarioFragment())
                    true
                }
                R.id.btn_contrato -> {
                    replaceFragment(ContratoBeneficiarioFragment())
                    true
                }
                R.id.btn_pago -> {
                    replaceFragment(PagosBeneficiarioFragment())
                    true
                }
                R.id.btn_mis_visitas -> {
                    replaceFragment(VispendientesBeneficiarioFragment())
                    true
                }
                else -> false
            }
        }

        // Fragment inicial por defecto
        replaceFragment(HomeBeneficiarioFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.paciente_nav, fragment)
            .commit()
    }
}
