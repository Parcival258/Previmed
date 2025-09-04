data class Usuario(
    val nombre: String,
    val segundo_nombre: String?,
    val apellido: String,
    val segundo_apellido: String?,
    val email: String,
    val password: String,
    val direccion: String,
    val numero_documento: String,
    val fecha_nacimiento: String,
    val numero_hijos: Int?,
    val estrato: Int?,
    val autorizacion_datos: Boolean,
    val genero: String,
    val estado_civil: String,
    val rol_id: Int,
    val eps_id: Int?,
    val habilitar: Boolean = true
)
