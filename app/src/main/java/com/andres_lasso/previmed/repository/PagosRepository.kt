package com.andres_lasso.previmed.repository

import android.util.Log
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.PagoResponse

class PagosRepository {

    suspend fun obtenerPagoPorUUID(uuid: String): PagoResponse? {
        return try {
            val api = RetrofitClient.pagosApi
            val response = api.getMembresiaByUserId(uuid)

            if (response.isSuccessful && response.body() != null) {
                val m = response.body()!!.membresia
                PagoResponse(
                    precio = m.plan.precio,
                    formaPago = m.formaPago ?: "N/A",
                    fechaInicio = m.fechaInicio,
                    fechaFin = m.fechaFin,
                    fechaPago = m.fechaFin,
                    tipoPlan = m.plan.tipoPlan,
                    titular = m.firma ?: "Titular no disponible",
                    descripcionPlan = m.plan.descripcion
                )
            } else {
                Log.e("PAGOS_REPO", "❌ Error HTTP: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PAGOS_REPO", "❌ Excepción: ${e.message}", e)
            null
        }
    }
}
