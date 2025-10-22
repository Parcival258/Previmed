package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class FormaPago(
    @SerializedName("idFormaPago") val idFormaPago: Int,
    @SerializedName("tipoPago") val tipoPago: String?,
    @SerializedName("estado") val estado: Boolean
)
