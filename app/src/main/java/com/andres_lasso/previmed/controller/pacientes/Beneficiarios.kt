package com.andres_lasso.previmed.controller.pacientes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.pacientes.recycler.BeneficiarioViewModel
import com.andres_lasso.previmed.controller.pacientes.recycler.BeneficiariosRepo
import com.andres_lasso.previmed.controller.pacientes.recycler.adapter.BeneficiariosAdapter
import com.andres_lasso.previmed.databinding.ActivityBeneficiariosBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.utils.PreferenceHelper


class Beneficiarios : AppCompatActivity() {

    private lateinit var binding: ActivityBeneficiariosBinding
    private lateinit var adapter: BeneficiariosAdapter
    private lateinit var viewModel: BeneficiarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeneficiariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BeneficiariosAdapter(emptyList())
        binding.recyclerBeneficiarios.layoutManager = LinearLayoutManager(this)
        binding.recyclerBeneficiarios.adapter = adapter

        val repo = BeneficiariosRepo(RetrofitClient.beneficiarioApi)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return BeneficiarioViewModel(repo) as T
            }
        })[BeneficiarioViewModel::class.java]

        viewModel.beneficiarios.observe(this) { lista ->
            if (lista.isEmpty()) Toast.makeText(this, "No hay beneficiarios", Toast.LENGTH_SHORT).show()
            adapter.submitList(lista)
        }

        val pacienteId = PreferenceHelper.getIdPaciente(this)?.toIntOrNull()
        if (pacienteId != null) viewModel.cargarBeneficiarios(pacienteId)
    }
}
