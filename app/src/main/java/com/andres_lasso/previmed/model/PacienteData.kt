    package com.andres_lasso.previmed.model

    import com.google.gson.annotations.SerializedName

    data class Rol(
        @SerializedName("idRol")
        val idRol: Int,
        @SerializedName("nombreRol")
        val nombreRol: String,
        @SerializedName("estado")
        val estado: Boolean
    )

    data class Eps(
        @SerializedName("idEps")
        val idEps: Int,
        @SerializedName("nombreEps")
        val nombreEps: String,
        @SerializedName("estado")
        val estado: Boolean
    )


    data class EpsResponse(
        val epsList: List<Eps> // el nombre no importa, pero debe coincidir con el tipo esperado
    )



