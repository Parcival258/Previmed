package com.andres_lasso.previmed.controller.pacientes.recycler.adapter

import androidx.lifecycle.*
import com.andres_lasso.previmed.model.Visita
import kotlinx.coroutines.launch

class VisitaViewModel(private val repo: VisitaRepo) : ViewModel() {

    private val _visitas = MutableLiveData<List<Visita>>()
    val visitas: LiveData<List<Visita>> = _visitas

    fun obtenerVisitas(idPaciente: Int) {
        viewModelScope.launch {
            val lista = repo.cargarVisitas(idPaciente)
            _visitas.value = lista
        }
    }

    fun cancelarVisita(idVisita: String, idPaciente: Int) {
        viewModelScope.launch {
            repo.cancelarVisita(idVisita)
            obtenerVisitas(idPaciente) // recarga la lista
        }
    }
}
