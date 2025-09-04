package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class PlanesResponse(
    @SerializedName("msj")
    val planes: List<Plan>
)

data class Plan(
    @SerializedName("idPlan")
    val idPlan: Int,
    @SerializedName("tipoPlan")
    val tipoPlan: String,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("precio")
    val precio: String,
    @SerializedName("estado")
    val estado: Boolean,
    @SerializedName("cantidadBeneficiarios")
    val cantidadBeneficiarios: Int
)
