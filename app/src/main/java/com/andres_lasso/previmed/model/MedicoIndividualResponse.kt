package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class MedicoIndividualResponse(
    @SerializedName("id_medico") val id_medico: Int,
    @SerializedName("disponibilidad") val disponibilidad: Boolean,
    @SerializedName("estado") val estado: Boolean,
    @SerializedName("usuario_id") val usuario_id: String,
    @SerializedName("usuario") val usuario: UsuarioIndividualResponse?
)