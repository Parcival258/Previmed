package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.FormaPago
import retrofit2.Call
import retrofit2.http.GET

interface FormaPagoApi {
    @GET("formas_pago/read")
    fun getFormasPago(): Call<List<FormaPago>>
}
