package com.andres_lasso.previmed.interfaces


import com.andres_lasso.previmed.model.LoginRequest
import com.andres_lasso.previmed.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("usuarios")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
}


