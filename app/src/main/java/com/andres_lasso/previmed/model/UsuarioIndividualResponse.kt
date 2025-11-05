package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class UsuarioIndividualResponse (
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("email") val email: String,
    @SerializedName("numero_documento") val numeroDocumento: String
)