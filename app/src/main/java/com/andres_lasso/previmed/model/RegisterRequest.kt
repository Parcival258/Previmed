package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val nombre: String,
    @SerializedName("segundo_nombre") val segundoNombre: String? = null,
    val apellido: String,
    @SerializedName("segundo_apellido") val segundoApellido: String? = null,
    val email: String,
    val password: String,
    val direccion: String,
    @SerializedName("numero_documento") val numeroDocumento: String,
    @SerializedName("fecha_nacimiento") val fechaNacimiento: String,
    @SerializedName("numero_hijos") val numeroHijos: String? = null,
    val estrato: String? = null,
    @SerializedName("autorizacion_datos") val autorizacionDatos: Boolean,
    @SerializedName("eps_id") val epsId: Int? = null,
    @SerializedName("rol_id") val rolId: Int = 4,
    val habilitar: Boolean = true,
    val genero: String,
    @SerializedName("estado_civil") val estadoCivil: String,
    @SerializedName("tipo_documento") val tipoDocumento: String
)
data class Rol(
    val id: Int,
    val nombre: String
)

data class Eps(
    val id: Int,
    val nombre: String
)


