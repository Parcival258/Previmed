package com.andres_lasso.previmed.view.pagos

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.ActivityPagosAddBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.*
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PagosAdd : AppCompatActivity() {

    private lateinit var binding: ActivityPagosAddBinding
    private var listaTitulares = listOf<PacienteData>()
    private var listaFormasPago = listOf<FormaPago>()
    private var membresiaIdSeleccionada: Int = -1
    private var formaPagoIdSeleccionada: Int = -1

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val asesorId: String by lazy {
        PreferenceHelper.getIdAsesor(this) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityPagosAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        window.statusBarColor = ContextCompat.getColor(this, R.color.statusBarLiteGray)

        setupBiometric()
        cargarTitulares()
        cargarFormasPago()
        setupDatePickers()

        // Para que abra la lista como spinner
        binding.autoTitular.setOnClickListener {
            binding.autoTitular.showDropDown()
        }

        // Búsqueda mientras escribe
        binding.autoTitular.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.autoTitular.showDropDown()
            }
        })

        binding.btnGuardar.setOnClickListener {
            if (validarCampos()) {
                if (isBiometricAvailable()) {
                    mostrarBiometricPrompt()
                } else {
                    registrarPago()
                }
            }
        }
    }

    private fun setupBiometric() {
        val executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    registrarPago()
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Confirmar Registro de Pago")
            .setSubtitle("Verifica tu identidad para registrar el pago")
            .setDescription("Usa tu huella digital o reconocimiento facial")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    private fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_WEAK or
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    private fun mostrarBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }

    private fun setupDatePickers() {
        val addPicker = { editText: EditText ->
            val c = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d -> editText.setText(String.format("%04d-%02d-%02d", y, m + 1, d)) },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.etFechaInicio.setOnClickListener { addPicker(binding.etFechaInicio) }
        binding.etFechaFin.setOnClickListener { addPicker(binding.etFechaFin) }
        binding.etFechaPago.setOnClickListener { addPicker(binding.etFechaPago) }
    }

    private fun validarCampos(): Boolean {
        return when {
            membresiaIdSeleccionada == -1 -> show("Selecciona un titular")
            formaPagoIdSeleccionada == -1 -> show("Selecciona una forma de pago")
            binding.etMonto.text.isNullOrBlank() -> show("Ingresa el monto")
            binding.etFechaInicio.text.isNullOrBlank() -> show("Selecciona fecha de inicio")
            binding.etFechaFin.text.isNullOrBlank() -> show("Selecciona fecha fin")
            binding.etFechaPago.text.isNullOrBlank() -> show("Selecciona fecha pago")
            binding.etNumeroRecibo.text.isNullOrBlank() -> show("Ingresa número de recibo")
            asesorId.isEmpty() -> show("No se encontró el asesor")
            else -> true
        }
    }

    private fun show(msg: String): Boolean {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        return false
    }

    private fun cargarTitulares() {
        RetrofitClient.pacienteApi.getTitulares().enqueue(object :
            Callback<ApiResponse<List<PacienteData>>> {

            override fun onResponse(
                call: Call<ApiResponse<List<PacienteData>>>,
                res: Response<ApiResponse<List<PacienteData>>>
            ) {
                listaTitulares = res.body()?.data ?: emptyList()

                val nombres = listaTitulares.map {
                    "${it.usuario?.nombre} ${it.usuario?.apellido}"
                }

                val adapter = ArrayAdapter(
                    this@PagosAdd,
                    android.R.layout.simple_dropdown_item_1line,
                    nombres
                )

                binding.autoTitular.setAdapter(adapter)

                binding.autoTitular.setOnItemClickListener { _, _, pos, _ ->
                    membresiaIdSeleccionada =
                        listaTitulares[pos].membresiaPaciente?.firstOrNull()?.membresiaId ?: -1
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<PacienteData>>>, t: Throwable) {
                show("Error cargando titulares")
            }
        })
    }

    private fun cargarFormasPago() {
        RetrofitClient.formaPagoApi.getFormasPago().enqueue(object :
            Callback<List<FormaPago>> {

            override fun onResponse(call: Call<List<FormaPago>>, res: Response<List<FormaPago>>) {
                listaFormasPago = res.body() ?: emptyList()

                val nombres = listaFormasPago.map { it.tipoPago ?: "" }

                val adapter = ArrayAdapter(
                    this@PagosAdd,
                    android.R.layout.simple_spinner_item,
                    nombres
                )

                binding.spinnerFormaPago.adapter = adapter

                binding.spinnerFormaPago.setOnItemSelectedListener(object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: android.view.View?,
                        pos: Int,
                        id: Long
                    ) {
                        formaPagoIdSeleccionada = listaFormasPago[pos].idFormaPago ?: -1
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                })
            }

            override fun onFailure(call: Call<List<FormaPago>>, t: Throwable) {
                show("Error cargando formas de pago")
            }
        })
    }

    private fun registrarPago() {

        val pago = PagoRequest(
            monto = binding.etMonto.text.toString().toDouble(),
            fecha_inicio = binding.etFechaInicio.text.toString(),
            fecha_fin = binding.etFechaFin.text.toString(),
            fecha_pago = binding.etFechaPago.text.toString(),
            membresia_id = membresiaIdSeleccionada,
            forma_pago_id = formaPagoIdSeleccionada,
            cobrador_id = asesorId,
            numero_recibo = binding.etNumeroRecibo.text.toString(),
            estado = "Pendiente",
            foto = null
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = RetrofitClient.pagoApi.createPago(pago)

                runOnUiThread {
                    if (res.isSuccessful) {
                        show("Pago registrado")
                        limpiar()
                    } else {
                        show("Error: ${res.code()}")
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    show("Excepción: ${e.message}")
                }
            }
        }
    }

    private fun limpiar() {
        binding.etMonto.text.clear()
        binding.etFechaInicio.text.clear()
        binding.etFechaFin.text.clear()
        binding.etFechaPago.text.clear()
        binding.etNumeroRecibo.text.clear()
        binding.autoTitular.text.clear()
        binding.spinnerFormaPago.setSelection(0)
    }
}
