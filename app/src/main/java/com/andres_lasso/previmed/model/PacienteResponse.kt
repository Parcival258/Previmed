package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

// -----------------------------
// ✅ MODELO PRINCIPAL
// -----------------------------
data class PacientesResponse(
    val message: String?,
    val data: List<PacienteData>? // ✅ lista de pacientes
)

// ✅ Para el POST /pacientes
data class PacienteCreadoResponse(
    val message: String?,
    val data: PacienteCreadoData?
)

data class PacienteCreadoData(
    @SerializedName("idPaciente") val idPaciente: Int,
    val direccionCobro: String?,
    val ocupacion: String?,
    val activo: Boolean?,
    val beneficiario: Boolean?,
    val usuarioId: String?
)


data class PacienteData(
    @SerializedName(value = "idPaciente", alternate = ["id_paciente"])
    val idPaciente: Int?,
    @SerializedName("direccion_cobro")
    val direccionCobro: String?,
    @SerializedName("ocupacion")
    val ocupacion: String?,
    @SerializedName("activo")
    val activo: Boolean?,
    @SerializedName("beneficiario")
    val beneficiario: Boolean?,
    @SerializedName("paciente_id")
    val pacienteId: Int?,
    @SerializedName("usuario_id")
    val usuarioId: String?,
    @SerializedName("usuario")
    val usuario: Usuario?,
    @SerializedName("membresia_paciente")
    val membresiaPaciente: List<MembresiaPaciente>? = emptyList()
)
data class PacienteResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: Paciente?
)

// Alias útil si más adelante usas respuestas genéricas
typealias PacientesListResponse = ApiResponse<List<PacienteData>>
