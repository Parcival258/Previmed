package com.andres_lasso.previmed.model
data class LoginRequest(
    val numero_documento: String,
    val password: String
)