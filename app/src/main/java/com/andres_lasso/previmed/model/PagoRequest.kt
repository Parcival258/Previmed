package com.andres_lasso.previmed.model

data class PagoRequest(
    val monto: Double,
    val fecha_inicio: String?,
    val fecha_fin: String?,
    val fecha_pago: String,
    val membresia_id: Int,
    val forma_pago_id: Int,
    val foto: String? = null
)
