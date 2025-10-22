package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class PacienteRequest(
    @SerializedName("usuario_id") val usuarioId: String? = null,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("segundo_nombre") val segundoNombre: String? = null,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("segundo_apellido") val segundoApellido: String? = null,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("numero_documento") val numeroDocumento: String,
    @SerializedName("fecha_nacimiento") val fechaNacimiento: String? = null,
    @SerializedName("numero_hijos") val numeroHijos: Int? = null,
    @SerializedName("estrato") val estrato: Int? = null,
    @SerializedName("autorizacion_datos") val autorizacionDatos: Boolean? = null,
    @SerializedName("habilitar") val habilitar: Boolean? = null,
    @SerializedName("genero") val genero: String? = null,
    @SerializedName("estado_civil") val estadoCivil: String? = null,
    @SerializedName("tipo_documento") val tipoDocumento: String? = null,
    @SerializedName("eps_id") val epsId: Int? = null,
    @SerializedName("rol_id") val rolId: Int? = null,
    @SerializedName("direccion_cobro") val direccionCobro: String? = null,
    @SerializedName("ocupacion") val ocupacion: String? = null,
    @SerializedName("activo") val activo: Boolean? = null,
    @SerializedName("beneficiario") val beneficiario: Boolean? = null,
    @SerializedName("paciente_id") val pacienteId: Int? = null, // 🔹 si es beneficiario
    @SerializedName("plan_id") val planId: Int? = null,
    @SerializedName("numero_contrato") val numeroContrato: String? = null,
    @SerializedName("fecha_inicio_contrato") val fechaInicioContrato: String? = null,
    @SerializedName("fecha_fin_contrato") val fechaFinContrato: String? = null,
    @SerializedName("forma_pago") val formaPago: String? = null
)
