package com.andres_lasso.previmed.controller.medico

import com.andres_lasso.previmed.controller.medico.medicoDAO.VisitasPendientesDao
import java.time.LocalDate

val visitasPendientes = listOf(
    VisitasPendientesDao(
        name = "Carlos",
        secondName = "Andrés",
        lastName = "Lasso",
        secondLastName = "García",
        dni = 12345678,
        address = "Calle 45 #12-34",
        date = LocalDate.of(2025, 6, 25),
        hour = "08:00",
        description = "Consulta general"
    ),
    VisitasPendientesDao(
        name = "Laura",
        secondName = "María",
        lastName = "Rodríguez",
        secondLastName = "Pérez",
        dni = 23456789,
        address = "Cra 10 #50-20",
        date = LocalDate.of(2025, 6, 26),
        hour = "09:30",
        description = "Control postoperatorio"
    ),
    VisitasPendientesDao(
        name = "Pedro",
        secondName = "José",
        lastName = "González",
        secondLastName = "Martínez",
        dni = 34567890,
        address = "Av. Siempre Viva #742",
        date = LocalDate.of(2025, 6, 27),
        hour = "10:00",
        description = "Examen de rutina"
    ),
    VisitasPendientesDao(
        name = "Sofía",
        secondName = "Isabel",
        lastName = "Hernández",
        secondLastName = "López",
        dni = 45678901,
        address = "Calle Falsa 123",
        date = LocalDate.of(2025, 6, 28),
        hour = "11:15",
        description = "Seguimiento tratamiento"
    ),
    VisitasPendientesDao(
        name = "Martín",
        secondName = "",
        lastName = "Díaz",
        secondLastName = "Ramírez",
        dni = 56789012,
        address = "Carrera 7 #20-15",
        date = LocalDate.of(2025, 6, 29),
        hour = "14:00",
        description = "Consulta por dolencia"
    ),
    VisitasPendientesDao(
        name = "Valentina",
        secondName = "Lucía",
        lastName = "Sánchez",
        secondLastName = "Torres",
        dni = 67890123,
        address = "Diagonal 30 #5-50",
        date = LocalDate.of(2025, 6, 30),
        hour = "15:30",
        description = "Vacunación"
    )
)