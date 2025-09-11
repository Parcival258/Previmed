package com.andres_lasso.previmed.controller.asesor.recycler

data class BarriosClass(
    val idBarrio: Int,
    val nombreBarrio: String,
    val latitud: Double?,
    val longitud: Double?,
    val habilitar: Boolean,
    val ciudad: String? = null,
    val comuna: String? = null
)
