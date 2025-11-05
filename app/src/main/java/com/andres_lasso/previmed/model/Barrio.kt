package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class Barrio(
    @SerializedName("idBarrio") val idBarrio: Int,
    @SerializedName("nombreBarrio") val nombreBarrio: String, //
    @SerializedName("latitud") val latitud: Double?,
    @SerializedName("longitud") val longitud: Double?,
    @SerializedName("estado") val estado: Boolean
)

data class BarriosResponse(
    @SerializedName("msj") val msj: List<Barrio>)
