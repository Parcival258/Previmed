package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.BeneficiariosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BeneficiarioService {
    @GET("pacientes/beneficiarios/{paciente_id}")
    suspend fun getBeneficiarios(@Path("paciente_id") pacienteId: Int): Response<BeneficiariosResponse>
}
