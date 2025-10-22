package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

// -----------------------------
// ✅ USUARIO (implementa lo de UsuarioSimple.kt + campos extendidos)
// -----------------------------
data class Usuario(
    @SerializedName("idUsuario")
    val idUsuario: String?, // UUID (varchar)

    @SerializedName("nombre")
    val nombre: String?,

    @SerializedName("segundo_nombre")
    val segundoNombre: String? = null,

    @SerializedName("apellido")
    val apellido: String?,

    @SerializedName("segundo_apellido")
    val segundoApellido: String? = null,

    @SerializedName("email")
    val email: String?,

    @SerializedName("numero_documento")
    val numeroDocumento: String? = null,

    @SerializedName("fecha_nacimiento")
    val fechaNacimiento: String? = null,

    @SerializedName("numero_hijos")
    val numeroHijos: String? = null,

    @SerializedName("estrato")
    val estrato: String? = null,

    @SerializedName("autorizacion_datos")
    val autorizacionDatos: Boolean? = null,

    @SerializedName("eps_id")
    val epsId: Int? = null,

    @SerializedName("rol_id")
    val rolId: Int? = null,

    @SerializedName("habilitar")
    val habilitar: Boolean? = null,

    @SerializedName("genero")
    val genero: String? = null,

    @SerializedName("estado_civil")
    val estadoCivil: String? = null,

    @SerializedName("tipo_documento")
    val tipoDocumento: String? = null
)
