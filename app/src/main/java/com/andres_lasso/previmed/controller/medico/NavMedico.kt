package com.andres_lasso.previmed.controller.medico

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.ActivityNavMedicoBinding

class NavMedico : AppCompatActivity() {

    private lateinit var binding: ActivityNavMedicoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla el layout usando ViewBinding
        binding = ActivityNavMedicoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura la navegación inferior
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_activity_nav_medico)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_visits,
                R.id.navigation_history
            )
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }
}
