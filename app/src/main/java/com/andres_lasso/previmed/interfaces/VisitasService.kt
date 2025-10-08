package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.BarriosResponse
import com.andres_lasso.previmed.model.MedicosResponse
import com.andres_lasso.previmed.model.VisitaResponse
import com.andres_lasso.previmed.model.VisitasRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE

interface VisitaService {
    @GET("visitas/paciente/{id}")
    suspend fun getVisitas(@Path("id") pacienteId: Int): Response<VisitaResponse>

    @POST("visitas")
    suspend fun crearVisita(@Body visitas: VisitasRequest): Response<Any>

    @DELETE("visitas/{id}")
    suspend fun cancelarVisita(@Path("id") visitaId: String)

    @GET("medicos")
    suspend fun getMedicos(): Response<MedicosResponse>

    @GET("barrios")
    suspend fun getBarrios(): Response<BarriosResponse>
}