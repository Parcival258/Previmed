package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.Visita
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface VisitaApi {
    @POST("visitas") // Cambia según tu endpoint
    suspend fun crearVisita(@Body visita: Visita): Response<Void>
}

