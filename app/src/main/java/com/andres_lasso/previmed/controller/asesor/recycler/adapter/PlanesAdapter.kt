package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.LayoutInflater
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
        var precio = holder.binding.tvPrecioPlan;

        precio.text = "$ ${item.precioPlan}"
    }

    override fun getItemCount(): Int {
        return planesList.size
    }
}