package com.andres_lasso.previmed.interfaces

import PacienteRequest
import PacienteResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PacienteApi {
    @POST("/pacientes")  // 👈 revisa que el endpoint sea correcto
    fun registrarPaciente(@Body request: PacienteRequest): Call<PacienteResponse>


}

