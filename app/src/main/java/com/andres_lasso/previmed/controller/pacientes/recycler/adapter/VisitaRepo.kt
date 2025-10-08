package com.andres_lasso.previmed.controller.pacientes.recycler.adapter

import android.util.Log
import com.andres_lasso.previmed.interfaces.VisitaService
import com.andres_lasso.previmed.model.Visita
import com.andres_lasso.previmed.model.VisitasRequest

class VisitaRepo(private val service: VisitaService) {

    suspend fun cargarVisitas(idPaciente: Int): List<Visita> {
        Log.d("VISITA_REPO", "📡 Consultando visitas para idPaciente: $idPaciente")
        val response = service.getVisitas(idPaciente)
        if (response.isSuccessful) {
            val lista = response.body()?.msj ?: emptyList()
            Log.d("VISITA_REPO", "✅ Visitas obtenidas: ${lista.size}")
            return lista
        }
        Log.e("VISITA_REPO", "❌ Error API: ${response.code()}")
        throw Exception("Error API: ${response.code()}")
    }

    suspend fun cancelarVisita(idVisita: String) {
        Log.d("VISITA_REPO", "🗑️ Cancelando visita: $idVisita")
        service.cancelarVisita(idVisita)
    }

    suspend fun crearVisita(request: VisitasRequest) = service.crearVisita(request)
}