package com.andres_lasso.previmed.controller.asesor

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.PagoRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RegistrarPagoActivity : AppCompatActivity() {

    private lateinit var etMonto: EditText
    private lateinit var etFechaInicio: EditText
    private lateinit var etFechaFin: EditText
    private lateinit var etFechaPago: EditText
    private lateinit var btnRegistrarPago: Button
    private lateinit var btnNuevoPago: Button
    private lateinit var tvMembresia: TextView
    private lateinit var tvFormaPago: TextView
    private lateinit var progressBar: ProgressBar

    private var membresiaId: Int = -1
    private var formaPagoId: Int = 0

    private val TAG = "RegistrarPago"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_pago)

        initViews()
        setupDatePickers()
        recibirDatosIntent()

        btnRegistrarPago.setOnClickListener {
            if (validarCampos()) registrarPago()
        }

        btnNuevoPago.setOnClickListener { limpiarCampos() }
    }

    private fun initViews() {
        etMonto = findViewById(R.id.etMonto)
        etFechaInicio = findViewById(R.id.etFechaInicio)
        etFechaFin = findViewById(R.id.etFechaFin)
        etFechaPago = findViewById(R.id.etFechaPago)
        btnRegistrarPago = findViewById(R.id.btnRegistrarPago)
        btnNuevoPago = findViewById(R.id.btnNuevoPago)
        tvMembresia = findViewById(R.id.tvMembresia)
        tvFormaPago = findViewById(R.id.tvFormaPago)
        progressBar = findViewById(R.id.pbCargargandu)
    }

    private fun recibirDatosIntent() {
        membresiaId = intent.getIntExtra("MEMBRESIA_ID", -1)
        formaPagoId = intent.getIntExtra("FORMA_PAGO_ID", 0)
        val formaPagoTexto = intent.getStringExtra("FORMA_PAGO") ?: "Desconocido"

        tvMembresia.text = "🪪 Membresía N° $membresiaId"
        tvFormaPago.text = "💳 Forma de pago: $formaPagoTexto"

        Log.d(TAG, "📥 Datos recibidos -> MEMBRESIA_ID=$membresiaId, FORMA_PAGO_ID=$formaPagoId, FORMA_PAGO=$formaPagoTexto")
    }

    private fun setupDatePickers() {
        val listener = { et: EditText ->
            val c = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    et.setText(String.format("%04d-%02d-%02d", year, month + 1, day))
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        etFechaInicio.setOnClickListener { listener(etFechaInicio) }
        etFechaFin.setOnClickListener { listener(etFechaFin) }
        etFechaPago.setOnClickListener { listener(etFechaPago) }
    }

    private fun validarCampos(): Boolean {
        return when {
            etMonto.text.isNullOrBlank() -> {
                Toast.makeText(this, "💰 El monto es obligatorio", Toast.LENGTH_SHORT).show(); false
            }
            etFechaInicio.text.isNullOrBlank() -> {
                Toast.makeText(this, "📅 Selecciona la fecha de inicio", Toast.LENGTH_SHORT).show(); false
            }
            etFechaFin.text.isNullOrBlank() -> {
                Toast.makeText(this, "📅 Selecciona la fecha de fin", Toast.LENGTH_SHORT).show(); false
            }
            etFechaPago.text.isNullOrBlank() -> {
                Toast.makeText(this, "📅 Selecciona la fecha de pago", Toast.LENGTH_SHORT).show(); false
            }
            membresiaId == -1 -> {
                Toast.makeText(this, "❗ Falta el ID de membresía", Toast.LENGTH_SHORT).show(); false
            }
            else -> true
        }
    }

    private fun limpiarCampos() {
        etMonto.text.clear()
        etFechaInicio.text.clear()
        etFechaFin.text.clear()
        etFechaPago.text.clear()
        Log.d(TAG, "🧹 Campos limpiados")
    }

    private fun registrarPago() {
        val monto = etMonto.text.toString().toDouble()
        val fechaInicio = etFechaInicio.text.toString()
        val fechaFin = etFechaFin.text.toString()
        val fechaPago = etFechaPago.text.toString()

        Log.d(TAG, "🚀 Enviando pago sin foto:")
        Log.d(TAG, "   monto=$monto")
        Log.d(TAG, "   fecha_inicio=$fechaInicio")
        Log.d(TAG, "   fecha_fin=$fechaFin")
        Log.d(TAG, "   fecha_pago=$fechaPago")
        Log.d(TAG, "   membresia_id=$membresiaId")
        Log.d(TAG, "   forma_pago_id=$formaPagoId")

        progressBar.visibility = View.VISIBLE
        btnRegistrarPago.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pago = PagoRequest(
                    monto = monto,
                    fecha_inicio = fechaInicio,
                    fecha_fin = fechaFin,
                    fecha_pago = fechaPago,
                    membresia_id = membresiaId,
                    forma_pago_id = formaPagoId,
                    foto = null
                )

                val response = RetrofitClient.pagoApi.createPago(pago)

                runOnUiThread {
                    progressBar.visibility = View.GONE
                    btnRegistrarPago.isEnabled = true

                    if (response.isSuccessful) {
                        Log.i(TAG, "✅ Pago registrado correctamente: ${response.body()}")
                        Toast.makeText(this@RegistrarPagoActivity, "✅ Pago registrado correctamente", Toast.LENGTH_LONG).show()
                        limpiarCampos()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e(TAG, "❌ Error al registrar pago (${response.code()}): $errorBody")
                        Toast.makeText(this@RegistrarPagoActivity, "❌ Error al registrar pago (${response.code()})", Toast.LENGTH_LONG).show()
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    btnRegistrarPago.isEnabled = true
                    Log.e(TAG, "⚠️ Excepción: ${e.message}", e)
                    Toast.makeText(this@RegistrarPagoActivity, "⚠️ Error de conexión o excepción inesperada", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        fun openRegistrarPago(
            context: Context,
            membresiaId: Int,
            formaPagoId: Int,
            formaPago: String? = null
        ) {
            val intent = Intent(context, RegistrarPagoActivity::class.java).apply {
                putExtra("MEMBRESIA_ID", membresiaId)
                putExtra("FORMA_PAGO_ID", formaPagoId)
                putExtra("FORMA_PAGO", formaPago)
            }
            context.startActivity(intent)
        }
    }
}
