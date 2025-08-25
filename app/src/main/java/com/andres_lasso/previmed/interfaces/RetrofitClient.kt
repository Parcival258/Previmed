package com.andres_lasso.previmed.interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://previmedbackend-vnbb.onrender.com/"

    val instance: LoginApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // tu backend en Render
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(LoginApi::class.java)
    }
}
