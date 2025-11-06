package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class Visita(
    @SerializedName("idVisita") val idVisita: Int?,
    @SerializedName("fechaVisita") val fechaVisita: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("direccion") val direccion: String?,
    @SerializedName("estado") val estado: Boolean?,
    @SerializedName("pacienteId") val pacienteId: Int?,
    @SerializedName("medicoId") val medicoId: Int?,
    @SerializedName("telefono") val telefono: String?,
    @SerializedName("barrioId") val barrioId: Int?,
    @SerializedName("paciente") val paciente: Paciente?,
    @SerializedName("barrio") val barrio: Barrio?,
    @SerializedName("medico") val medico: Medico?,
    @SerializedName("nombreBarrio") val nombreBarrio: String? = null
)

data class VisitaResponse(
    @SerializedName("msj") val msj: List<Visita>?,
    @SerializedName("data") val data: List<Visita>?
)

data class VisitasRequest(
    @SerializedName("fecha_visita") val fechaVisita: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("estado") val estado: Boolean = true,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("paciente_id") val pacienteId: Int,
    @SerializedName("medico_id") val medicoId: Int,
    @SerializedName("barrio_id") val barrioId: Int
)