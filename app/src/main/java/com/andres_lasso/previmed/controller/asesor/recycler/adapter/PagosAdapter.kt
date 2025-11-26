package com.andres_lasso.previmed.controller.asesor.recycler

import android.graphics.Color
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

    // 🔍 LISTAS PARA FILTRO
    private var listaOriginal: List<PagoModel> = emptyList()
    private var listaFiltrada: List<PagoModel> = emptyList()

    private val itemsExpandidos = mutableSetOf<Int>()

    inner class PagosViewHolder(val binding: ItemPagosBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagosViewHolder {
        val binding =
            ItemPagosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagosViewHolder, position: Int) {

        val pago = listaFiltrada[position]   // ← USAMOS LISTA FILTRADA
        val b = holder.binding

        Log.d("PagosAdapter", "==== Pago posición $position =====")
        Log.d("PagosAdapter", "numeroRecibo: ${pago.numeroRecibo}")
        Log.d("PagosAdapter", "numeroContrato: ${pago.membresia?.numeroContrato}")

        // TITULAR
        val titular = pago.membresia
            ?.membresiaPaciente
            ?.firstOrNull()
            ?.paciente
            ?.usuario

        // CABECERA
        b.tvNumRecibo.text = pago.numeroRecibo ?: "N/A"
        b.tvNumContrato.text = pago.membresia?.numeroContrato ?: "N/A"

        b.tvTitular.text = listOfNotNull(
            titular?.nombre,
            titular?.apellido
        ).joinToString(" ").ifEmpty { "Sin titular" }

        b.tvMontoCabecera.text = "Precio: $${pago.monto ?: 0}"

        // ESTADO
        val estado = pago.estado?.trim()?.uppercase() ?: "N/A"
        b.tvEstadoCabecera.text = estado

        when (estado) {
            "ASIGNADO" -> b.tvEstadoCabecera.setTextColor(Color.parseColor("#1976D2"))
            "PENDIENTE" -> b.tvEstadoCabecera.setTextColor(Color.RED)
            "REALIZADO" -> b.tvEstadoCabecera.setTextColor(Color.parseColor("#FF9800"))
            "APROBADO" -> b.tvEstadoCabecera.setTextColor(Color.GREEN)
            else -> b.tvEstadoCabecera.setTextColor(Color.BLACK)
        }

        // SECCIÓN EXPANDIBLE
        b.tvCobrador.text = "Cobrador: ${listOfNotNull(
            pago.cobrador?.nombre,
            pago.cobrador?.apellido
        ).joinToString(" ").ifEmpty { "N/A" }}"

        b.tvFechaCobro.text = "Fecha cobro: ${pago.fechaPago ?: "N/A"}"
        b.tvFechaInicio.text = "Inicio: ${pago.fechaInicio ?: "N/A"}"
        b.tvFechaFin.text = "Fin: ${pago.fechaFin ?: "N/A"}"
        b.tvFormaPago.text = "Forma de pago: ${pago.formaPago?.tipoPago ?: "N/A"}"
        b.tvMonto.text = "Monto total: ${pago.monto ?: "N/A"}"
        b.tvEstado.text = "Estado: $estado"

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

    override fun getItemCount(): Int = listaFiltrada.size  // ← LISTA FILTRADA

    fun setData(nuevaLista: List<PagoModel>) {
        listaOriginal = nuevaLista
        listaFiltrada = nuevaLista
        notifyDataSetChanged()
    }

    // 🔍 FILTRO POR NOMBRE O NÚMERO DE RECIBO
    fun filtrar(texto: String) {
        val query = texto.lowercase()

        listaFiltrada = if (query.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter { pago ->

                val nombreCompleto = listOfNotNull(
                    pago.membresia?.membresiaPaciente?.firstOrNull()
                        ?.paciente?.usuario?.nombre,
                    pago.membresia?.membresiaPaciente?.firstOrNull()
                        ?.paciente?.usuario?.apellido
                ).joinToString(" ").lowercase()

                val numeroRecibo = pago.numeroRecibo?.lowercase() ?: ""

                // 🎯 BUSCAR SOLO POR NOMBRE O NÚMERO RECIBO
                nombreCompleto.contains(query) ||
                        numeroRecibo.contains(query)
            }
        }

        notifyDataSetChanged()
    }
}
