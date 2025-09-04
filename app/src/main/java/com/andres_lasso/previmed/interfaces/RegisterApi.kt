package com.andres_lasso.previmed.interfaces


import com.andres_lasso.previmed.model.RegisterRequest
import com.andres_lasso.previmed.model.RegisterResponse


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("usuarios")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>
}