package com.andres_lasso.previmed.controller.asesor.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.ItemPagosBinding
import com.andres_lasso.previmed.model.PagoModel

class PagosAdapter :
    RecyclerView.Adapter<PagosAdapter.PagosViewHolder>() {

    private var listaPagos: List<PagoModel> = emptyList()
    private val itemsExpandidos = mutableSetOf<Int>()

    inner class PagosViewHolder(val binding: ItemPagosBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagosViewHolder {
        val binding =
            ItemPagosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagosViewHolder, position: Int) {
        val pago = listaPagos[position]
        val b = holder.binding

        // LOGS
        Log.d("PagosAdapter", "---- Pago en posición $position ----")
        Log.d("PagosAdapter", "numeroRecibo: ${pago.numeroRecibo}")
        Log.d("PagosAdapter", "membresia: ${pago.membresia}")
        Log.d("PagosAdapter", "numeroContrato: ${pago.membresia?.numeroContrato}")
        Log.d("PagosAdapter", "fechaPago: ${pago.fechaPago}")
        Log.d("PagosAdapter", "fechaInicio: ${pago.fechaInicio}")
        Log.d("PagosAdapter", "fechaFin: ${pago.fechaFin}")
        Log.d("PagosAdapter", "monto: ${pago.monto}")
        Log.d("PagosAdapter", "estado: ${pago.estado}")
        Log.d("PagosAdapter", "formaPago: ${pago.formaPago}")
        Log.d("PagosAdapter", "cobrador: ${pago.cobrador}")

        val titular = pago.membresia
            ?.membresiaPaciente
            ?.firstOrNull()
            ?.paciente
            ?.usuario

        Log.d("PagosAdapter", "Titular: ${titular?.nombre} ${titular?.apellido}")

        // DATOS PARA LA VISTA
        b.tvNumRecibo.text = pago.numeroRecibo ?: "N/A"
        b.tvNumContrato.text = pago.membresia?.numeroContrato ?: "N/A"

        b.tvTitular.text = listOfNotNull(
            titular?.nombre,
            titular?.apellido
        ).joinToString(" ").ifEmpty { "Sin titular" }

        b.tvCobrador.text = "Cobrador: ${listOfNotNull(
            pago.cobrador?.nombre,
            pago.cobrador?.apellido
        ).joinToString(" ").ifEmpty { "N/A" }}"

        b.tvFechaCobro.text = "Fecha cobro: ${pago.fechaPago ?: "N/A"}"
        b.tvFechaInicio.text = "Inicio: ${pago.fechaInicio ?: "N/A"}"
        b.tvFechaFin.text = "Fin: ${pago.fechaFin ?: "N/A"}"

        b.tvFormaPago.text = "Forma de pago: ${pago.formaPago?.tipoPago ?: "N/A"}"
        b.tvMonto.text = "Monto total: ${pago.monto ?: "N/A"}"
        b.tvEstado.text = "Estado: ${pago.estado ?: "N/A"}"

        // 🔥 NUEVO → PRECIO DEL PAGO
        b.tvMontoCabecera.text = "Precio: $${pago.monto ?: 0}"

        // EXPANDIR / COLAPSAR
        val expanded = itemsExpandidos.contains(position)
        b.expandableLayout.visibility = if (expanded) View.VISIBLE else View.GONE
        b.arrow.rotation = if (expanded) 180f else 0f

        b.cardRoot.setOnClickListener {
            if (expanded) {
                itemsExpandidos.remove(position)
                b.expandableLayout.startAnimation(
                    AnimationUtils.loadAnimation(b.root.context, R.anim.collapse_anim)
                )
                b.expandableLayout.visibility = View.GONE
                b.arrow.animate().rotation(0f).start()

            } else {
                itemsExpandidos.add(position)
                b.expandableLayout.startAnimation(
                    AnimationUtils.loadAnimation(b.root.context, R.anim.expand_anim)
                )
                b.expandableLayout.visibility = View.VISIBLE
                b.arrow.animate().rotation(180f).start()
            }
        }
    }

    override fun getItemCount(): Int = listaPagos.size

    fun setData(nuevaLista: List<PagoModel>) {
        Log.d("PagosAdapter", "setData() lista recibida: $nuevaLista")
        listaPagos = nuevaLista
        notifyDataSetChanged()
    }
}
