package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.PagoModel
import com.andres_lasso.previmed.model.PagoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PagoApi {
    @GET("registros-pago")
    suspend fun getAllPagos(): Response<List<PagoModel>>

    @POST("registro-pago")
    suspend fun createPago(@Body pago: PagoRequest): Response<PagoModel>
}
