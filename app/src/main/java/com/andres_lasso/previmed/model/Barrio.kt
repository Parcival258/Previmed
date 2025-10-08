package com.andres_lasso.previmed.model

data class Barrio(
    val idBarrio: Int,
    val nombreBarrio: String,
    val latitud: Double?,
    val longitud: Double?,
    val estado: Boolean
)

data class BarriosResponse(
    val msj: List<Barrio>
)
