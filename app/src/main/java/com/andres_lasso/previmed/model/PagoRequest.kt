data class PagoRequest(
    val monto: Double,
    val fecha_inicio: String?,
    val fecha_fin: String?,
    val fecha_pago: String,
    val membresia_id: Int,
    val forma_pago_id: Int?,        // puede ser null
    val foto: String? = null,
    val numero_recibo: String? = null,
    val cobrador_id: String? = null,   // ← ahora es Int? opcional
    val estado: String? = null
)
