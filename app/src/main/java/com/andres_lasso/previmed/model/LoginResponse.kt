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
    val documento: String,

    @SerializedName("rol")
    val rol: RolLogin
)

// 👇 Este Rol es el del login
data class RolLogin(
    @SerializedName("idRol")
    val idRol: Int,

    @SerializedName("nombreRol")
    val nombreRol: String,

    @SerializedName("estado")
    val estado: Boolean
)