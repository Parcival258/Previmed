package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("numero_documento")
    val numeroDocumento: String,

    @SerializedName("password")
    val password: String
)