package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class PacientesDataa(
    @SerializedName("idPaciente")
    val idPaciente: Int,
    @SerializedName("direccionCobro")
    val direccionCobro: String?,
    val ocupacion: String?,
    val activo: Boolean,
    val beneficiario: Boolean,
    @SerializedName("pacienteId")
    val pacienteId: Int?,
    @SerializedName("usuarioId")
    val usuarioId: String,
    val usuario: Usuario
)

data class Rol(
    @SerializedName("idRol")
    val idRol: Int,
    @SerializedName("nombreRol")
    val nombreRol: String,
    @SerializedName("estado")
    val estado: Boolean
)

data class RolesResponse(
    @SerializedName("msj")
    val msj: List<Rol>
)

// Modelo para EPS
data class Eps(
    @SerializedName("idEps")
    val idEps: Int,
    @SerializedName("nombreEps")
    val nombreEps: String,
    @SerializedName("estado")
    val estado: Boolean
)

data class EpsResponse(
    @SerializedName("msj")
    val msj: List<Eps>
)


data class PacienteDataa(
    val idPaciente: Int,
    val usuario: Usuario
    // otros campos omitidos porque ahora no se mostrarán
)
data class Usuariso(
    val nombre: String,
    val apellido: String,
    val numeroDocumento: String
// otros campos omitidos...
)