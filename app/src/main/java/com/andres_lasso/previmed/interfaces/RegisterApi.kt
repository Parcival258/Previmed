package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.Eps
import com.andres_lasso.previmed.model.LoginRequest
import com.andres_lasso.previmed.model.LoginResponse
import com.andres_lasso.previmed.model.RegisterRequest
import com.andres_lasso.previmed.model.RegisterResponse
import com.andres_lasso.previmed.model.Rol

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("usuarios")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>
}

interface RolesApi {

    @GET("roles")
    fun getRoles(): Call<List<Rol>>
}

interface EpsApi {

    @GET("eps")
    fun getEps(): Call<List<Eps>>
}