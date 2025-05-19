package com.andres_lasso.previmed.controller.asesor


import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.andres_lasso.previmed.controller.asesor.recycler.PagosProvider
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.PagosAdapter
import com.andres_lasso.previmed.databinding.ActivityPagosViewBinding



class PagosView : AppCompatActivity() {
    private lateinit var binding: ActivityPagosViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagosViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerview()
    }

    private fun initRecyclerview(){
        val recyclerView: RecyclerView = binding.recyclerPagosAsesor
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PagosAdapter(PagosProvider.pagosList)
    }
}