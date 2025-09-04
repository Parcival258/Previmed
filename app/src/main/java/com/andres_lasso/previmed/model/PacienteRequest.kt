data class PacienteRequest(
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
        val habilitar: Boolean = true,
        val genero: String,
        val estado_civil: String,
        val tipo_documento: String,   // << antes estaba Int, pero tu JSON manda string
        val eps_id: Int,
        val rol_id: Int = 4,
        val direccion_cobro: String?,
        val ocupacion: String?,
        val activo: Boolean = true,
        val beneficiario: Boolean = true
)


