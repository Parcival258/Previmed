package com.andres_lasso.previmed.repository

import android.util.Log
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.PagoMembresiaUI
import com.andres_lasso.previmed.utils.DateUtils

class ContratoRepository {

    private val pagosApi = RetrofitClient.pagosApi

    suspend fun getContratoByUserId(userId: String): Result<PagoMembresiaUI> {
        return try {
            val response = pagosApi.getMembresiaByUserId(userId)
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                Log.d("CONTRATO_REPO", "✅ Respuesta exitosa")

                val ui = PagoMembresiaUI(
                    numeroContrato = dto.membresia.numeroContrato,
                    fechaInicio = DateUtils.formatApiDate(dto.membresia.fechaInicio),
                    fechaFin = DateUtils.formatApiDate(dto.membresia.fechaFin),
                    fechaProximoPago = DateUtils.calculateNextPaymentDate(dto.membresia.fechaInicio),
                    titularMembresia = dto.membresia.firma ?: "Titular no disponible",
                    plan = dto.membresia.plan.tipoPlan,
                    beneficios = dto.membresia.plan.descripcion
                )

                Result.success(ui)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("CONTRATO_REPO", "❌ Error: ${e.message}", e)
            Result.failure(e)
        }
    }
}
