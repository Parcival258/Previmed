package com.andres_lasso.previmed.model

data class LoginResponse(
    val token: String?,   // si tu backend devuelve token
    val message: String?, // si tu backend devuelve mensaje
    val rol: String?      // si tu backend devuelve rol
)
