package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.PlanesClass

class PlanesAdapter(private  val planesList:List<PlanesClass>): RecyclerView.Adapter<PlanesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int):PlanesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlanesViewHolder(layoutInflater.inflate(R.layout.item_planes, parent, false))
    }

    override fun onBindViewHolder(holder: PlanesViewHolder, position: Int) {
        val item = planesList[position]
        holder.render(item)

        var expand_card_beneficios = holder.binding.ExpandCardPlanes;
        var beneficios = holder.binding.beneficios
        var item_card_planes = holder.binding.itemCardPlanes
        val text_Ver_Detalles = holder.binding.verBeneficios
        val iconArrow = holder.binding.iconArrow
        val precio= holder.binding.tvPrecioPlan

        holder.binding.btnVerBeneficios.setOnClickListener {
            val isVisible = expand_card_beneficios.visibility == View.VISIBLE
            TransitionManager.beginDelayedTransition(item_card_planes, AutoTransition())

            if(isVisible){
                expand_card_beneficios.visibility=View.GONE
                text_Ver_Detalles.text="Ver Beneficios"
                iconArrow.animate().rotation(0f).start()

            }else{
                expand_card_beneficios.visibility=View.VISIBLE
                text_Ver_Detalles.text="Ver Beneficios"
                iconArrow.animate().rotation(180f).start()
            }
        }

        precio.text="$ ${item.precioPlan}"
        beneficios.text = item.beneficios.joinToString("\n")

    }

    override fun getItemCount(): Int {
        return planesList.size
    }
}