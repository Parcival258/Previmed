package com.andres_lasso.previmed.controller.asesor

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.ApiResponse
import com.andres_lasso.previmed.model.Membresia
import com.andres_lasso.previmed.controller.asesor.ContratoAsesorActivity
import com.andres_lasso.previmed.model.MembresiaRequest
import com.andres_lasso.previmed.model.MembresiaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ContratoActivity : AppCompatActivity() {

    // 🧱 Vistas
    private lateinit var etFirma: EditText
    private lateinit var etNumeroContrato: EditText
    private lateinit var etFechaInicioContrato: EditText
    private lateinit var etFechaFinContrato: EditText
    private lateinit var pbLoading: ProgressBar
    private lateinit var btnGuardarMembresia: Button
    private lateinit var tvIdMembresia: TextView

    // 📦 Variables recibidas
    private var usuarioId: String? = null
    private var pacienteId: Int? = null
    private var planId: Int? = null
    private var formaPago: String? = null
    private var formaPagoId: Int? = null

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrato)

        // ✅ Recibir datos del intent
        usuarioId = intent.getStringExtra("USUARIO_ID")
        pacienteId = intent.getIntExtra("PACIENTE_ID", -1).takeIf { it != -1 }
        planId = intent.getIntExtra("PLAN_ID", -1).takeIf { it != -1 }
        formaPago = intent.getStringExtra("FORMA_PAGO")
        formaPagoId = intent.getIntExtra("FORMA_PAGO_ID", -1).takeIf { it != -1 }

        // 🔍 LOGS de control
        Log.d("ContratoActivity", "usuarioId: $usuarioId")
        Log.d("ContratoActivity", "pacienteId: $pacienteId")
        Log.d("ContratoActivity", "planId: $planId")
        Log.d("ContratoActivity", "formaPago: $formaPago")
        Log.d("ContratoActivity", "formaPagoId: $formaPagoId")

        // ✅ Inicializar vistas
        etFirma = findViewById(R.id.etFirma)
        etNumeroContrato = findViewById(R.id.etNumeroContrato)
        etFechaInicioContrato = findViewById(R.id.etFechaInicioContrato)
        etFechaFinContrato = findViewById(R.id.etFechaFinContrato)
        pbLoading = findViewById(R.id.pbLoading)
        btnGuardarMembresia = findViewById(R.id.btnRegistrarContrato)
        tvIdMembresia = findViewById(R.id.tvIdMembresia)

        // ✅ Generar número único de contrato
        etNumeroContrato.setText("CT-${System.currentTimeMillis()}")

        // ✅ Fechas por defecto (inicio hoy, fin +1 año)
        val cInicio = Calendar.getInstance()
        val cFin = Calendar.getInstance().apply { add(Calendar.YEAR, 1) }
        etFechaInicioContrato.setText(dateFormat.format(cInicio.time))
        etFechaFinContrato.setText(dateFormat.format(cFin.time))

        // ✅ DatePickers
        etFechaInicioContrato.setOnClickListener { showDatePicker(etFechaInicioContrato) }
        etFechaFinContrato.setOnClickListener { showDatePicker(etFechaFinContrato) }

        // ✅ Guardar membresía al presionar
        btnGuardarMembresia.setOnClickListener { guardarMembresia() }
    }

    private fun showDatePicker(target: EditText) {
        val cal = Calendar.getInstance()
        val dpd = DatePickerDialog(
            this,
            { _, y, m, d ->
                val selected = Calendar.getInstance().apply { set(y, m, d) }
                target.setText(dateFormat.format(selected.time))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show()
    }

    private fun guardarMembresia() {
        val firma = etFirma.text.toString().trim()
        val numeroContrato = etNumeroContrato.text.toString().trim()
        val fechaInicio = etFechaInicioContrato.text.toString().trim()
        val fechaFin = etFechaFinContrato.text.toString().trim()

        if (firma.isEmpty()) {
            Toast.makeText(this, "Debe ingresar la firma", Toast.LENGTH_SHORT).show()
            return
        }

        pbLoading.visibility = View.VISIBLE
        btnGuardarMembresia.isEnabled = false

        val request = MembresiaRequest(
            pacienteId = pacienteId,
            formaPagoId = formaPagoId,
            planId = planId,
            numeroContrato = numeroContrato,
            fechaInicio = fechaInicio,
            fechaFin = fechaFin,
            estado = true,
            firma = firma,
            formaPago = formaPago
        )

        Log.d("ContratoActivity", "➡️ Enviando request: $request")

        RetrofitClient.membresiaApi.crearMembresia(request)
            .enqueue(object : Callback<Membresia> {
                override fun onResponse(call: Call<Membresia>, response: Response<Membresia>) {
                    pbLoading.visibility = View.GONE
                    btnGuardarMembresia.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val membresiaCreada = response.body()!!
                        val idMembresia = membresiaCreada.idMembresia ?: 0

                        Log.d("ContratoActivity", "✅ ID obtenido directamente: $idMembresia")
                        Log.d("ContratoActivity", "🧩 Detalle completo: $membresiaCreada")

                        tvIdMembresia.text = "Contrato creado con ID: $idMembresia"
                        tvIdMembresia.visibility = View.VISIBLE

                        Toast.makeText(
                            this@ContratoActivity,
                            "✅ Contrato registrado exitosamente",
                            Toast.LENGTH_LONG
                        ).show()

                        // Ir a la pantalla siguiente
                        val intent = Intent(this@ContratoActivity, AsociarBeneficiario::class.java)
                        intent.putExtra("MEMBRESIA_ID", idMembresia)
                        intent.putExtra("FORMA_PAGO", formaPago)
                        intent.putExtra("FORMA_PAGO_ID", formaPagoId)
                        intent.putExtra("PLAN_ID", planId ?: -1)
                        intent.putExtra("PACIENTE_ID_TITULAR", pacienteId ?: -1)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@ContratoActivity,
                            "Error al registrar contrato: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.w("ContratoActivity", "Respuesta sin body: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Membresia>, t: Throwable) {
                    pbLoading.visibility = View.GONE
                    btnGuardarMembresia.isEnabled = true
                    Toast.makeText(
                        this@ContratoActivity,
                        "⚠️ Error de conexión: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("ContratoActivity", "Fallo en conexión: ${t.message}", t)
                }
            })

    }

    /** ✅ Método auxiliar para continuar flujo con el id correcto */
    private fun procesarMembresiaCreada(idMembresia: Int?) {
        if (idMembresia == null || idMembresia == 0) {
            Toast.makeText(this, "⚠️ No se pudo obtener el ID de la membresía", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("ContratoActivity", "🔑 idMembresia obtenido: $idMembresia")

        tvIdMembresia.text = "Contrato creado con ID: $idMembresia"
        tvIdMembresia.visibility = View.VISIBLE

        val intent = Intent(this, AsociarBeneficiario::class.java)
        intent.putExtra("MEMBRESIA_ID", idMembresia)
        intent.putExtra("FORMA_PAGO", formaPago)
        intent.putExtra("FORMA_PAGO_ID", formaPagoId)
        intent.putExtra("PLAN_ID", planId ?: -1)
        intent.putExtra("PACIENTE_ID_TITULAR", pacienteId ?: -1)
        startActivity(intent)
        finish()
    }

}
