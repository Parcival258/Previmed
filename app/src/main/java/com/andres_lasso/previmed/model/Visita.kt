package com.andres_lasso.previmed.model

data class Visita(
    val idVisita: Int,
    val fechaVisita: String,
    val descripcion: String,
    val direccion: String,
    val estado: Boolean,
    val pacienteId: Int,
    val medicoId: Int,
    val telefono: String,
    val barrioId: Int,
    val medico: Medico,
    val paciente: Paciente,
    val barrio: Barrio
)

data class VisitaResponse(
    val msj: List<Visita>

)

data class VisitasRequest(
    val fecha_visita: String,
    val descripcion: String?,
    val direccion: String,
    val estado: Boolean = true,
    val telefono: String,
    val paciente_id: Int,
    val medico_id: Int,
    val barrio_id: Int
)