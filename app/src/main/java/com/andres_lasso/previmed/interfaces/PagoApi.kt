package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.PagoModel
import com.andres_lasso.previmed.model.PagoRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PagoApi {

    // ✅ Obtener todos los pagos
    @GET("registros-pago")
    suspend fun getAllPagos(): Response<List<PagoModel>>

    // ✅ Crear pago SIN foto (JSON puro)
    @POST("registro-pago")
    suspend fun createPago(@Body pago: PagoRequest): Response<PagoModel>

    // ✅ Crear pago CON foto (multipart/form-data)
    @Multipart
    @POST("registro-pago")
    suspend fun createPagoConFoto(
        @Part("monto") monto: RequestBody,
        @Part("fecha_inicio") fechaInicio: RequestBody,
        @Part("fecha_fin") fechaFin: RequestBody,
        @Part("fecha_pago") fechaPago: RequestBody,
        @Part("membresia_id") membresiaId: RequestBody,
        @Part("forma_pago_id") formaPagoId: RequestBody,
        @Part("numero_recibo") numeroRecibo: RequestBody?,  // 🆕 Nuevo
        @Part("cobrador_id") cobradorId: RequestBody?,      // 🆕 Nuevo
        @Part("estado") estado: RequestBody?,               // 🆕 Nuevo
        @Part foto: MultipartBody.Part?                     // Foto opcional
    ): Response<PagoModel>
}
