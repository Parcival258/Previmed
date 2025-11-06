package com.andres_lasso.previmed.network

import com.andres_lasso.previmed.model.*
import retrofit2.Response
import retrofit2.http.*

interface PagosApi {

    /** 🔹 Obtener membresía completa por UUID del usuario */
    @GET("membresiasxpacientes/user/{userId}")
    suspend fun getMembresiaByUserId(
        @Path("userId") userId: String
    ): Response<MembresiaXUserResponse>

    /** 🔹 Obtener registros de pago de una membresía */
    @GET("registros-pago/membresia/{id}")
    suspend fun getPagosByMembresiaId(
        @Path("id") membresiaId: Int
    ): Response<List<RegistroPagoResponse>>


    /** 🔹 Cancelar registro de pago */
    @DELETE("registros-pago/{id}")
    suspend fun cancelarRegistroPago(
        @Path("id") pagoId: String
    ): Response<Unit>
}