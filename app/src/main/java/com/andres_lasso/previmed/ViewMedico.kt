package com.andres_lasso.previmed

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.FragmentNavigator
import com.andres_lasso.previmed.controller.medico.fragmentMedico.HistotyMedico
import com.andres_lasso.previmed.controller.medico.fragmentMedico.HomeMedico
import com.andres_lasso.previmed.controller.medico.fragmentMedico.VisitsMedico
import com.google.android.material.bottomnavigation.BottomNavigationView


class ViewMedico : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_medico)

        bottomNavigationView = findViewById(R.id.bottom_navigation_medico)

        // Configurar el listener para el BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.botton_home -> {
                    replaceFragment(HomeMedico())
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
        replaceFragment(HomeMedico())


    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container_medico, fragment).commit()

    }

}