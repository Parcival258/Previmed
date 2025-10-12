package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class MedicoIndividualResponse(
    @SerializedName("id_medico")      val idMedico: Int,
    @SerializedName("disponibilidad") val disponibilidad: Boolean,
    @SerializedName("estado")         val estado: Boolean,
    @SerializedName("usuario_id")     val usuarioId: String,
    @SerializedName("usuario") val usuario: UsuarioMedico?
)

data class UsuarioIndividualResponse(
    @SerializedName("nombre")  val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("email")    val email: String,
    @SerializedName("numero_documento") val numeroDocumento: String
)