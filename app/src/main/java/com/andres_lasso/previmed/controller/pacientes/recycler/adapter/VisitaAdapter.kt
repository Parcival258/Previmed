package com.andres_lasso.previmed.controller.pacientes.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.databinding.ItemVisitaBinding
import com.andres_lasso.previmed.model.Visita
import com.andres_lasso.previmed.utils.MedicoCache

class VisitaAdapter(
    private var visitas: List<Visita>,
    private val onCancelClick: (Visita) -> Unit
) : RecyclerView.Adapter<VisitaAdapter.VisitaViewHolder>() {

    inner class VisitaViewHolder(val binding: ItemVisitaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitaViewHolder {
        val binding = ItemVisitaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VisitaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VisitaViewHolder, position: Int) {
        val visita = visitas[position]

        val fecha = visita.fechaVisita.take(10)
        holder.binding.tvFecha.text = "Visita Pendiente $fecha"

        val idMedico = visita.medicoId
        val nombre = MedicoCache.getNombre(idMedico)
        holder.binding.tvMedico.text = nombre ?: "Cargando médico..."



        holder.binding.btnCancelar.setOnClickListener { onCancelClick(visita) }
    }

    override fun getItemCount() = visitas.size

    fun submitList(newList: List<Visita>) {
        visitas = newList
        notifyDataSetChanged()
    }
}