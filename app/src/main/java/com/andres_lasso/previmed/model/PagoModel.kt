package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class PagoModel(
    @SerializedName("idRegistro")
    val idRegistro: Int? = null,
    @SerializedName("monto")
    val monto: String? = null,
    @SerializedName("foto")
    val foto: String? = null,
    @SerializedName("fechaInicio")
    val fechaInicio: String? = null,
    @SerializedName("fechaFin")
    val fechaFin: String? = null,
    @SerializedName("fechaPago")
    val fechaPago: String? = null,
    @SerializedName("membresiaId")
    val membresiaId: Int? = null,
    @SerializedName("formaPagoId")
    val formaPagoId: Int? = null,
    @SerializedName("formaPago")
    val formaPago: FormaPagoPago? = null, // 👈 renombrado
    @SerializedName("membresia")
    val membresia: MembresiaPago? = null
)

// -----------------------------
// ✅ FORMA DE PAGO (renombrada)
// -----------------------------
data class FormaPagoPago(
    @SerializedName("tipoPago")
    val tipoPago: String?,
    @SerializedName("idFormaPago")
    val idFormaPago: Int?
)

// -----------------------------
// ✅ MEMBRESÍA (solo los campos relevantes para pago)
// -----------------------------
data class MembresiaPago(
    @SerializedName("numeroContrato")
    val numeroContrato: String?,

    @SerializedName("idMembresia")
    val idMembresia: Int?,

    @SerializedName("membresiaPaciente")
    val membresiaPaciente: List<MembresiaPacientePago>? = emptyList()
)

// -----------------------------
// ✅ RELACIÓN MEMBRESÍA-PACIENTE
// -----------------------------
data class MembresiaPacientePago(
    @SerializedName("pacienteId")
    val pacienteId: Int?,

    @SerializedName("membresiaId")
    val membresiaId: Int?,

    @SerializedName("idMembresiaXPaciente")
    val idMembresiaXPaciente: Int?,

    @SerializedName("paciente")
    val paciente: PacientePago? = null
)

// -----------------------------
// ✅ PACIENTE
// -----------------------------
data class PacientePago(
    @SerializedName("direccionCobro")
    val direccionCobro: String?,

    @SerializedName("usuarioId")
    val usuarioId: String?,

    @SerializedName("idPaciente")
    val idPaciente: Int?,

    @SerializedName("usuario")
    val usuario: Usuario? = null
)
