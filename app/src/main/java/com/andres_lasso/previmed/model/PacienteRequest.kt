package com.andres_lasso.previmed.model

data class PacienteRequest(
    val nombre: String,
    val segundo_nombre: String? = null,
    val apellido: String,
    val segundo_apellido: String? = null,
    val email: String,
    val password: String,
    val direccion: String,
    val numero_documento: String,
    val fecha_nacimiento: String,
    val numero_hijos: Int? = null,
    val estrato: Int? = null,
    val autorizacion_datos: Boolean,
    val habilitar: Boolean = true,
    val genero: String,
    val estado_civil: String,
    val tipo_documento: String,
    val eps_id: Int = 1,   // Valor por defecto EPS
    val rol_id: Int = 1,   // Valor por defecto Rol
    val direccion_cobro: String,
    val ocupacion: String? = null,
    val activo: Boolean = true,
    val beneficiario: Boolean = true,

)
