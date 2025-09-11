package com.andres_lasso.previmed.controller.asesor

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    private lateinit var adapter: PlanesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanesViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PlanesAdapter(listaPlanes)
        binding.recyclerPlanesAsesor.layoutManager = LinearLayoutManager(this)
        binding.recyclerPlanesAsesor.adapter = adapter

        cargarPlanes()
    }

    private fun cargarPlanes() {
        RetrofitClient.planes.getPlanes().enqueue(object : Callback<PlanesResponse> {
            override fun onResponse(call: Call<PlanesResponse>, response: Response<PlanesResponse>) {
                if (response.isSuccessful) {
                    val planes = response.body()?.planes?.filter { it.estado } ?: listOf()
                    listaPlanes.clear()
                    listaPlanes.addAll(planes)
                    adapter.notifyDataSetChanged()
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

    // Método público para agregar un nuevo plan y actualizar la lista automáticamente
    fun agregarNuevoPlan(plan: Plan) {
        listaPlanes.add(plan)
        adapter.agregarPlan(plan)
    }
    private val pollingInterval = 5000L // 5 segundos

    private val pollingHandler = Handler(Looper.getMainLooper())
    private val pollingRunnable = object : Runnable {
        override fun run() {
            cargarPlanes()
            pollingHandler.postDelayed(this, pollingInterval)
        }
    }

    override fun onResume() {
        super.onResume()
        pollingHandler.post(pollingRunnable)
    }

    override fun onPause() {
        super.onPause()
        pollingHandler.removeCallbacks(pollingRunnable)
    }

}

