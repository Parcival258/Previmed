data class PacienteData(
    val idPaciente: Int,
    val direccion_cobro: String?,
    val ocupacion: String?,
    val activo: Boolean,
    val beneficiario: Boolean,
    val paciente_id: String?,
    val usuario_id: String,
    val usuario: Usuario
)