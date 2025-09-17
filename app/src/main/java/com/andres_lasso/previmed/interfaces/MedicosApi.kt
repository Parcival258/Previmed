// MedicosApi.kt
package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.Medico
import com.andres_lasso.previmed.model.MedicosResponse
import retrofit2.Response
import retrofit2.http.GET

interface MedicosApi {
    @GET("medicos") // Cambia según tu endpoint
    suspend fun listarMedicos(): Response<MedicosResponse>
}