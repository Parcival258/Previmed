package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class Paciente(
    @SerializedName("id") val id: Int?,
    @SerializedName("usuario") val usuario: Usuario?,
    @SerializedName("paciente_id") val pacienteId: Int?, // 🔹 ID del titular si este paciente es beneficiario
    @SerializedName("beneficiario") val beneficiario: Boolean? // 🔹 True si recibe beneficios
)
