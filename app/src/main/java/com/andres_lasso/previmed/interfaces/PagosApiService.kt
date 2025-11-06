package com.andres_lasso.previmed.network

import com.andres_lasso.previmed.model.MembresiaIdResponse
import com.andres_lasso.previmed.model.MembresiaXUserResponse
import com.andres_lasso.previmed.model.RegistroPagoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface PagosApiService {

    /**
     * Obtiene los registros de pago de una membresía específica
     * Endpoint: /registros-pago/membresia/:id
     * @param membresiaId ID de la membresía
     * @return Lista de registros de pago con información detallada
     */
    @GET("registros-pago/membresia/{id}")
    suspend fun getPagosByMembresiaId(
        @Path("id") membresiaId: Int
    ): Response<List<RegistroPagoResponse>>

    /**
     * Obtiene el ID de membresía asociado a un usuario
     * Endpoint: /membresiasxpacientes/user/:userId
     * @param userId ID del usuario (UUID)
     * @return Objeto con el ID de la membresía
     */
    @GET("membresiasxpacientes/user/{userId}")
    suspend fun getMembresiaIdByUserId(
        @Path("userId") userId: String
    ): Response<MembresiaIdResponse>

    /**
     * Obtiene la información completa de la membresía de un usuario por UUID
     * Incluye: membresía, plan, fechas, beneficiarios
     * Endpoint: /membresiasxpacientes/user/:userId
     * @param userId ID del usuario (UUID)
     * @return Información completa de la membresía
     */
    @GET("membresiasxpacientes/user/{userId}")
    suspend fun getMembresiaByUserId(
        @Path("userId") userId: String
    ): Response<MembresiaXUserResponse>

    /**
     * Obtiene la información completa de la membresía por ID de Paciente (integer)
     * Este endpoint es más confiable
     * Endpoint: /membresiasxpacientes/paciente/:pacienteId
     * @param pacienteId ID del paciente (integer)
     * @return Información completa de la membresía
     */
    @GET("membresiasxpacientes/paciente/{pacienteId}")
    suspend fun getMembresiaByPacienteId(
        @Path("pacienteId") pacienteId: Int
    ): Response<MembresiaXUserResponse>
}

