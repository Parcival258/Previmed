package com.andres_lasso.previmed.model

data class PagoInfo(
    val numeroContrato: String,
    val fechaInicio: String,
    val fechaFin: String,
    val fechaProximoPago: String,
    val titularMembresia: String,
    val plan: String,
    val beneficios: String
)


