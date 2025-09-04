package com.andres_lasso.previmed.controller.asesor

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R

import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.PacientesAsesorFragment
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

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
    private lateinit var etDireccionCobro: EditText
    private lateinit var btnGuardar: Button

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
        etDireccionCobro = findViewById(R.id.etDireccionCobro)
        btnGuardar = findViewById(R.id.btnGuardar)

        setupSpinner(spTipoDocumento, R.array.tipos_documento)
        setupSpinner(spAutorizacionDatos, R.array.autorizacion_datos)
        setupSpinner(spGenero, R.array.generos)
        setupSpinner(spEstadoCivil, R.array.estados_civiles)

        setupDatePicker()

        btnGuardar.setOnClickListener {
            if (!validarCampos()) return@setOnClickListener

            val rolId = 4 // Fijo para paciente

            val request = PacienteRequest(
                nombre = etNombre.text.toString(),
                segundo_nombre = null,
                apellido = etApellido.text.toString(),
                segundo_apellido = null,
                email = etEmail.text.toString(),
                password = etPassword.text.toString(),
                direccion = etDireccion.text.toString(),
                numero_documento = etNumeroDocumento.text.toString(),
                fecha_nacimiento = etFechaNacimiento.text.toString(),
                numero_hijos = etNumeroHijos.text.toString().toIntOrNull(),
                estrato = etEstrato.text.toString().toIntOrNull(),
                autorizacion_datos = spAutorizacionDatos.selectedItem.toString().equals("Sí", ignoreCase = true),
                habilitar = true,
                genero = spGenero.selectedItem.toString(),
                estado_civil = spEstadoCivil.selectedItem.toString(),
                tipo_documento = spTipoDocumento.selectedItem.toString(),
                eps_id = 1,
                rol_id = rolId,
                direccion_cobro = etDireccionCobro.text.toString().ifBlank { "Sin dirección de cobro" },
                ocupacion = null,
                activo = true,
                beneficiario = true
            )
            registrarPaciente(request)
        }
    }

    private fun setupSpinner(spinner: Spinner, arrayResId: Int) {
        val adapter = ArrayAdapter.createFromResource(this, arrayResId, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupDatePicker() {
        etFechaNacimiento.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    val formattedDate = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
                    etFechaNacimiento.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun validarCampos(): Boolean {
        return when {
            etNombre.text.isNullOrBlank() -> { toastAndFocus("Ingrese el nombre", etNombre); false }
            etApellido.text.isNullOrBlank() -> { toastAndFocus("Ingrese el apellido", etApellido); false }
            etEmail.text.isNullOrBlank() -> { toastAndFocus("Ingrese el email", etEmail); false }
            etPassword.text.isNullOrBlank() -> { toastAndFocus("Ingrese la contraseña", etPassword); false }
            etNumeroDocumento.text.isNullOrBlank() -> { toastAndFocus("Ingrese el número de documento", etNumeroDocumento); false }
            etFechaNacimiento.text.isNullOrBlank() -> { toastAndFocus("Seleccione fecha de nacimiento", etFechaNacimiento); false }
            etDireccionCobro.text.isNullOrBlank() -> { toastAndFocus("Ingrese la dirección de cobro", etDireccionCobro); false }
            else -> true
        }
    }

    private fun toastAndFocus(msg: String, editText: EditText) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        editText.requestFocus()
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun registrarPaciente(request: PacienteRequest) {
        RetrofitClient.pacienteApi.registrarPaciente(request).enqueue(object : Callback<PacientesResponse> {
            override fun onResponse(
                call: Call<PacientesResponse>,
                response: Response<PacientesResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    toast("Paciente registrado correctamente")
                } else {
                    val errorBody = response.errorBody()?.string()
                    toast("Error al registrar paciente: $errorBody")
                }
            }
            override fun onFailure(call: Call<PacientesResponse>, t: Throwable) {
                toast("Fallo en la conexión: ${t.message}")
            }
        })
    }
}
