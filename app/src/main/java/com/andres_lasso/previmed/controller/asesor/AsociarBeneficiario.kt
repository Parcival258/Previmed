package com.andres_lasso.previmed.controller.asesor

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.ApiResponse
import com.andres_lasso.previmed.model.PacienteData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AsociarBeneficiario : AppCompatActivity() {

    private lateinit var spTitular: Spinner
    private lateinit var spBeneficiario: Spinner
    private lateinit var btnAsociar: Button
    private lateinit var btnOmitir: Button
    private lateinit var progressBar: ProgressBar

    private var listaTitulares: List<PacienteData> = emptyList()
    private var listaBeneficiarios: List<PacienteData> = emptyList()

    private var titularSeleccionado: PacienteData? = null
    private var beneficiarioSeleccionado: PacienteData? = null
    private var titularIdExtra: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asociar_beneficiario)

        // Referencias del layout
        spTitular = findViewById(R.id.spTitular)
        spBeneficiario = findViewById(R.id.spBeneficiario)
        btnAsociar = findViewById(R.id.btnAsociar)
        btnOmitir = findViewById(R.id.btnOmitir)
        progressBar = findViewById(R.id.progressBar)

        // Recuperar ID del titular pasado desde la actividad anterior
        titularIdExtra = intent.getIntExtra("PACIENTE_ID_TITULAR", -1).takeIf { it != -1 }

        // Cargar datos
        cargarPacientes()

        // Eventos de botones
        btnAsociar.setOnClickListener {
            android.util.Log.d("BTN_ASOCIAR", "👉 Botón presionado")

            if (titularSeleccionado != null && beneficiarioSeleccionado != null) {
                android.util.Log.d("BTN_ASOCIAR", "Titular: ${titularSeleccionado?.idPaciente}, Beneficiario: ${beneficiarioSeleccionado?.idPaciente}")
                asociarBeneficiarioConTitular(beneficiarioSeleccionado!!, titularSeleccionado!!)
            } else {
                mostrarDialogo("Error", "Debe seleccionar titular y beneficiario para asociar.")
            }
        }

        btnOmitir.setOnClickListener {
            irARegistrarPago()
        }
    }

    /** 🔹 Cargar todos los pacientes (titulares y beneficiarios) */
    private fun cargarPacientes() {
        progressBar.visibility = View.VISIBLE

        RetrofitClient.pacienteApi.getPacientes()
            .enqueue(object : Callback<ApiResponse<List<PacienteData>>> {
                override fun onResponse(
                    call: Call<ApiResponse<List<PacienteData>>>,
                    response: Response<ApiResponse<List<PacienteData>>>
                ) {
                    progressBar.visibility = View.GONE

                    val pacientes = response.body()?.data ?: emptyList()
                    if (pacientes.isEmpty()) {
                        mostrarDialogo("Aviso", "No hay pacientes registrados.")
                        return
                    }

                    listaTitulares = pacientes.filter { it.beneficiario != true } // los que NO son beneficiarios
                    listaBeneficiarios = pacientes.filter { it.beneficiario == true || it.pacienteId == null } // los que pueden ser dependientes

                    android.util.Log.d("PACIENTES", "Total: ${pacientes.size}")
                    android.util.Log.d("PACIENTES", "Titulares: ${listaTitulares.size}, Beneficiarios: ${listaBeneficiarios.size}")

                    configurarSpinnerTitulares()
                    configurarSpinnerBeneficiarios()
                }

                override fun onFailure(call: Call<ApiResponse<List<PacienteData>>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    mostrarDialogo("Error", "No se pudo cargar la lista de pacientes: ${t.localizedMessage}")
                }
            })
    }

    /** 🔹 Configura el spinner de titulares */
    private fun configurarSpinnerTitulares() {
        val nombres = listaTitulares.map {
            "${it.usuario?.nombre ?: ""} ${it.usuario?.apellido ?: ""} (ID: ${it.idPaciente ?: "?"})"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTitular.adapter = adapter

        // Si venía un titular desde otra Activity, seleccionarlo
        titularSeleccionado = titularIdExtra?.let { id -> listaTitulares.find { it.idPaciente == id } }
        titularSeleccionado?.let {
            val pos = listaTitulares.indexOf(it)
            if (pos >= 0) spTitular.setSelection(pos)
        }

        spTitular.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                titularSeleccionado = listaTitulares[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                titularSeleccionado = null
            }
        }
    }

    /** 🔹 Configura el spinner de beneficiarios */
    private fun configurarSpinnerBeneficiarios() {
        val nombres = listaBeneficiarios.map {
            "${it.usuario?.nombre ?: ""} ${it.usuario?.apellido ?: ""} (ID: ${it.idPaciente ?: "?"})"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spBeneficiario.adapter = adapter

        spBeneficiario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                beneficiarioSeleccionado = listaBeneficiarios[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                beneficiarioSeleccionado = null
            }
        }
    }

    private fun asociarBeneficiarioConTitular(beneficiario: PacienteData, titular: PacienteData) {
        android.util.Log.d("ASOCIAR", "🚀 Iniciando asociación...")

        val idBeneficiario = beneficiario.idPaciente
        val idTitular = titular.idPaciente

        android.util.Log.d("ASOCIAR", "IDs -> Titular=$idTitular, Beneficiario=$idBeneficiario")

        if (idBeneficiario == null || idTitular == null) {
            mostrarDialogo("Error", "IDs inválidos para la asociación.")
            return
        }

        progressBar.visibility = View.VISIBLE
        val body = mapOf(
            "paciente_id" to idTitular,
            "beneficiario" to true
        )

        RetrofitClient.pacienteApi.asociarBeneficiario(idBeneficiario, body)
            .enqueue(object : Callback<ApiResponse<PacienteData>> {
                override fun onResponse(
                    call: Call<ApiResponse<PacienteData>>,
                    response: Response<ApiResponse<PacienteData>>
                ) {
                    progressBar.visibility = View.GONE
                    android.util.Log.d("ASOCIAR", "Respuesta código: ${response.code()}")
                    android.util.Log.d("ASOCIAR", "Cuerpo: ${response.body()}")

                    if (response.isSuccessful) {
                        mostrarDialogo("Éxito", "El beneficiario fue asociado correctamente.")
                    } else {
                        mostrarDialogo("Error", "No se pudo asociar el beneficiario. Código: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<PacienteData>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    android.util.Log.e("ASOCIAR", "Error de conexión: ${t.message}")
                    mostrarDialogo("Error", "Error al conectar: ${t.localizedMessage}")
                }
            })
    }



    /** 🔹 Navegar a la pantalla de Registrar Pago */
    private fun irARegistrarPago() {
        val intent = Intent(this, RegistrarPagoActivity::class.java)
        intent.putExtra("MEMBRESIA_ID", 1)
        intent.putExtra("FORMA_PAGO_ID", 1)
        intent.putExtra("FORMA_PAGO", "Efectivo")
        startActivity(intent)
    }

    /** 🔹 Mostrar diálogo con autocierre */
    private fun mostrarDialogo(titulo: String, mensaje: String) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .create()

        dialog.show()
        Handler(Looper.getMainLooper()).postDelayed({ dialog.dismiss() }, 2500)
    }
}
