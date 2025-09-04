package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PacienteApi {
    @POST("pacientes")
    fun registrarPaciente(@Body request: PacienteRequest): Call<PacienteResponse>
}

interface RolesApi {
    @GET("roles")
    fun getRoles(): Call<RolesResponse>
}

interface EpsApi {
    @GET("eps")
    fun getEps(): Call<EpsResponse>
}
