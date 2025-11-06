package com.andres_lasso.previmed.controller.pacientes.recycler

import android.util.Log
import com.andres_lasso.previmed.interfaces.BeneficiarioService
import com.andres_lasso.previmed.model.Beneficiario

class BeneficiariosRepo(private val service: BeneficiarioService) {
    suspend fun getBeneficiarios(pacienteId: Int): List<Beneficiario> {
        val resp = service.getBeneficiarios(pacienteId)
        return if (resp.isSuccessful) {
            resp.body()?.data ?: emptyList()
        } else {
            Log.e("BENEF_REPO", "Error API: ${resp.code()}")
            emptyList()
        }
    }
}
