package com.andres_lasso.previmed.controller.pacientes.recycler.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.pacientes.recycler.BeneficiariosClass

class BeneficiariosViewHolder (view: View): RecyclerView.ViewHolder(view){

    val nombre = view.findViewById<TextView>(R.id.tvBeneficiariosName)
    val apellido = view.findViewById<TextView>(R.id.tvBeneficiariosApellido)

    fun render(beneficiarios: BeneficiariosClass){
        nombre.text = beneficiarios.nombre
        apellido.text = beneficiarios.apellido
    }
}