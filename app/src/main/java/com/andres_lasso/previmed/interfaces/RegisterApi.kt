package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.ApiResponse
import com.andres_lasso.previmed.model.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("usuarios")
    fun registerUser(@Body body: Map<String, Any>): Call<ApiResponse<Usuario>>

}
