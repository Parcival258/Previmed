data class PacienteResponse(
    val id: Int,
    val nombre: String,
    val email: String,
    val mensaje: String?, // por si tu backend devuelve un mensaje
    val data: PacienteData?
)
