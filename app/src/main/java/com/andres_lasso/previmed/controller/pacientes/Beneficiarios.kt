package com.andres_lasso.previmed.controller.pacientes

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.pacientes.recycler.BeneficiariosProvider
import com.andres_lasso.previmed.controller.pacientes.recycler.adapter.BeneficiariosAdapter
import com.andres_lasso.previmed.databinding.ActivityBeneficiariosBinding

class Beneficiarios : AppCompatActivity() {
    private lateinit var binding: ActivityBeneficiariosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeneficiariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Muestra si es beneficiario o no.
        val itsBeneficiario: Boolean = true;
        val txtbeneficiario: TextView = binding.indicadorBeneficiario

        if (itsBeneficiario){
            txtbeneficiario.text = "Si";
            txtbeneficiario.setTextColor(ContextCompat.getColor(this, R.color.green))
        }else{
            txtbeneficiario.text = "No";
            txtbeneficiario.setTextColor(ContextCompat.getColor(this, R.color.red))
        }

        initRecyclerView()

    }
    private fun initRecyclerView(){
        val recycleView: RecyclerView = binding.recyclerBeneficiarios
        recycleView.layoutManager = LinearLayoutManager(this);
        recycleView.adapter = BeneficiariosAdapter(BeneficiariosProvider.beneficiariosList)
    }
}