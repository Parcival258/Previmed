package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.*
import retrofit2.Call
import retrofit2.http.*

interface PacienteApi {

    // --------------------------------------------------------------------
    // 🔹 USUARIOS
    // --------------------------------------------------------------------
    @POST("usuarios")
    fun registrarUsuario(
        @Body user: Usuario
    ): Call<ApiResponse<Usuario>>

    @POST("usuarios")
    fun registrarUsuarioMap(
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Call<ApiResponse<Usuario>>

    // --------------------------------------------------------------------
    // 🔹 PACIENTES
    // --------------------------------------------------------------------
    // Crear paciente (titular o beneficiario)
    @POST("pacientes")
    fun registrarPaciente(@Body request: PacienteRequest): Call<PacienteCreadoResponse>

    // Crear beneficiario directo
    @POST("pacientes/beneficiarios")
    fun registrarBeneficiario(
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Call<ApiResponse<PacienteData>>

    // --------------------------------------------------------------------
    // 🔹 CONSULTAS Y LISTADOS
    // --------------------------------------------------------------------
    @GET("pacientes")
    fun getPacientes(): Call<ApiResponse<List<PacienteData>>>

    @GET("pacientes/titular")
    fun getTitulares(): Call<ApiResponse<List<PacienteData>>>

    @GET("pacientes/beneficiarios")
    fun getBeneficiarios(): Call<ApiResponse<List<PacienteData>>>

    @GET("pacientes/mi-perfil")
    fun getMiPerfil(): Call<ApiResponse<PacienteData>>

    // --------------------------------------------------------------------
    // 🔹 BÚSQUEDAS INDIVIDUALES
    // --------------------------------------------------------------------
    @GET("pacientes/por-usuario/{usuario_id}")
    fun getPacienteByUsuarioId(
        @Path("usuario_id") usuarioId: String
    ): Call<ApiResponse<PacienteData>>

    @GET("pacientes/{id}")
    fun getPacienteById(
        @Path("id") id: Int
    ): Call<ApiResponse<PacienteData>>

    // --------------------------------------------------------------------
    // 🔹 ASOCIAR BENEFICIARIO
    // --------------------------------------------------------------------
    // Versión principal → usa el ID del beneficiario
    @PUT("pacientes/asociar/{id}")
    fun asociarBeneficiario(
        @Path("id") beneficiarioId: Int,
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Call<ApiResponse<PacienteData>>

    // Alternativa opcional → si el backend usa el ID del titular
    @PUT("pacientes/{id}/asociar-beneficiario")
    fun asociarBeneficiarioPorTitular(
        @Path("id") titularId: Int,
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Call<ApiResponse<PacienteData>>

    // --------------------------------------------------------------------
    // 🔹 MODIFICACIÓN Y ELIMINACIÓN
    // --------------------------------------------------------------------
    @PUT("pacientes/{id}")
    fun updatePacienteById(
        @Path("id") id: Int,
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Call<ApiResponse<PacienteData>>

    @DELETE("pacientes/{id}")
    fun deletePacienteById(
        @Path("id") id: Int
    ): Call<ApiResponse<Unit>>
}
