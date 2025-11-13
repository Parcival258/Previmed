package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

// ✅ MODELO para crear una membresía (Request al backend)
data class MembresiaRequest(
    @SerializedName("paciente_id") val pacienteId: Int?,
    @SerializedName("forma_pago_id") val formaPagoId: Int?,
    @SerializedName("plan_id") val planId: Int?,
    @SerializedName("numero_contrato") val numeroContrato: String?,
    @SerializedName("fecha_inicio") val fechaInicio: String?,
    @SerializedName("fecha_fin") val fechaFin: String?,
    @SerializedName("estado") val estado: Boolean = true,
    @SerializedName("firma") val firma: String? = null,
    @SerializedName("forma_pago") val formaPago: String? = null
)

// ✅ MODELO de respuesta del backend al crear la membresía
data class MembresiaResponse(
    val success: Boolean,
    val message: String?,
    val data: Membresia?
)

// ✅ MODELO principal de Membresía
data class Membresia(
    @SerializedName("idMembresia") val idMembresia: Int?,
    @SerializedName("firma") val firma: String?,
    @SerializedName("formaPago") val formaPago: String?,
    @SerializedName("numeroContrato") val numeroContrato: String?,
    @SerializedName("fechaInicio") val fechaInicio: String?,
    @SerializedName("fechaFin") val fechaFin: String?,
    @SerializedName("planId") val planId: Int?,
    @SerializedName("estado") val estado: Boolean?,
    @SerializedName("membresiaPaciente") val membresiaPaciente: List<MembresiaPaciente>?
)

// ✅ RELACIÓN entre membresía y paciente (sin recursión)
data class MembresiaPaciente(
    @SerializedName("idMembresiaXPaciente") val idMembresiaXPaciente: Int?,
    @SerializedName("pacienteId") val pacienteId: Int?,
    @SerializedName("membresiaId") val membresiaId: Int?,
    @SerializedName("paciente") val paciente: Paciente?, // referencia inversa hacia el paciente
    @SerializedName("membresia") val membresia: MembresiaSimple? // versión simplificada de membresía
)

// ✅ MODELO simplificado para evitar recursión infinita
data class MembresiaSimple(
    @SerializedName("idMembresia") val idMembresia: Int?,
    @SerializedName("firma") val firma: String?,
    @SerializedName("formaPago") val formaPago: String?,
    @SerializedName("numeroContrato") val numeroContrato: String?,
    @SerializedName("fechaInicio") val fechaInicio: String?,
    @SerializedName("fechaFin") val fechaFin: String?,
    @SerializedName("planId") val planId: Int?,
    @SerializedName("estado") val estado: Boolean?
)
