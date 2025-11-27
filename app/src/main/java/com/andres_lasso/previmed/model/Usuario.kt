package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class Usuario(

    @SerializedName("idUsuario")
    val idUsuario: String? = null,

    @SerializedName("nombre")
    val nombre: String? = null,

    @SerializedName("segundoNombre")
    val segundoNombre: String? = null,

    @SerializedName("apellido")
    val apellido: String? = null,

    @SerializedName("segundoApellido")
    val segundoApellido: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("direccion")
    val direccion: String? = null,

    @SerializedName("numeroDocumento")
    val numeroDocumento: String? = null,

    @SerializedName("fechaNacimiento")
    val fechaNacimiento: String? = null,

    @SerializedName("numeroHijos")
    val numeroHijos: String? = null,

    @SerializedName("estrato")
    val estrato: String? = null,

    @SerializedName("autorizacionDatos")
    val autorizacionDatos: Boolean? = null,

    @SerializedName("epsId")
    val epsId: Int? = null,

    @SerializedName("rolId")
    val rolId: Int? = null,

    @SerializedName("habilitar")
    val habilitar: Boolean? = null,

    @SerializedName("genero")
    val genero: String? = null,

    @SerializedName("estadoCivil")
    val estadoCivil: String? = null,

    @SerializedName("tipoDocumento")
    val tipoDocumento: String? = null
)
