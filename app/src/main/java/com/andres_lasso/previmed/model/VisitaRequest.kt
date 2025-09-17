package com.andres_lasso.previmed.model

data class VisitaRequest(
    val paciente_id: Int,
    val medico_id: Int?,
    val barrio_id: Int,
    val direccion: String,
    val descripcion: String,
    val fecha_visita: String,
    val telefono: String
)
