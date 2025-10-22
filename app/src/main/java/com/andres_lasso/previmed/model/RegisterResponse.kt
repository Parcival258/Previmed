package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("idUsuario") val idUsuario: String?,
    val nombre: String?,
    val email: String?
)
