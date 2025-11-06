// Archivo: com.andres_lasso.previmed.utils.DateUtils.kt (NUEVO ARCHIVO)

package com.andres_lasso.previmed.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object DateUtils {

    private val UI_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun formatApiDate(apiDate: String): String {
        return try {
            val instant = Instant.parse(apiDate)
            val localDate = LocalDate.ofInstant(instant, ZoneId.of("UTC"))
            localDate.format(UI_FORMATTER)
        } catch (e: DateTimeParseException) {
            "Fecha Inválida"
        }
    }

    fun calculateNextPaymentDate(lastPaymentDate: String): String {
        return try {
            val instant = Instant.parse(lastPaymentDate)
            val localDate = LocalDate.ofInstant(instant, ZoneId.of("UTC"))

            // Asume ciclo de pago mensual
            val nextDate = localDate.plusMonths(1)

            nextDate.format(UI_FORMATTER)
        } catch (e: Exception) {
            "N/A (Error cálculo)"
        }
    }
}