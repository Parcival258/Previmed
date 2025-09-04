package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: UserData,

    @SerializedName("jwt")
    val jwt: String
)

data class UserData(
    @SerializedName("id")
    val id: String,

    @SerializedName("documento")
    val documento: String
)
