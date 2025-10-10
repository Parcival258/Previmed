package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class UsuarioMedico(
    val id_usuario: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val numero_documento: String
)

data class Medico(
    val id_medico: Int,
    val disponibilidad: Boolean,
    val estado: Boolean,
    val usuario_id: String,
    val usuario: UsuarioMedico
)

    data class MedicosListResponse(
    @SerializedName("data") val data: List<MedicoIndividualResponse>
)