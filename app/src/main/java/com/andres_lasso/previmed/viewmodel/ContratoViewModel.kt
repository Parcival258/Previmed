package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andres_lasso.previmed.model.MembresiaXUserResponse

import kotlinx.coroutines.launch
import android.util.Log
import com.andres_lasso.previmed.interfaces.RetrofitClient

class ContratoViewModel : ViewModel() {

    private val _membresia = MutableLiveData<MembresiaXUserResponse?>()
    val membresia: LiveData<MembresiaXUserResponse?> get() = _membresia

    fun cargarMembresiaPorUUID(uuidPaciente: String) {
        viewModelScope.launch {
            try {
                Log.d("CONTRATO_VM", "🔄 Buscando membresía con UUID: $uuidPaciente")
                val response = RetrofitClient.pagosApi.getMembresiaByUserId(uuidPaciente)

                if (response.isSuccessful) {
                    _membresia.postValue(response.body())
                    Log.d("CONTRATO_VM", "✅ Membresía cargada correctamente")
                } else {
                    Log.e("CONTRATO_VM", "❌ Error HTTP: ${response.code()} - ${response.message()}")
                    _membresia.postValue(null)
                }

            } catch (e: Exception) {
                Log.e("CONTRATO_VM", "❌ Excepción: ${e.localizedMessage}")
                _membresia.postValue(null)
            }
        }
    }
}
