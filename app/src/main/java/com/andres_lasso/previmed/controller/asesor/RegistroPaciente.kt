package com.andres_lasso.previmed.controller.asesor

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.Login
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import PacienteRequest
import PacienteResponse

class RegistroPaciente : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etDireccion: EditText
    private lateinit var spTipoDocumento: Spinner
    private lateinit var etNumeroDocumento: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etNumeroHijos: EditText
    private lateinit var etEstrato: EditText
    private lateinit var spAutorizacionDatos: Spinner
    private lateinit var spGenero: Spinner
    private lateinit var spEstadoCivil: Spinner
    private lateinit var spRol: Spinner
    private lateinit var spEps: Spinner
    private lateinit var btnGuardar: Button

    private var listaRoles = listOf<Rol>()
    private var listaEps = listOf<Eps>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_paciente)

        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etDireccion = findViewById(R.id.etDireccion)
        spTipoDocumento = findViewById(R.id.spTipoDocumento)
        etNumeroDocumento = findViewById(R.id.etNumeroDocumento)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etNumeroHijos = findViewById(R.id.etNumeroHijos)
        etEstrato = findViewById(R.id.etEstrato)
        spAutorizacionDatos = findViewById(R.id.spAutorizacionDatos)
        spGenero = findViewById(R.id.spGenero)
        spEstadoCivil = findViewById(R.id.spEstadoCivil)
        spRol = findViewById(R.id.spRol)
        spEps = findViewById(R.id.spEps)
        btnGuardar = findViewById(R.id.btnGuardar)

        setupSpinner(spTipoDocumento, R.array.tipos_documento)
        setupSpinner(spAutorizacionDatos, R.array.autorizacion_datos)
        setupSpinner(spGenero, R.array.generos)
        setupSpinner(spEstadoCivil, R.array.estados_civiles)

        cargarRoles()
        cargarEps()
        setupDatePicker()

        btnGuardar.setOnClickListener {
            if (validateFields()) {
                val numeroDocumentoText = etNumeroDocumento.text.toString().trim()
                if (numeroDocumentoText.isEmpty()) {
                    Toast.makeText(this, "El número de documento es obligatorio", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val rolId = listaRoles.getOrNull(spRol.selectedItemPosition)?.id ?: 4
                val epsId = listaEps.getOrNull(spEps.selectedItemPosition)?.id ?: 1

                val request = PacienteRequest(
                    nombre = etNombre.text.toString(),
                    segundo_nombre = null,
                    apellido = etApellido.text.toString(),
                    segundo_apellido = null,
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString(),
                    direccion = etDireccion.text.toString(),
                    numero_documento = numeroDocumentoText,
                    fecha_nacimiento = etFechaNacimiento.text.toString(),
                    numero_hijos = etNumeroHijos.text.toString().toIntOrNull(),
                    estrato = etEstrato.text.toString().toIntOrNull(),
                    autorizacion_datos = spAutorizacionDatos.selectedItem.toString().equals("Sí", ignoreCase = true),
                    habilitar = true,
                    genero = spGenero.selectedItem.toString(),
                    estado_civil = spEstadoCivil.selectedItem.toString(),
                    tipo_documento = spTipoDocumento.selectedItem.toString(),
                    eps_id = epsId,
                    rol_id = rolId,
                    direccion_cobro = null,
                    ocupacion = null,
                    activo = true,
                    beneficiario = true
                )

                registrarPaciente(request)
            }
        }
    }

    private fun setupSpinner(spinner: Spinner, arrayResId: Int) {
        val adapter = ArrayAdapter.createFromResource(
            this,
            arrayResId,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun cargarRoles() {
        RetrofitClient.rolesApi.getRoles().enqueue(object : Callback<List<Rol>> {
            override fun onResponse(call: Call<List<Rol>>, response: Response<List<Rol>>) {
                if (response.isSuccessful) {
                    listaRoles = response.body() ?: listOf()
                    val nombresRoles = listaRoles.map { it.nombre }
                    spRol.adapter = ArrayAdapter(this@RegistroPaciente, android.R.layout.simple_spinner_item, nombresRoles).apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }
                }
            }
            override fun onFailure(call: Call<List<Rol>>, t: Throwable) {
                Log.e("Roles", "Fallo en llamada roles", t)
            }
        })
    }

    private fun cargarEps() {
        RetrofitClient.epsApi.getEps().enqueue(object : Callback<List<Eps>> {
            override fun onResponse(call: Call<List<Eps>>, response: Response<List<Eps>>) {
                if (response.isSuccessful) {
                    listaEps = response.body() ?: listOf()
                    val nombresEps = listaEps.map { it.nombre }
                    spEps.adapter = ArrayAdapter(this@RegistroPaciente, android.R.layout.simple_spinner_item, nombresEps).apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }
                }
            }
            override fun onFailure(call: Call<List<Eps>>, t: Throwable) {
                Log.e("EPS", "Fallo en llamada EPS", t)
            }
        })
    }

    private fun setupDatePicker() {
        etFechaNacimiento.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this, { _, y, m, d ->
                val formattedDate = "%04d-%02d-%02d".format(y, m + 1, d)
                etFechaNacimiento.setText(formattedDate)
            }, year, month, day)
            dpd.show()
        }
    }

    private fun validateFields(): Boolean {
        return when {
            etNombre.text.isNullOrBlank() -> {
                toastAndFocus("Ingrese el nombre", etNombre)
                false
            }
            etApellido.text.isNullOrBlank() -> {
                toastAndFocus("Ingrese el apellido", etApellido)
                false
            }
            etEmail.text.isNullOrBlank() -> {
                toastAndFocus("Ingrese el email", etEmail)
                false
            }
            etPassword.text.isNullOrBlank() -> {
                toastAndFocus("Ingrese la contraseña", etPassword)
                false
            }
            etNumeroDocumento.text.isNullOrBlank() -> {
                toastAndFocus("Ingrese el número de documento", etNumeroDocumento)
                false
            }
            etFechaNacimiento.text.isNullOrBlank() -> {
                toastAndFocus("Seleccione fecha de nacimiento", etFechaNacimiento)
                false
            }
            else -> true
        }
    }

    private fun toastAndFocus(message: String, view: EditText) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        view.requestFocus()
    }

    private fun registrarPaciente(request: PacienteRequest) {
        RetrofitClient.pacienteApi.registrarPaciente(request)
            .enqueue(object : Callback<PacienteResponse> {
                override fun onResponse(call: Call<PacienteResponse>, response: Response<PacienteResponse>) {
                    if (response.isSuccessful) {
                        val paciente = response.body()
                        Toast.makeText(this@RegistroPaciente, "Paciente registrado: ${paciente?.nombre}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegistroPaciente, Login::class.java))
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("PACIENTE_ERROR", errorBody ?: "Error desconocido")
                        Toast.makeText(this@RegistroPaciente, "Error: $errorBody", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<PacienteResponse>, t: Throwable) {
                    Toast.makeText(this@RegistroPaciente, "Fallo en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
