package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class PacientesResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: List<PacienteData>
)

data class PacienteData(
    @SerializedName("idPaciente")
    val idPaciente: Int,

    @SerializedName("direccionCobro")
    val direccionCobro: String?,

    @SerializedName("ocupacion")
    val ocupacion: String?,

    @SerializedName("activo")
    val activo: Boolean,

    @SerializedName("beneficiario")
    val beneficiario: Boolean,

    @SerializedName("pacienteId")
    val pacienteId: Int?,

    @SerializedName("usuarioId")
    val usuarioId: String,

    @SerializedName("usuario")
    val usuario: Usuario
)

data class Usuario(
    @SerializedName("idUsuario")
    val idUsuario: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("segundoNombre")
    val segundoNombre: String?,

    @SerializedName("apellido")
    val apellido: String,

    @SerializedName("segundoApellido")
    val segundoApellido: String?,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("direccion")
    val direccion: String,

    @SerializedName("numeroDocumento")
    val numeroDocumento: String,

    @SerializedName("fechaNacimiento")
    val fechaNacimiento: String,

    @SerializedName("numeroHijos")
    val numeroHijos: String?,

    @SerializedName("estrato")
    val estrato: String?,

    @SerializedName("autorizacionDatos")
    val autorizacionDatos: Boolean,

    @SerializedName("epsId")
    val epsId: Int?,

    @SerializedName("rolId")
    val rolId: Int,

    @SerializedName("habilitar")
    val habilitar: Boolean,

    @SerializedName("genero")
    val genero: String,

    @SerializedName("estadoCivil")
    val estadoCivil: String,

    @SerializedName("tipoDocumento")
    val tipoDocumento: String
)
