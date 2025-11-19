package com.andres_lasso.previmed

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.controller.asesor.ContratoActivity
import com.andres_lasso.previmed.databinding.ActivityRegistroUsuarioBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RegistroUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroUsuarioBinding
    private val TAG = "RegistroUsuario"

    private var selectedEpsId: Int = 0
    private var selectedPlanId: Int? = null
    private var selectedFormaPagoId: Int? = null

    private var epsList: List<Eps> = emptyList()
    private var planList: List<Plan> = emptyList()
    private var formaPagoList: List<FormaPago> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ⭐ StatusBar azul oscuro con iconos blancos (igual a tu barra de arriba)
        window.statusBarColor = getColor(R.color.white)
        androidx.core.view.WindowInsetsControllerCompat(
            window,
            window.decorView
        ).isAppearanceLightStatusBars = false

        setupStaticSpinners()
        setupDatePicker(binding.etFechaNacimiento)

        loadEps()
        fetchPlanes()
        loadFormasPago()

        binding.btnGuardar.setOnClickListener { registrarUsuario() }

        binding.btnSiguienteRegistro.setOnClickListener {
            toast("Primero guarda el usuario y paciente antes de continuar")
        }
    }


    private fun validarCamposBasicos(): Boolean {
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        return when {
            nombre.isEmpty() -> { toast("El nombre es obligatorio"); false }
            apellido.isEmpty() -> { toast("El apellido es obligatorio"); false }
            email.isEmpty() -> { toast("El email es obligatorio"); false }
            password.isEmpty() -> { toast("La contraseña es obligatoria"); false }
            selectedEpsId == 0 -> { toast("Selecciona una EPS"); false }
            selectedPlanId == null -> { toast("Selecciona un plan"); false }
            selectedFormaPagoId == null -> { toast("Selecciona una forma de pago"); false }
            else -> true
        }
    }

    private fun registrarUsuario() {
        if (!validarCamposBasicos()) return

        val pacienteRequest = PacienteRequest(
            nombre = binding.etNombre.text.toString().trim(),
            segundoNombre = binding.etSegundoNombre.text.toString().trim().ifEmpty { null },
            apellido = binding.etApellido.text.toString().trim(),
            segundoApellido = binding.etSegundoApellido.text.toString().trim().ifEmpty { null },
            email = binding.etEmail.text.toString().trim(),
            password = binding.etPassword.text.toString().trim(),
            direccion = binding.etDireccion.text.toString().trim().ifEmpty { null },
            numeroDocumento = binding.etNumeroDocumento.text.toString().trim(),
            fechaNacimiento = binding.etFechaNacimiento.text.toString().trim().ifEmpty { null },
            numeroHijos = binding.etNumeroHijos.text.toString().trim().toIntOrNull(),
            estrato = binding.etEstrato.text.toString().trim().toIntOrNull(),
            autorizacionDatos = (binding.spAutorizacionDatos.selectedItem?.toString()?.equals("Sí", true)
                ?: false),
            habilitar = true,
            genero = binding.spGenero.selectedItem?.toString(),
            estadoCivil = binding.spEstadoCivil.selectedItem?.toString(),
            tipoDocumento = binding.spTipoDocumento.selectedItem?.toString(),
            epsId = selectedEpsId,
            rolId = 4,
            direccionCobro = binding.etDireccionCobro.text.toString().trim().ifEmpty { null },
            ocupacion = binding.etOcupacion.text.toString().trim().ifEmpty { null },
            activo = true,
            beneficiario = true,
            pacienteId = null,
            planId = selectedPlanId,
            formaPago = binding.spformapago.selectedItem?.toString()
        )

        Log.d(TAG, "📦 Enviando PacienteRequest: $pacienteRequest")
        binding.btnGuardar.isEnabled = false

        RetrofitClient.pacienteApi.registrarPaciente(pacienteRequest)
            .enqueue(object : Callback<PacienteCreadoResponse> {
                override fun onResponse(
                    call: Call<PacienteCreadoResponse>,
                    response: Response<PacienteCreadoResponse>
                ) {
                    binding.btnGuardar.isEnabled = true
                    if (response.isSuccessful && response.body()?.data != null) {
                        val creado = response.body()!!.data!!
                        Log.d(TAG, "✅ Paciente creado con ID: ${creado.idPaciente}")
                        toast("Paciente creado correctamente")
                        abrirContrato(creado.usuarioId ?: "", creado.idPaciente)
                    } else {
                        Log.e(TAG, "❌ Error crear paciente: ${response.errorBody()?.string()}")
                        toast("Error al crear paciente")
                    }
                }

                override fun onFailure(call: Call<PacienteCreadoResponse>, t: Throwable) {
                    binding.btnGuardar.isEnabled = true
                    Log.e(TAG, "🚨 Fallo conexión crear paciente", t)
                    toast("Error de conexión con el servidor")
                }
            })
    }

    private fun abrirContrato(usuarioId: String, pacienteId: Int) {
        val intent = Intent(this, ContratoActivity::class.java).apply {
            putExtra("USUARIO_ID", usuarioId)
            putExtra("PACIENTE_ID", pacienteId)
            putExtra("PLAN_ID", selectedPlanId ?: -1)
            putExtra("FORMA_PAGO_ID", selectedFormaPagoId ?: -1)
            putExtra("FORMA_PAGO", binding.spformapago.selectedItem?.toString() ?: "")
        }
        startActivity(intent)
        finish()
    }

    private fun fetchPlanes() {
        RetrofitClient.planesApi.getPlanes().enqueue(object : Callback<PlanesResponse> {
            override fun onResponse(call: Call<PlanesResponse>, response: Response<PlanesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val planesResponse = response.body()!!
                    planList = planesResponse.planes.filter { it.estado }

                    if (planList.isEmpty()) {
                        toast("No hay planes activos")
                        return
                    }

                    val adapter = ArrayAdapter(
                        this@RegistroUsuario,
                        android.R.layout.simple_spinner_item,
                        planList.map { it.tipoPlan }
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spPlan.adapter = adapter

                    binding.spPlan.setOnItemSelectedListener(object :
                        android.widget.AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: android.widget.AdapterView<*>,
                            view: android.view.View?,
                            pos: Int,
                            id: Long
                        ) {
                            selectedPlanId = planList[pos].idPlan
                        }

                        override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
                    })
                } else {
                    Log.e(TAG, "❌ Error cargando planes: ${response.errorBody()?.string()}")
                    toast("Error al cargar planes")
                }
            }

            override fun onFailure(call: Call<PlanesResponse>, t: Throwable) {
                Log.e(TAG, "🚨 Error de conexión al cargar planes", t)
                toast("No se pudo conectar al servidor de planes")
            }
        })
    }

    private fun loadEps() {
        RetrofitClient.epsApi.getEps().enqueue(object : Callback<List<Eps>> {
            override fun onResponse(call: Call<List<Eps>>, response: Response<List<Eps>>) {
                if (response.isSuccessful && response.body() != null) {
                    epsList = response.body()!!
                    if (epsList.isEmpty()) {
                        toast("No hay EPS disponibles")
                        return
                    }

                    val adapter = ArrayAdapter(
                        this@RegistroUsuario,
                        android.R.layout.simple_spinner_item,
                        epsList.map { it.nombreEps }
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spEps.adapter = adapter

                    binding.spEps.setOnItemSelectedListener(object :
                        android.widget.AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: android.widget.AdapterView<*>,
                            view: android.view.View?,
                            pos: Int,
                            id: Long
                        ) {
                            selectedEpsId = epsList[pos].idEps
                        }

                        override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
                    })
                } else {
                    Log.e(TAG, "❌ Error cargando EPS: ${response.errorBody()?.string()}")
                    toast("Error al cargar EPS")
                }
            }

            override fun onFailure(call: Call<List<Eps>>, t: Throwable) {
                Log.e(TAG, "🚨 Error conexión EPS", t)
                toast("No se pudo conectar al servidor de EPS")
            }
        })
    }

    private fun loadFormasPago() {
        RetrofitClient.formaPagoApi.getFormasPago().enqueue(object : Callback<List<FormaPago>> {
            override fun onResponse(
                call: Call<List<FormaPago>>,
                response: Response<List<FormaPago>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    formaPagoList = response.body()!!.filter { it.estado }

                    if (formaPagoList.isEmpty()) {
                        toast("No hay formas de pago disponibles")
                        return
                    }

                    val adapter = ArrayAdapter(
                        this@RegistroUsuario,
                        android.R.layout.simple_spinner_item,
                        formaPagoList.mapNotNull { it.tipoPago }
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spformapago.adapter = adapter

                    binding.spformapago.setOnItemSelectedListener(object :
                        android.widget.AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: android.widget.AdapterView<*>,
                            view: android.view.View?,
                            pos: Int,
                            id: Long
                        ) {
                            val formaSeleccionada = formaPagoList[pos]
                            selectedFormaPagoId = formaSeleccionada.idFormaPago
                            Log.d(TAG, "💳 Forma de pago seleccionada: ${formaSeleccionada.tipoPago} (ID: ${formaSeleccionada.idFormaPago})")
                        }

                        override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
                    })
                } else {
                    Log.e(TAG, "❌ Error al cargar formas de pago: ${response.errorBody()?.string()}")
                    toast("Error al cargar formas de pago")
                }
            }

            override fun onFailure(call: Call<List<FormaPago>>, t: Throwable) {
                Log.e(TAG, "🚨 Error de conexión al cargar formas de pago", t)
                toast("No se pudo conectar al servidor de formas de pago")
            }
        })
    }

    private fun setupStaticSpinners() {
        val spinners = mapOf(
            binding.spTipoDocumento to R.array.tipos_documento,
            binding.spAutorizacionDatos to R.array.autorizacion_datos,
            binding.spGenero to R.array.generos,
            binding.spEstadoCivil to R.array.estados_civiles,
            binding.spRol to R.array.rol_paciente
        )
        spinners.forEach { (spinner, arrayRes) ->
            val adapter = ArrayAdapter.createFromResource(
                this,
                arrayRes,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        binding.spRol.isEnabled = false
    }

    private fun setupDatePicker(editText: android.widget.EditText) {
        editText.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                editText.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", y, m + 1, d))
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
