package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.ApiResponse
import com.andres_lasso.previmed.model.MedicoResponse
import com.andres_lasso.previmed.model.MedicoUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface MedicoApi {

    @GET("medicos/usuario/{usuario_id}")
    suspend fun obtenerMedicoPorUsuario(@Path("usuario_id") usuarioId: String): Response<MedicoResponse>

    @GET("medicos/{id}")
    suspend fun obtenerMedicoPorId(@Path("id") id: Int): Response<MedicoResponse>

    @PUT("medicos/{id}")
    suspend fun actualizarMedico(
        @Path("id") idMedico: Int,
        @Body body: MedicoUpdateRequest
    ): Response<ApiResponse<MedicoResponse>>






}