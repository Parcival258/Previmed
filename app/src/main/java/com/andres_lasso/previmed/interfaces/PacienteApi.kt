package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface PacienteApi {

    /** 🔹 Registrar usuario con objeto */
    @POST("usuarios")
    fun registrarUsuario(
        @Body user: Usuario
    ): Call<ApiResponse<Usuario>>

    /** 🔹 Registrar usuario con map */
    @POST("usuarios")
    fun registrarUsuarioMap(
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Call<ApiResponse<Usuario>>

    /** 🔹 Crear paciente (titular o beneficiario) */
    @POST("pacientes")
    fun registrarPaciente(@Body request: PacienteRequest): Call<PacienteCreadoResponse>

    /** 🔹 Crear beneficiario directo */
    @POST("pacientes/beneficiarios")
    fun registrarBeneficiario(
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Call<ApiResponse<PacienteData>>

    /** 🔹 Obtener todos los pacientes */
    @GET("pacientes")
    fun getPacientes(): Call<ApiResponse<List<PacienteData>>>

    /** 🔹 Obtener solo titulares (paciente_id = null) */
    @GET("pacientes/titular")
    fun getTitulares(): Call<ApiResponse<List<PacienteData>>>

    /** 🔹 Obtener solo beneficiarios (paciente_id = null) */
    @GET("pacientes/beneficiarios")
    fun getBeneficiarios(): Call<ApiResponse<List<PacienteData>>>

    /** 🔹 Obtener mi perfil */
    @GET("pacientes/mi-perfil")
    fun getMiPerfil(): Call<ApiResponse<PacienteData>>

    /** 🔹 Obtener paciente por usuario_id */
    @GET("pacientes/por-usuario/{usuario_id}")
    fun getPacienteByUsuarioId(
        @Path("usuario_id") usuarioId: String
    ): Call<ApiResponse<PacienteData>>

    /** 🔹 Obtener paciente por id */
    @GET("pacientes/{id}")
    suspend fun getPacienteById(
        @Path("id") id: Int
    ): Response<PacienteResponse>


    /** 🔹 Asociar beneficiario usando ID del beneficiario */
    @PUT("pacientes/asociar/{id}")
    fun asociarBeneficiario(
        @Path("id") beneficiarioId: Int,
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Call<ApiResponse<PacienteData>>

    /** 🔹 Alternativa: asociar beneficiario usando ID del titular */
    @PUT("pacientes/{id}/asociar-beneficiario")
    fun asociarBeneficiarioPorTitular(
        @Path("id") titularId: Int,
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Call<ApiResponse<PacienteData>>

    /** 🔹 Actualizar paciente por ID */
    @PUT("pacientes/{id}")
    fun updatePacienteById(
        @Path("id") id: Int,
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Call<ApiResponse<PacienteData>>

    /** 🔹 Eliminar paciente por ID */
    @DELETE("pacientes/{id}")
    fun deletePacienteById(
        @Path("id") id: Int
    ): Call<ApiResponse<Unit>>


}
