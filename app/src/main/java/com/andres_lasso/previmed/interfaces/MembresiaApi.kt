package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.ApiResponse
import com.andres_lasso.previmed.model.Membresia
import com.andres_lasso.previmed.model.MembresiaRequest
import com.andres_lasso.previmed.model.MembresiaResponse
import retrofit2.Call
import retrofit2.http.*

interface MembresiaApi {

    @POST("membresias")
    fun crearMembresia(@Body request: MembresiaRequest): Call<Membresia>

    @GET("membresias/{id}")
    fun obtenerMembresia(@Path("id") id: Int): Call<ApiResponse<MembresiaResponse>>

    @GET("membresias")
    fun listarMembresias(): Call<List<Membresia>>

}

