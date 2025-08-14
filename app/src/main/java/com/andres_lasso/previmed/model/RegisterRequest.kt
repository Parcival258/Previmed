package com.andres_lasso.previmed.model

data class RegisterRequest(
    val numero_documento: String,
    val password: String,
    val nombre: String,
    val email: String
)
