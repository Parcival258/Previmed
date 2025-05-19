package com.andres_lasso.previmed.controller.asesor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.BarriosProvider
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.BarriosAdapter
import com.andres_lasso.previmed.databinding.ActivityBarriosBinding

class Barrios : AppCompatActivity() {
    private lateinit var binding: ActivityBarriosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarriosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerbarriosAsesor
        recyclerView.layoutManager = LinearLayoutManager(this);
        recyclerView.adapter = BarriosAdapter(BarriosProvider.barrioslits)
    }
}