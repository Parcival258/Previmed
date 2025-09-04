package com.andres_lasso.previmed.model

data class PacienteResponse(
    val id: Int,
    val nombre: String?,
    val email: String?,
    val mensaje: String?,
    val data: PacienteData?
)
