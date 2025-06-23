package com.andres_lasso.previmed.controller.medico.medicoDAO

import java.time.LocalDate


data class VisitasPendientesDao(
    val name: String,
    val secondName: String,
    val lastName: String,
    val secondLastName: String,
    val dni: Int,
    val address: String,
    val date: LocalDate,
    val hour: String,
    val description: String,
)
