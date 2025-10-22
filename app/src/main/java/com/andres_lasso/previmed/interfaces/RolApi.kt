package com.tu_paquete.Interface

import com.andres_lasso.previmed.model.Rol
import retrofit2.Call
import retrofit2.http.GET
import com.andres_lasso.previmed.interfaces.RetrofitClient

interface RolApi {
    @GET("roles")  // 👈 Ajusta al endpoint real
    fun getRoles(): Call<List<Rol>>
}
