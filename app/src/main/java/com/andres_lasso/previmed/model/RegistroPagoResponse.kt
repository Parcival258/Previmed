package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

// ==================== RESPUESTA DE MEMBRESÍA POR USUARIO ====================
data class MembresiaXUserResponse(
    @SerializedName("pacienteId")
    val pacienteId: Int,

    @SerializedName("membresiaId")
    val membresiaId: Int,

    @SerializedName("idMembresiaXPaciente")
    val idMembresiaXPaciente: Int,

    @SerializedName("membresia")
    val membresia: MembresiaData
)

data class MembresiaData(
    @SerializedName("idMembresia")
    val idMembresia: Int,

    @SerializedName("firma")
    val firma: String?,

    @SerializedName("formaPago")
    val formaPago: String?,

    @SerializedName("numeroContrato")
    val numeroContrato: String,

    @SerializedName("fechaInicio")
    val fechaInicio: String,

    @SerializedName("fechaFin")
    val fechaFin: String,

    @SerializedName("planId")
    val planId: Int,

    @SerializedName("estado")
    val estado: Boolean,

    @SerializedName("plan")
    val plan: PlanData
)

data class PlanData(
    @SerializedName("idPlan")
    val idPlan: Int,

    @SerializedName("tipoPlan")
    val tipoPlan: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("precio")
    val precio: String,

    @SerializedName("estado")
    val estado: Boolean,

    @SerializedName("cantidadBeneficiarios")
    val cantidadBeneficiarios: Int
)

// ==================== MODELO PARA LA UI DE PAGOS ====================
data class PagoMembresiaUI(
    val numeroContrato: String,
    val fechaInicio: String,
    val fechaFin: String,
    val fechaProximoPago: String,
    val titularMembresia: String,
    val plan: String,
    val beneficios: String
)

// ==================== RESPUESTA DE MEMBRESÍA ID (simple) ====================
data class MembresiaIdResponse(
    @SerializedName(value = "membresiaId", alternate = ["membresia_id"])
    val membresiaId: Int?
)

// ==================== REGISTROS DE PAGO (Opcional - para futuro) ====================
data class RegistroPagoResponse(
    @SerializedName("idRegistro")
    val idRegistro: Int,

    @SerializedName("monto")
    val monto: String,

    @SerializedName("fechaInicio")
    val fechaInicio: String,

    @SerializedName("fechaFin")
    val fechaFin: String,

    @SerializedName("membresiaId")
    val membresiaId: Int,

    @SerializedName("fechaPago")
    val fechaPago: String,

    @SerializedName("membresia")
    val membresia: MembresiaDetalle
)

data class MembresiaDetalle(
    @SerializedName("numeroContrato")
    val numeroContrato: String,

    @SerializedName("idMembresia")
    val idMembresia: Int,

    @SerializedName("membresiaPaciente")
    val membresiaPaciente: List<MembresiaXPaciente>
)

data class MembresiaXPaciente(
    @SerializedName("paciente")
    val paciente: PacienteDetalle
)

data class PacienteDetalle(
    @SerializedName("usuario")
    val usuario: UsuarioCompleto
)

data class UsuarioCompleto(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("segundoNombre")
    val segundoNombre: String?,

    @SerializedName("apellido")
    val apellido: String,

    @SerializedName("segundoApellido")
    val segundoApellido: String?
)
data class PagoResponse(
    val precio: String,
    val formaPago: String,
    val fechaInicio: String,
    val fechaFin: String,
    val fechaPago: String,
    val tipoPlan: String,
    val titular: String,
    val descripcionPlan: String
)


