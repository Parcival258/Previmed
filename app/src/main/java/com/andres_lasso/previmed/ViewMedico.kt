package com.andres_lasso.previmed

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.TextView
import com.andres_lasso.previmed.controller.medico.fragmentMedico.HistotyMedico
import com.andres_lasso.previmed.controller.medico.fragmentMedico.HomeMedico
import com.andres_lasso.previmed.controller.medico.fragmentMedico.VisitsMedico

class ViewMedico : AppCompatActivity() {

    // Variables para la navegación personalizada
    private lateinit var navHome: LinearLayout
    private lateinit var navVisits: LinearLayout
    private lateinit var navHistory: LinearLayout

    private lateinit var homeBackground: View
    private lateinit var visitsBackground: View
    private lateinit var historyBackground: View

    private lateinit var homeIcon: ImageView
    private lateinit var visitsIcon: ImageView
    private lateinit var historyIcon: ImageView

    private lateinit var homeText: TextView
    private lateinit var visitsText: TextView
    private lateinit var historyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_medico)

        initViews()
        setupNavigation()

        // Seleccionar "Inicio" por defecto
        selectNavItem(0)
        replaceFragment(HomeMedico())
    }

    private fun initViews() {
        navHome = findViewById(R.id.nav_home)
        navVisits = findViewById(R.id.nav_visits)
        navHistory = findViewById(R.id.nav_history)

        homeBackground = findViewById(R.id.home_background)
        visitsBackground = findViewById(R.id.visits_background)
        historyBackground = findViewById(R.id.history_background)

        homeIcon = findViewById(R.id.home_icon)
        visitsIcon = findViewById(R.id.visits_icon)
        historyIcon = findViewById(R.id.history_icon)

        homeText = findViewById(R.id.home_text)
        visitsText = findViewById(R.id.visits_text)
        historyText = findViewById(R.id.history_text)
    }

    private fun setupNavigation() {
        navHome.setOnClickListener {
            selectNavItem(0)
            replaceFragment(HomeMedico())
        }

        navVisits.setOnClickListener {
            selectNavItem(1)
            replaceFragment(VisitsMedico())
        }

        navHistory.setOnClickListener {
            selectNavItem(2)
            replaceFragment(HistotyMedico())
        }
    }

    private fun selectNavItem(position: Int) {
        // Resetear todos los elementos
        resetAllNavItems()

        when (position) {
            0 -> { // Inicio, aqui cambio el color de cuando esta seleccionado
                homeBackground.visibility = View.VISIBLE
                homeIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white))
                homeText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                homeText.setTypeface(null, android.graphics.Typeface.BOLD)
            }

            1 -> { // Visitas, aqui cambio el color de cuando esta seleccionado
                visitsBackground.visibility = View.VISIBLE
                visitsIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white))
                visitsText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                visitsText.setTypeface(null, android.graphics.Typeface.BOLD)
            }

            2 -> { // Historial, aqui cambio el color de cuando esta seleccionado
                historyBackground.visibility = View.VISIBLE
                historyIcon.setColorFilter(ContextCompat.getColor(this, android.R.color.white))
                historyText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                historyText.setTypeface(null, android.graphics.Typeface.BOLD)
            }
        }
    }


    private fun resetAllNavItems() {
        // Ocultar todos los fondos
        homeBackground.visibility = View.INVISIBLE
        visitsBackground.visibility = View.INVISIBLE
        historyBackground.visibility = View.INVISIBLE

        // Restaurar colores originales
        val originalColor = ContextCompat.getColor(this, R.color.AzulOscuro_Prevemed)

        homeIcon.setColorFilter(originalColor)
        visitsIcon.setColorFilter(originalColor)
        historyIcon.setColorFilter(originalColor)

        homeText.setTextColor(originalColor)
        visitsText.setTextColor(originalColor)
        historyText.setTextColor(originalColor)

        // Restaurar tipo de fuente normal
        homeText.setTypeface(null, android.graphics.Typeface.NORMAL)
        visitsText.setTypeface(null, android.graphics.Typeface.NORMAL)
        historyText.setTypeface(null, android.graphics.Typeface.NORMAL)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container_medico, fragment)
            .commit()
    }
}