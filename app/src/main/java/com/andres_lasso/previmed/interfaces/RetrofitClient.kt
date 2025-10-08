package com.andres_lasso.previmed.interfaces

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://previmedbackend-q73n.onrender.com/"

    // Interceptor para mostrar logs de request y response HTTP
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente HTTP con timeout configurado y logging
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(200, TimeUnit.SECONDS)  // Tiempo máximo para conectar
        .readTimeout(200, TimeUnit.SECONDS)     // Tiempo máximo para leer respuesta
        .writeTimeout(200, TimeUnit.SECONDS)    // Tiempo máximo para enviar datos
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)              // Usar cliente con timeout y logging
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Servicios API
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val rolesApi: PacienteApi.RolesApi by lazy {
        retrofit.create(PacienteApi.RolesApi::class.java)
    }

    val epsApi: PacienteApi.EpsApi by lazy {
        retrofit.create(PacienteApi.EpsApi::class.java)
    }

    val loginApi: LoginApi by lazy {
        retrofit.create(LoginApi::class.java)
    }

    val contratosApi: Contratos by lazy {
        retrofit.create(Contratos::class.java)
    }
    val pacienteApi: PacienteApi by lazy {
        retrofit.create(PacienteApi::class.java)
    }
    val planes: PlanesApi by lazy {
        retrofit.create(PlanesApi::class.java)
    }
    val visitas: VisitaService by lazy {
        retrofit.create(VisitaService::class.java)
    }

    val pacienteApiService: PacienteApi by lazy {
        retrofit.create(PacienteApi::class.java)
    }




}
