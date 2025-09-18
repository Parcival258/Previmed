package com.andres_lasso.previmed.model

data class BarriosResponse(
    val idBarrio: Int,
    val nombreBarrio: String,
    val latitud: Double,
    val longitud: Double,
    val estado: Boolean
)
