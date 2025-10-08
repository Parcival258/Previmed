package com.andres_lasso.previmed.model

data class UsuarioMedico(
    val id_usuario: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val numero_documento: String
)

data class Medico(
    val id_medico: Int,
    val disponibilidad: Boolean,
    val estado: Boolean,
    val usuario_id: String,
    val usuario: Usuario
)

data class MedicosResponse(
    val data: List<Medico>?
)
