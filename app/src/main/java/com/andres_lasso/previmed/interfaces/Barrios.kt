package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.controller.asesor.recycler.BarriosClass
import com.andres_lasso.previmed.model.BarriosResponse
import retrofit2.http.GET
import retrofit2.Response

interface BarriosApi {
    @GET("/barrios")
    suspend fun listarBarrios(): Response<BarriosLista>
}

data class BarriosLista(val msj: List<BarriosResponse>)
