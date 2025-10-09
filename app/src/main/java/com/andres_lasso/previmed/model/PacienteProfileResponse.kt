package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class PacienteInfoData(
    @SerializedName("idPaciente")
    val id_paciente: Int,

    @SerializedName("direccionCobro")
    val direccion_cobro: String?,

    val ocupacion: String?,
    val activo: Boolean,
    val beneficiario: Boolean,

    @SerializedName("usuarioId")
    val usuario_id: String,

    @SerializedName("pacienteId")
    val paciente_id: Int?
)

data class PacienteInfoResponse(
    val message: String,
    val data: PacienteInfoData
)