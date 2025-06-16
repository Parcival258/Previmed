package com.andres_lasso.previmed.controller.asesor.recycler.adapter

data class Beneficios(
    val id: Int,
    val nombre: String,
    val idPlan: Int
) {
    override fun toString(): String {
        return "✔ $nombre"
    }
}
