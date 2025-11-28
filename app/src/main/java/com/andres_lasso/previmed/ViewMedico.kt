package com.andres_lasso.previmed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.controller.medico.fragmentMedico.HistotyMedico
import com.andres_lasso.previmed.controller.medico.fragmentMedico.HomeFragment
import com.andres_lasso.previmed.controller.medico.fragmentMedico.VisitsMedico
import com.andres_lasso.previmed.databinding.ActivityViewMedicoBinding

class ViewMedico : AppCompatActivity() {

    private lateinit var binding: ActivityViewMedicoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMedicoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()

        // Cargar fragment inicial
        replaceFragment(HomeFragment())
    }

    private fun setupBottomNavigation() {

        binding.bottomNavigationMedico.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.botton_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.botton_visits -> {
                    replaceFragment(VisitsMedico())
                    true
                }

                R.id.botton_history -> {
                    replaceFragment(HistotyMedico())
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container_medico, fragment)
            .commit()
    }
}
