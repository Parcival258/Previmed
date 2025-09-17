package com.andres_lasso.previmed.model

data class MedicoRequest(
    val nombre: String,
    val especialidad: String? = null,
    val telefono: String? = null,
    val correo: String? = null
)
