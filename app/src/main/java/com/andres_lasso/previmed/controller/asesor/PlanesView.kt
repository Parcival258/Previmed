package com.andres_lasso.previmed.controller.asesor

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.PlanesAdapter
import com.andres_lasso.previmed.databinding.ActivityPlanesViewBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.Plan
import com.andres_lasso.previmed.model.PlanesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlanesView : AppCompatActivity() {
    private lateinit var binding: ActivityPlanesViewBinding
    private val listaPlanes = mutableListOf<Plan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanesViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarPlanes()
    }

    private fun cargarPlanes() {
        RetrofitClient.planes.getPlanes().enqueue(object : Callback<PlanesResponse> {
            override fun onResponse(call: Call<PlanesResponse>, response: Response<PlanesResponse>) {
                if (response.isSuccessful) {
                    val planes = response.body()?.planes?.filter { it.estado } ?: listOf()
                    listaPlanes.clear()
                    listaPlanes.addAll(planes)
                    initRecyclerView()
                } else {
                    Toast.makeText(this@PlanesView, "Error al cargar planes", Toast.LENGTH_SHORT).show()
                }
                Log.d("PlanesView", "Planes recibidos: ${listaPlanes.size}")
                listaPlanes.forEach {
                    Log.d("PlanesView", "Plan: ${it.tipoPlan} - Precio: ${it.precio}")
                }

            }

            override fun onFailure(call: Call<PlanesResponse>, t: Throwable) {
                Toast.makeText(this@PlanesView, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerPlanesAsesor
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlanesAdapter(listaPlanes)
    }
}
