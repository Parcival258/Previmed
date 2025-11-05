package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class Paciente(
    @SerializedName("idPaciente") val idPaciente: Int?,
    @SerializedName("usuario") val usuario: Usuario?,
    @SerializedName("direccionCobro") val direccionCobro: String?,
    @SerializedName("ocupacion") val ocupacion: String?,
    @SerializedName("activo") val activo: Boolean?,
    @SerializedName("beneficiario") val beneficiario: Boolean?,
    @SerializedName("pacienteId") val pacienteId: Int?, // ID del titular si aplica
    @SerializedName("usuarioId") val usuarioId: String?
)
