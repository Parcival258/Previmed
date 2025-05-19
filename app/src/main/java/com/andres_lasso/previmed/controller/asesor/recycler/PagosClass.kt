package com.andres_lasso.previmed.controller.asesor.recycler

data class PagosClass(
    val idPago: Int,
    val numRecibo: String,
    val numContrato: String,
    val titular: String,
    val cobrador: String,
    val fechaCobro: String,
    val fechaInicio: String,
    val fechaFin: String,
    val formaPago: String,
    val monto: String
)