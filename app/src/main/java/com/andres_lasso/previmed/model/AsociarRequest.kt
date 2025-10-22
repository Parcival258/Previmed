package com.andres_lasso.previmed.model

data class AsociarRequest(
    val paciente_id: Int,     // ID del titular
    val beneficiario: Boolean = true
)
