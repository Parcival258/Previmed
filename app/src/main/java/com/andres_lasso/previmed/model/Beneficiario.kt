package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class Usuari(
    @SerializedName("idUsuario") val idUsuario: String,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("segundoNombre") val segundoNombre: String?,
    @SerializedName("apellido") val apellido: String?,
    @SerializedName("segundoApellido") val segundoApellido: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("numeroDocumento") val numeroDocumento: String?,
    @SerializedName("direccion") val direccion: String?,
    @SerializedName("fechaNacimiento") val fechaNacimiento: String?,
    @SerializedName("genero") val genero: String?
)

data class Beneficiario(
    @SerializedName("idPaciente") val idPaciente: Int,
    @SerializedName("pacienteId") val pacienteId: Int,
    @SerializedName("activo") val activo: Boolean,
    @SerializedName("beneficiario") val beneficiario: Boolean,
    @SerializedName("direccionCobro") val direccionCobro: String?,
    @SerializedName("usuario") val usuario: Usuario?
)

data class BeneficiariosResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Beneficiario>
)
