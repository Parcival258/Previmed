package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class UsuarioMedico(
    @SerializedName("id_usuario") val id_usuario: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("email") val email: String,
    @SerializedName("numero_documento") val numero_documento: String
)

data class Medico(
    @SerializedName("id_medico") val id_medico: Int,
    @SerializedName("disponibilidad") var disponibilidad: Boolean?,
    @SerializedName("estado") var estado: Boolean?,
    @SerializedName("usuario_id") val usuario_id: String,
    @SerializedName("usuario") val usuario: UsuarioMedico
)

// 👇 Si tu backend devuelve { "msj": { ... } }
data class MedicoResponse(
    @SerializedName("data") val data: Medico?,
    @SerializedName("msj") val msj: Medico
)
{
    // Un único punto de acceso seguro al médico
    val medicoOrNull: Medico?
        get() = data ?: msj
}

data class MedicoUpdateRequest(
    val disponibilidad: Boolean,
    val estado: Boolean
)
