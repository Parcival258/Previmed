package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    // Mensaje principal del backend
    @SerializedName("message")
    val message: String? = null,

    // A veces el backend usa "msg" en lugar de "message"
    @SerializedName("msg")
    val msg: String? = null,

    // Token JWT (solo llega cuando el login es exitoso)
    @SerializedName("jwt")
    val jwt: String? = null,

    // Información del usuario (solo llega en login exitoso)
    @SerializedName("data")
    val data: UserData? = null
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