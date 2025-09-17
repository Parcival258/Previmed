package com.andres_lasso.previmed.model

data class VisitaResponse(
    val msj: String,
    val data: DataVisita
)

data class DataVisita(
    val id: Int,
    val direccion: String,
    val descripcion: String,
    val medico_id: Int?
)
