package com.andres_lasso.previmed.controller.asesor.recycler.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.recycler.BarriosClass

class BarriosAdapter(val barriosList:List<BarriosClass>): RecyclerView.Adapter<BarriosViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarriosViewHolder {
        val layaoutInflater = LayoutInflater.from(parent.context)
        return BarriosViewHolder(layaoutInflater.inflate(R.layout.item_barrios,parent, false))
    }

    override fun getItemCount(): Int {
        return barriosList.size
    }

    override fun onBindViewHolder(holder: BarriosViewHolder, position: Int) {
         val item = barriosList[position]
        holder.render(item)
    }
}