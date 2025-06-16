package com.andres_lasso.previmed.controller.asesor.recycler

import com.andres_lasso.previmed.controller.asesor.recycler.adapter.Beneficios

data class PlanesClass(
    val idPlan: Int,
    val tipoPlan: String,
    val desripcionPlan: String,
    val precioPlan: String,
    val beneficios: List<Beneficios>
)


