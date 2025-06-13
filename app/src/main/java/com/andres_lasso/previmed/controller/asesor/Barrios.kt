package com.andres_lasso.previmed.controller.asesor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.andres_lasso.previmed.controller.asesor.recycler.BarriosProvider
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.BarriosAdapter
import com.andres_lasso.previmed.databinding.ActivityBarriosBinding

class Barrios : AppCompatActivity() {
    private lateinit var binding: ActivityBarriosBinding
    private lateinit var adapter: BarriosAdapter
    private val listBarrios = BarriosProvider.barrioslits

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarriosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerbarriosAsesor.layoutManager = LinearLayoutManager(this)
        adapter = BarriosAdapter(listBarrios)
        binding.recyclerbarriosAsesor.adapter = adapter

        binding.buscarBarrio.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val barriosFiltrados = listBarrios.filter {
                    it.nombreBarrio.contains(newText.orEmpty(), ignoreCase = true)
                }
                adapter.updateBarrios(barriosFiltrados)
                return true
            }
        })
    }
}
