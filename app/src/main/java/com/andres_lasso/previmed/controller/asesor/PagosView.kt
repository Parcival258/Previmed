package com.andres_lasso.previmed.controller.asesor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andres_lasso.previmed.controller.asesor.recycler.PagosAdapter
import com.andres_lasso.previmed.databinding.ActivityPagosViewBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import kotlinx.coroutines.launch

class PagosView : AppCompatActivity() {

    private lateinit var binding: ActivityPagosViewBinding
    private val adapter = PagosAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPagosViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()
        loadPagos()
    }

    private fun setupRecycler() {
        binding.recyclerPagosAsesor.layoutManager = LinearLayoutManager(this)
        binding.recyclerPagosAsesor.adapter = adapter
    }

    private fun loadPagos() {
        lifecycleScope.launch {
            try {
                // La API devuelve PagosResponse directamente
                val response = RetrofitClient.pagoApi.obtenerPagos()

                // Lista de pagos proveniente de data
                val listaPagos = response.data ?: emptyList()

                // Cargar al adapter
                adapter.setData(listaPagos)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
