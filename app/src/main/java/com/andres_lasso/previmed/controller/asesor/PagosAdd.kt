package com.andres_lasso.previmed.view.pagos

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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

    // 🔥 Tomamos el ID del asesor logueado desde PreferenceHelper
    private val asesorId: String by lazy {
        PreferenceHelper.getIdAsesor(this) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagosAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarTitulares()
        cargarFormasPago()
        setupDatePickers()

        binding.btnGuardar.setOnClickListener {
            if (validarCampos()) registrarPago()
        }
    }

    private fun setupDatePickers() {
        val listener = { editText: EditText ->
            val c = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    editText.setText(String.format("%04d-%02d-%02d", year, month + 1, day))
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.etFechaInicio.setOnClickListener { listener(binding.etFechaInicio) }
        binding.etFechaFin.setOnClickListener { listener(binding.etFechaFin) }
        binding.etFechaPago.setOnClickListener { listener(binding.etFechaPago) }
    }

    private fun validarCampos(): Boolean {
        return when {
            membresiaIdSeleccionada == -1 -> {
                Toast.makeText(this, "Selecciona un titular", Toast.LENGTH_SHORT).show(); false
            }
            formaPagoIdSeleccionada == -1 -> {
                Toast.makeText(this, "Selecciona una forma de pago", Toast.LENGTH_SHORT).show(); false
            }
            binding.etMonto.text.isNullOrBlank() -> {
                Toast.makeText(this, "Ingresa el monto", Toast.LENGTH_SHORT).show(); false
            }
            binding.etFechaInicio.text.isNullOrBlank() -> {
                Toast.makeText(this, "Selecciona la fecha de inicio", Toast.LENGTH_SHORT).show(); false
            }
            binding.etFechaFin.text.isNullOrBlank() -> {
                Toast.makeText(this, "Selecciona la fecha de fin", Toast.LENGTH_SHORT).show(); false
            }
            binding.etFechaPago.text.isNullOrBlank() -> {
                Toast.makeText(this, "Selecciona la fecha de pago", Toast.LENGTH_SHORT).show(); false
            }
            binding.etNumeroRecibo.text.isNullOrBlank() -> {
                Toast.makeText(this, "Ingresa el número de recibo", Toast.LENGTH_SHORT).show(); false
            }
            asesorId.isEmpty() -> {
                Toast.makeText(this, "Error: No se encontró el ID del asesor", Toast.LENGTH_LONG).show()
                false
            }
            else -> true
        }
    }

    private fun cargarTitulares() {
        RetrofitClient.pacienteApi.getTitulares().enqueue(object :
            Callback<ApiResponse<List<PacienteData>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<PacienteData>>>,
                response: Response<ApiResponse<List<PacienteData>>>
            ) {
                if (response.isSuccessful) {
                    listaTitulares = response.body()?.data ?: emptyList()
                    val nombres = listaTitulares.map { it.usuario?.nombre + " " + it.usuario?.apellido }
                    val adapter = ArrayAdapter(this@PagosAdd, android.R.layout.simple_spinner_item, nombres)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerTitular.adapter = adapter

                    binding.spinnerTitular.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
                        ) {
                            membresiaIdSeleccionada =
                                listaTitulares[position].membresiaPaciente?.firstOrNull()?.membresiaId ?: -1
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                } else {
                    Toast.makeText(this@PagosAdd, "Error al cargar titulares", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<PacienteData>>>, t: Throwable) {
                Toast.makeText(this@PagosAdd, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun cargarFormasPago() {
        RetrofitClient.formaPagoApi.getFormasPago().enqueue(object : Callback<List<FormaPago>> {
            override fun onResponse(call: Call<List<FormaPago>>, response: Response<List<FormaPago>>) {
                if (response.isSuccessful) {
                    listaFormasPago = response.body() ?: emptyList()
                    val nombres = listaFormasPago.map { it.tipoPago ?: "Sin nombre" }
                    val adapter = ArrayAdapter(this@PagosAdd, android.R.layout.simple_spinner_item, nombres)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerFormaPago.adapter = adapter

                    binding.spinnerFormaPago.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
                        ) {
                            formaPagoIdSeleccionada = listaFormasPago[position].idFormaPago ?: -1
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                } else {
                    Toast.makeText(this@PagosAdd, "Error al cargar formas de pago", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<FormaPago>>, t: Throwable) {
                Toast.makeText(this@PagosAdd, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun registrarPago() {

        val monto = binding.etMonto.text.toString().toDouble()
        val fechaInicio = binding.etFechaInicio.text.toString()
        val fechaFin = binding.etFechaFin.text.toString()
        val fechaPago = binding.etFechaPago.text.toString()
        val numeroRecibo = binding.etNumeroRecibo.text.toString()

        val pagoRequest = PagoRequest(
            monto = monto,
            fecha_inicio = fechaInicio,
            fecha_fin = fechaFin,
            fecha_pago = fechaPago,
            membresia_id = membresiaIdSeleccionada,
            forma_pago_id = formaPagoIdSeleccionada,

            // 🔥 Cambio importante → ahora enviamos el asesor real
            cobrador_id = asesorId,

            numero_recibo = numeroRecibo,
            estado = "Pendiente",
            foto = null
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.pagoApi.createPago(pagoRequest)
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PagosAdd, "Pago registrado correctamente", Toast.LENGTH_LONG).show()
                        limpiarCampos()
                    } else {
                        Toast.makeText(this@PagosAdd, "Error al registrar pago: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@PagosAdd, "Excepción: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun limpiarCampos() {
        binding.etMonto.text.clear()
        binding.etFechaInicio.text.clear()
        binding.etFechaFin.text.clear()
        binding.etFechaPago.text.clear()
        binding.etNumeroRecibo.text.clear()
        binding.spinnerTitular.setSelection(0)
        binding.spinnerFormaPago.setSelection(0)
        membresiaIdSeleccionada = -1
        formaPagoIdSeleccionada = -1
    }
}
