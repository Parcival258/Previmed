package com.andres_lasso.previmed.controller.pacientes.recycler.adapter

import android.util.Log
import androidx.lifecycle.*
import com.andres_lasso.previmed.model.Visita
import kotlinx.coroutines.launch

class VisitaViewModel(private val repo: VisitaRepo) : ViewModel() {

    private val _visitas = MutableLiveData<List<Visita>>()
    val visitas: LiveData<List<Visita>> = _visitas

    fun obtenerVisitas(idPaciente: Int) {
        Log.d("VISITA_VM", "📡 Obteniendo visitas para idPaciente: $idPaciente")
        viewModelScope.launch {
            try {
                val lista = repo.cargarVisitas(idPaciente)
                Log.d("VISITA_VM", "✅ Visitas obtenidas: ${lista.size}")
                _visitas.value = lista
            } catch (e: Exception) {
                Log.e("VISITA_VM", "❌ Error obteniendo visitas", e)
                _visitas.value = emptyList()
            }
        }
    }

    fun cancelarVisita(idVisita: String, idPaciente: Int) {
        viewModelScope.launch {
            try {
                Log.d("VISITA_VM", "🗑️ Cancelando visita: $idVisita")
                repo.cancelarVisita(idVisita)
                obtenerVisitas(idPaciente)
            } catch (_: Exception) {}
        }
    }
}