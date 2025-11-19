package com.andres_lasso.previmed.controller.asesor

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.BarriosClass
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.BarriosAdapter
import com.andres_lasso.previmed.interfaces.RetrofitClient
import kotlinx.coroutines.launch

class Barrios : AppCompatActivity() {

    private lateinit var adapter: BarriosAdapter
    private var listaOriginal = listOf<BarriosClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🟦 Activa que el contenido use toda la pantalla
        enableEdgeToEdge()

        setContentView(R.layout.activity_barrios)

        // 🟦 Ajustar padding según barra de estado (FUNCIONA CON TU XML)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootBarrios)) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(
                view.paddingLeft,
                statusBarInsets.top,   // 👈 Agregamos el padding de la barra de estado
                view.paddingRight,
                view.paddingBottom
            )
            WindowInsetsCompat.CONSUMED
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerbarriosAsesor)
        val searchInput = findViewById<EditText>(R.id.etBuscarBarrio)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BarriosAdapter(mutableListOf())
        recyclerView.adapter = adapter

        // 🔍 Filtro de búsqueda
        searchInput.addTextChangedListener {
            val query = it.toString().lowercase().trim()
            filtrarLista(query)
        }

        // 📡 Llamada API
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.visitasApi.getBarrios()

                println("🔥 Código: ${response.code()}")
                println("🔥 Raw: ${response.errorBody()?.string()}")
                println("🔥 Body: ${response.body()}")

                if (response.isSuccessful && response.body() != null) {
                    val listarBarrios = response.body()!!.msj

                    listaOriginal = listarBarrios.map {
                        BarriosClass(
                            idBarrio = it.idBarrio ?: 0,
                            nombreBarrio = it.nombreBarrio ?: "",
                            latitud = it.latitud ?: 0.0,
                            longitud = it.longitud ?: 0.0,
                            estado = it.estado ?: false
                        )
                    }

                    adapter.actualizarLista(listaOriginal)
                } else {
                    Toast.makeText(this@Barrios, "Error al cargar barrios", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@Barrios, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filtrarLista(query: String) {
        val filtrada = if (query.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter {
                (it.nombreBarrio ?: "").lowercase().contains(query)
            }
        }
        adapter.actualizarLista(filtrada)
    }

}
