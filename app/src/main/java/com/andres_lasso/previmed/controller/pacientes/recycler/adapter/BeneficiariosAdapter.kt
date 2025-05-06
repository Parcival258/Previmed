package com.andres_lasso.previmed.controller.pacientes.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.pacientes.recycler.BeneficiariosClass

class BeneficiariosAdapter( private val beneficiariosList: List<BeneficiariosClass>): RecyclerView.Adapter<BeneficiariosViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BeneficiariosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BeneficiariosViewHolder(layoutInflater.inflate(R.layout.item_beneficiarios, parent, false))
    }

    override fun onBindViewHolder(
        holder: BeneficiariosViewHolder,
        position: Int
    ) {
        val item = beneficiariosList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return beneficiariosList.size
    }

}