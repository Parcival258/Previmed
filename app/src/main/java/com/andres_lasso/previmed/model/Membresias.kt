package com.andres_lasso.previmed.model



data class Paciente(
    val idPaciente: Int?,
    val usuario: Usuario?,
    val direccionCobro: String?,
    val activo: Boolean?,
    val beneficiario: Boolean?
)

data class MembresiaPaciente(
    val pacienteId: Int?,
    val membresiaId: Int?,
    val idMembresiaXPaciente: Int?,
    val paciente: Paciente?
)

data class Membresia(
    val idMembresia: Int?,
    val firma: String?,
    val formaPago: String?,
    val numeroContrato: String?,
    val fechaInicio: String?,
    val fechaFin: String?,
    val estado: Boolean,
    val planId: Int?,
    val membresiaPaciente: List<MembresiaPaciente>?
)

data class MembresiaRequest(
    val firma: String,
    val formaPago: String,
    val numeroContrato: String,
    val fechaInicio: String,
    val fechaFin: String,
    val estado: Boolean,
    val planId: Int,
    val pacienteId: Int
)

