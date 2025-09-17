package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.BarriosResponse
import retrofit2.Response
import retrofit2.http.GET

interface BarriosApi {
    @GET("barrios")
    suspend fun listarBarrios(): Response<BarriosResponse>
}
