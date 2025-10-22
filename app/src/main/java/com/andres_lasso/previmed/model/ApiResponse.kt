package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("data")
    val data: T? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("mensaje")
    val mensaje: String? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("statusCode")
    val statusCode: Int? = null
)
