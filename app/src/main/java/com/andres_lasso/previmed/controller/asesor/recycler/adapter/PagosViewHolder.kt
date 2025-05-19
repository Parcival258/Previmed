package com.andres_lasso.previmed.controller.asesor.recycler.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.controller.asesor.recycler.PagosClass
import com.andres_lasso.previmed.databinding.ItemPagosBinding

class PagosViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemPagosBinding.bind(view)

    fun render(pagos: PagosClass){
        binding.tvNumRecibo.text = pagos.numRecibo
        binding.tvNumContrato.text = pagos.numContrato
        binding.tvTitular.text = pagos.titular
        binding.tvCobrador.text = pagos.cobrador
        binding.tvFechaCobro.text = pagos.fechaCobro
        binding.tvFechaInicio.text = pagos.fechaInicio
        binding.tvFechaFin.text = pagos.fechaFin
        binding.tvFormaPago.text = pagos.formaPago
        binding.tvMonto.text = pagos.monto
    }
}