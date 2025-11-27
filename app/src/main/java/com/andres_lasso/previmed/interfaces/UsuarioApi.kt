package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.Usuario
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UsuarioApi {

    @GET("usuarios/{id}")
    suspend fun getUsuarioById(
        @Path("id") id: String
    ): Response<Usuario>
}
