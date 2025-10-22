package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.model.Eps
import com.andres_lasso.previmed.model.EpsResponse
import retrofit2.Call
import retrofit2.http.GET

interface EpsApi {
    @GET("/eps/read")
    fun getEps(): Call<List<Eps>>
}

