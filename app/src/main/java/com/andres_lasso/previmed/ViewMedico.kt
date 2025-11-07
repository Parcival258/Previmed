package com.andres_lasso.previmed

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.controller.medico.fragmentMedico.HistotyMedico
import com.andres_lasso.previmed.controller.medico.fragmentMedico.HomeMedicoFragment
import com.andres_lasso.previmed.controller.medico.fragmentMedico.VisitsMedico
import com.andres_lasso.previmed.databinding.ActivityViewMedicoBinding

class ViewMedico : AppCompatActivity() {

    private lateinit var binding: ActivityViewMedicoBinding
    private var selectedIndex = -1

    // Fragments que manejará el médico
    private val fragments = listOf(
        HomeMedicoFragment(),
        VisitsMedico(),
        HistotyMedico()
    )

    // Ítems de navegación personalizados
    private val navItems by lazy {
        listOf(
            NavItem(binding.navHome, binding.homeBackground, binding.homeIcon, binding.homeText),
            NavItem(binding.navVisits, binding.visitsBackground, binding.visitsIcon, binding.visitsText),
            NavItem(binding.navHistory, binding.historyBackground, binding.historyIcon, binding.historyText)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMedicoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        selectNavItem(0) // Cargar Home al inicio
    }

    private fun setupNavigation() {
        navItems.forEachIndexed { index, navItem ->
            navItem.container.setOnClickListener {
                selectNavItem(index)
            }
        }
    }

    private fun selectNavItem(position: Int) {
        if (selectedIndex == position) return

        resetAllNavItems()
        activateNavItem(position)
        replaceFragment(fragments[position])
        selectedIndex = position
    }

    private fun resetAllNavItems() {
        val inactiveColor = ContextCompat.getColor(this, android.R.color.white) // BLANCO

        navItems.forEach { navItem ->
            navItem.apply {
                background.visibility = View.INVISIBLE
                icon.setColorFilter(inactiveColor)
                text.setTextColor(inactiveColor)
                text.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }
    }

    private fun activateNavItem(position: Int) {
        navItems[position].apply {
            background.visibility = View.VISIBLE
            icon.setColorFilter(ContextCompat.getColor(this@ViewMedico, android.R.color.white))
            text.setTextColor(ContextCompat.getColor(this@ViewMedico, android.R.color.white))
            text.setTypeface(null, android.graphics.Typeface.BOLD)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container_medico, fragment)
            .commit()
    }

    // Modelo para manejar cada ítem de la barra de navegación
    private data class NavItem(
        val container: View,
        val background: View,
        val icon: android.widget.ImageView,
        val text: android.widget.TextView
    )
}
