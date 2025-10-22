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
    private lateinit var etFechaPago: EditText
    private lateinit var btnRegistrarPago: Button
    private lateinit var btnNuevoPago: Button
    private lateinit var tvMembresia: TextView
    private lateinit var tvFormaPago: TextView
    private lateinit var tvFechasContrato: TextView
    private lateinit var btnSubirFoto: Button
    private lateinit var imgPreview: ImageView
    private lateinit var progressBar: ProgressBar

    private var membresiaId: Int = -1
    private var formaPagoId: Int = 0
    private var fechaInicio: String = ""
    private var fechaFin: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_pago)

        initViews()
        setupDatePicker()
        recibirDatosIntent()

        btnRegistrarPago.setOnClickListener {
            if (validarCampos()) registrarPago()
        }

        btnNuevoPago.setOnClickListener { limpiarCampos() }
    }

    /** Inicializa vistas */
    private fun initViews() {
        etMonto = findViewById(R.id.etMonto)
        etFechaPago = findViewById(R.id.etFechaPago)
        btnRegistrarPago = findViewById(R.id.btnRegistrarPago)
        btnNuevoPago = findViewById(R.id.btnNuevoPago)
        tvMembresia = findViewById(R.id.tvMembresia)
        tvFormaPago = findViewById(R.id.tvFormaPago)
        tvFechasContrato = findViewById(R.id.tvFechasContrato)
        btnSubirFoto = findViewById(R.id.btnSubirFoto)
        imgPreview = findViewById(R.id.imgPreview)
        progressBar = findViewById(R.id.pbCargandoPago)

        // Ocultar botones e imagen hasta implementar subida
        btnSubirFoto.visibility = View.GONE
        imgPreview.visibility = View.GONE
    }

    /** Recibe los datos de la membresía y forma de pago */
    private fun recibirDatosIntent() {
        membresiaId = intent.getIntExtra("MEMBRESIA_ID", -1)
        formaPagoId = intent.getIntExtra("FORMA_PAGO_ID", 0)
        fechaInicio = intent.getStringExtra("FECHA_INICIO") ?: ""
        fechaFin = intent.getStringExtra("FECHA_FIN") ?: ""
        val numeroContrato = intent.getStringExtra("NUMERO_CONTRATO") ?: "Sin contrato"
        val formaPagoTexto = intent.getStringExtra("FORMA_PAGO") ?: "Desconocido"

        tvMembresia.text = "Membresía ID: $membresiaId"
        tvFormaPago.text = "Forma de pago: $formaPagoTexto"
        tvFechasContrato.text = "Contrato: $numeroContrato\n($fechaInicio → $fechaFin)"
    }

    /** Selector de fecha */
    private fun setupDatePicker() {
        etFechaPago.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    etFechaPago.setText(String.format("%04d-%02d-%02d", year, month + 1, day))
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    /** Validar campos */
    private fun validarCampos(): Boolean {
        return when {
            etMonto.text.isNullOrBlank() -> {
                Toast.makeText(this, "💰 El monto es obligatorio", Toast.LENGTH_SHORT).show(); false
            }
            etFechaPago.text.isNullOrBlank() -> {
                Toast.makeText(this, "📅 Selecciona la fecha de pago", Toast.LENGTH_SHORT).show(); false
            }
            membresiaId == -1 -> {
                Toast.makeText(this, "❗ Falta el ID de membresía", Toast.LENGTH_SHORT).show(); false
            }
            fechaInicio.isBlank() || fechaFin.isBlank() -> {
                Toast.makeText(this, "⚠️ Faltan fechas del contrato", Toast.LENGTH_SHORT).show(); false
            }
            else -> true
        }
    }

    /** Limpia campos */
    private fun limpiarCampos() {
        etMonto.text.clear()
        etFechaPago.text.clear()
    }

    /** Registrar pago en el backend */
    private fun registrarPago() {
        val monto = etMonto.text.toString().toDouble()
        val fechaPago = etFechaPago.text.toString()

        val pago = PagoRequest(
            monto = monto,
            fecha_inicio = fechaInicio,
            fecha_fin = fechaFin,
            fecha_pago = fechaPago,
            membresia_id = membresiaId,
            forma_pago_id = formaPagoId,
            foto = null
        )

        progressBar.visibility = View.VISIBLE
        btnRegistrarPago.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.pagoApi.createPago(pago)

                runOnUiThread {
                    progressBar.visibility = View.GONE
                    btnRegistrarPago.isEnabled = true

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@RegistrarPagoActivity,
                            "✅ Pago registrado correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                        limpiarCampos()
                    } else {
                        Toast.makeText(
                            this@RegistrarPagoActivity,
                            "❌ Error al registrar pago (${response.code()})",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("RegistrarPago", "Error body: ${response.errorBody()?.string()}")
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    btnRegistrarPago.isEnabled = true
                    Toast.makeText(
                        this@RegistrarPagoActivity,
                        "⚠️ Falló la conexión o hubo un error inesperado",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("RegistrarPago", "Error: ${e.message}", e)
                }
            }
        }
    }

    companion object {
        fun openRegistrarPago(
            context: Context,
            membresiaId: Int,
            formaPagoId: Int,
            fechaInicioStr: String,
            fechaFinStr: String,
            numeroContrato: String,
            formaPago: String? = null
        ) {
            val intent = Intent(context, RegistrarPagoActivity::class.java).apply {
                putExtra("MEMBRESIA_ID", membresiaId)
                putExtra("FORMA_PAGO_ID", formaPagoId)
                putExtra("FECHA_INICIO", fechaInicioStr)
                putExtra("FECHA_FIN", fechaFinStr)
                putExtra("NUMERO_CONTRATO", numeroContrato)
                putExtra("FORMA_PAGO", formaPago)
            }
            context.startActivity(intent)
        }
    }
}
