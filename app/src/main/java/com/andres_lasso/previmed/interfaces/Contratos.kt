package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.Membresia
import com.andres_lasso.previmed.model.MembresiaRequest
import com.andres_lasso.previmed.model.Paciente
import com.andres_lasso.previmed.model.Plan
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Contratos {
    @GET("membresias")
    suspend fun listarMembresias(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<Membresia>


}
