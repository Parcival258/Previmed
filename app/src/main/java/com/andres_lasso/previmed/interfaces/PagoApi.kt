package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.PagoModel
import com.andres_lasso.previmed.model.PagoRequest
import com.andres_lasso.previmed.model.PagosResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PagoApi {

    @GET("registros-pago")
    suspend fun obtenerPagos(): PagosResponse

    @POST("registro-pago")
    suspend fun createPago(@Body pago: PagoRequest): Response<PagoModel>

    @Multipart
    @POST("registro-pago")
    suspend fun createPagoConFoto(
        @Part("monto") monto: RequestBody,
        @Part("fecha_inicio") fechaInicio: RequestBody,
        @Part("fecha_fin") fechaFin: RequestBody,
        @Part("fecha_pago") fechaPago: RequestBody,
        @Part("membresia_id") membresiaId: RequestBody,
        @Part("forma_pago_id") formaPagoId: RequestBody?,
        @Part("numero_recibo") numeroRecibo: RequestBody?,
        @Part("cobrador_id") cobradorId: RequestBody?,
        @Part("estado") estado: RequestBody?,
        @Part foto: MultipartBody.Part?
    ): Response<PagoModel>
}
