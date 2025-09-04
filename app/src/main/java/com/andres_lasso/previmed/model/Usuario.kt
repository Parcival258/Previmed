package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("idUsuario")
    val idUsuario: String,
    val nombre: String,
    @SerializedName("segundoNombre")
    val segundoNombre: String?,
    val apellido: String,
    @SerializedName("segundoApellido")
    val segundoApellido: String?,
    val email: String,
    val password: String,
    val direccion: String,
    @SerializedName("numeroDocumento")
    val numeroDocumento: String,
    @SerializedName("fechaNacimiento")
    val fechaNacimiento: String,
    @SerializedName("numeroHijos")
    val numeroHijos: String?,
    val estrato: String?,
    @SerializedName("autorizacionDatos")
    val autorizacionDatos: Boolean,
    @SerializedName("epsId")
    val epsId: Int?,
    @SerializedName("rolId")
    val rolId: Int,
    val habilitar: Boolean,
    val genero: String,
    @SerializedName("estadoCivil")
    val estadoCivil: String,
    @SerializedName("tipoDocumento")
    val tipoDocumento: String
)
