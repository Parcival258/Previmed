package com.andres_lasso.previmed.utils

import com.andres_lasso.previmed.model.MedicoIndividualResponse

object MedicoCache {
    private var medicos: List<MedicoIndividualResponse>? = null

    fun set(medicoList: List<MedicoIndividualResponse>) {
        medicos = medicoList
    }

    fun getNombre(idMedico: Int): String? {
        val med = medicos?.firstOrNull { it.idMedico == idMedico }
        return if (med?.usuario != null) {
            "${med.usuario.nombre} ${med.usuario.apellido}"
        } else {
            null
        }
    }
}