package com.andres_lasso.previmed.model

data class Visita(
    val fecha_visita: String,
    val descripcion: String,
    val direccion: String,
    val estado: String,
    val paciente_id: Int,
    val medico_id: Int?, // <- nullable
    val telefono: String,
    val barrio_id: Int
)
