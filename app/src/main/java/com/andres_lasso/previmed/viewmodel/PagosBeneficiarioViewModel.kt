package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andres_lasso.previmed.model.PagoResponse
import com.andres_lasso.previmed.repository.PagosRepository
import kotlinx.coroutines.launch
import android.util.Log

class PagosBeneficiarioViewModel : ViewModel() {

    private val repository = PagosRepository()

    private val _pago = MutableLiveData<PagoResponse?>()
    val pago: LiveData<PagoResponse?> get() = _pago

    fun cargarPagoPorUUID(uuid: String) {
        viewModelScope.launch {
            try {
                val pagoResponse = repository.obtenerPagoPorUUID(uuid)
                _pago.postValue(pagoResponse)
            } catch (e: Exception) {
                Log.e("PAGOS_VM", "❌ Error al cargar pago: ${e.localizedMessage}")
                _pago.postValue(null)
            }
        }
    }
}
