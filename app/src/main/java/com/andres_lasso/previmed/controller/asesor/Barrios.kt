package com.andres_lasso.previmed.controller.asesor

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.BarriosClass
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.BarriosAdapter
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.BarriosResponse
import kotlinx.coroutines.launch

class Barrios : AppCompatActivity() {
    private lateinit var adapter: BarriosAdapter
    private var listaOriginal = listOf<BarriosClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barrios)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerbarriosAsesor)
        val searchInput = findViewById<EditText>(R.id.etBuscarBarrio)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BarriosAdapter(mutableListOf())
        recyclerView.adapter = adapter

        searchInput.addTextChangedListener {
            val query = it.toString().lowercase().trim()
            filtrarLista(query)
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.barriosApi.listarBarrios()
                if (response.isSuccessful) {
                    val responseList: List<BarriosResponse> = response.body()?.msj ?: emptyList()
                    listaOriginal = responseList.map {
                        BarriosClass(
                            idBarrio = it.idBarrio,
                            nombreBarrio = it.nombreBarrio,
                            latitud = it.latitud,
                            longitud = it.longitud,
                            habilitar = it.estado,
                            ciudad = null,
                            comuna = null
                        )
                    }
                    adapter.actualizarLista(listaOriginal)
                } else {
                    Toast.makeText(this@Barrios, "Error al cargar barrios", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@Barrios, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filtrarLista(query: String) {
        val filtrada = if (query.isEmpty()) listaOriginal else listaOriginal.filter {
            it.nombreBarrio.lowercase().contains(query)
        }
        adapter.actualizarLista(filtrada)
    }
}
