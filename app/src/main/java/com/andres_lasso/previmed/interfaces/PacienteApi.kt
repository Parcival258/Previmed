package com.andres_lasso.previmed.interfaces

import PacienteClass
import com.andres_lasso.previmed.model.*

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PacienteApi {
    @POST("pacientes")
    fun registrarPaciente(@Body request: PacienteRequest): Call<PacientesResponse>

    @GET("pacientes")
    fun getPacientes(): Call<PacientesResponse>

    @GET("pacientes/por-usuario/{usuario_id}")
    suspend fun getPacienteByUsuarioId(@Path("usuario_id") usuarioId: String): Response<PacienteInfoResponse>

    interface RolesApi {
        @GET("roles")
        fun getRoles(): Call<RolesResponse>
    }

    interface EpsApi {
        @GET("eps")
        fun getEps(): Call<EpsResponse>
    }

}


