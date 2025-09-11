package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.PlanesResponse
import retrofit2.Call
import retrofit2.http.GET

interface PlanesApi {
    @GET("planes")
    fun getPlanes(): Call<PlanesResponse>
}
