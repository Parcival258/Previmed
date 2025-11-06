package com.andres_lasso.previmed.controller.pacientes.recycler

import androidx.lifecycle.*
import com.andres_lasso.previmed.model.Beneficiario
import kotlinx.coroutines.launch

class BeneficiarioViewModel(private val repo: BeneficiariosRepo) : ViewModel() {

    private val _beneficiarios = MutableLiveData<List<Beneficiario>>()
    val beneficiarios: LiveData<List<Beneficiario>> = _beneficiarios

    fun cargarBeneficiarios(pacienteId: Int) {
        viewModelScope.launch {
            try {
                val lista = repo.getBeneficiarios(pacienteId)
                _beneficiarios.value = lista
            } catch (e: Exception) {
                _beneficiarios.value = emptyList()
            }
        }
    }
}
