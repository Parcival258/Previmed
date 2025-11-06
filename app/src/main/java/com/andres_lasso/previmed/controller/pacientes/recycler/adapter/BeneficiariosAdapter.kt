package com.andres_lasso.previmed.controller.pacientes.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.Beneficiario

class BeneficiariosAdapter(
    private var lista: List<Beneficiario>
) : RecyclerView.Adapter<BeneficiariosAdapter.BeneficiarioViewHolder>() {

    inner class BeneficiarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvBeneficiarioNombre)
        val tvApellido: TextView = view.findViewById(R.id.tvBeneficiarioApellido)
        val tvDireccion: TextView = view.findViewById(R.id.tvBeneficiarioDireccion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeneficiarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_beneficiario, parent, false)
        return BeneficiarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: BeneficiarioViewHolder, position: Int) {
        val ben = lista[position]
        holder.tvNombre.text = ben.usuario?.nombre ?: "-"
        holder.tvApellido.text = ben.usuario?.apellido ?: "-"
        holder.tvDireccion.text = ben.direccionCobro ?: "-"
    }

    override fun getItemCount(): Int = lista.size

    fun submitList(nuevaLista: List<Beneficiario>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
