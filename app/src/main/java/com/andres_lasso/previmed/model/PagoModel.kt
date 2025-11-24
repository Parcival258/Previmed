package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

// -----------------------------
// ✅ MODELO PRINCIPAL DE PAGO
// -----------------------------
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

    @SerializedName("numeroRecibo")
    val numeroRecibo: String? = null,

    @SerializedName("cobradorId")
    val cobradorId: String? = null,

    @SerializedName("estado")
    val estado: String? = null,

    @SerializedName("formaPago")
    val formaPago: FormaPagoPago? = null,

    @SerializedName("cobrador")
    val cobrador: CobradorPago? = null, // 🆕 Añadido para coincidir con la respuesta del backend

    @SerializedName("membresia")
    val membresia: MembresiaPago? = null
)

// -----------------------------
// ✅ FORMA DE PAGO
// -----------------------------
data class FormaPagoPago(
    @SerializedName("tipoPago")
    val tipoPago: String?,

    @SerializedName("idFormaPago")
    val idFormaPago: Int?
)

data class PagosResponse(
    @SerializedName("data")
    val data: List<PagoModel>
)

// -----------------------------
// ✅ COBRADOR (NUEVO - parte de la respuesta del back)
// -----------------------------
data class CobradorPago(
    @SerializedName("idUsuario")
    val idUsuario: String?,

    @SerializedName("nombre")
    val nombre: String?,

    @SerializedName("segundoNombre")
    val segundoNombre: String?,

    @SerializedName("apellido")
    val apellido: String?,

    @SerializedName("segundoApellido")
    val segundoApellido: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("direccion")
    val direccion: String?,

    @SerializedName("numeroDocumento")
    val numeroDocumento: String?,

    @SerializedName("fechaNacimiento")
    val fechaNacimiento: String?,

    @SerializedName("numeroHijos")
    val numeroHijos: String?,

    @SerializedName("estrato")
    val estrato: String?,

    @SerializedName("autorizacionDatos")
    val autorizacionDatos: Boolean?,

    @SerializedName("epsId")
    val epsId: Int?,

    @SerializedName("rolId")
    val rolId: Int?,

    @SerializedName("habilitar")
    val habilitar: Boolean?,

    @SerializedName("genero")
    val genero: String?,

    @SerializedName("estadoCivil")
    val estadoCivil: String?,

    @SerializedName("tipoDocumento")
    val tipoDocumento: String?
)

// -----------------------------
// ✅ MEMBRESÍA
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
// ✅ RELACIÓN MEMBRESÍA - PACIENTE
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
    val usuario: UsuarioPago? = null
)

// -----------------------------
// ✅ USUARIO (anidado dentro del paciente)
// -----------------------------
data class UsuarioPago(
    @SerializedName("idUsuario")
    val idUsuario: String?,

    @SerializedName("nombre")
    val nombre: String?,

    @SerializedName("segundoNombre")
    val segundoNombre: String?,

    @SerializedName("apellido")
    val apellido: String?,

    @SerializedName("segundoApellido")
    val segundoApellido: String?,

    @SerializedName("email")
    val email: String?
)
