package com.andres_lasso.previmed.controller.asesor

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.PlanesProvider
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.PlanesAdapter
import com.andres_lasso.previmed.databinding.ActivityPlanesViewBinding

class PlanesView : AppCompatActivity() {
    private  lateinit var binding: ActivityPlanesViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanesViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
    }
    private fun initRecyclerView(){
        val recyclerView: RecyclerView = binding.recyclerPlanesAsesor
        recyclerView.layoutManager = LinearLayoutManager(this);
        recyclerView.adapter = PlanesAdapter(PlanesProvider.planesList)
    }
}