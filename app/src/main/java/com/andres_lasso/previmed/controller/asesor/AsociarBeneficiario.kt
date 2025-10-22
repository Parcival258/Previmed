package com.andres_lasso.previmed.controller.asesor

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.interfaces.RetrofitClient.pacienteApi
import com.andres_lasso.previmed.model.ApiResponse
import com.andres_lasso.previmed.model.PacienteData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AsociarBeneficiario : AppCompatActivity() {

    private lateinit var spinnerTitulares: Spinner
    private lateinit var btnAsociar: Button
    private lateinit var progressBar: ProgressBar

    private var listaTitulares: List<PacienteData> = emptyList()
    private var beneficiarioSeleccionado: PacienteData? = null
    private var titularSeleccionado: PacienteData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asociar_beneficiario)

        spinnerTitulares = findViewById(R.id.spTitular)
        btnAsociar = findViewById(R.id.btnAsociar)
        progressBar = findViewById(R.id.progressBar)

        // ✅ Recuperar beneficiario desde el Intent
        beneficiarioSeleccionado = intent.getSerializableExtra("beneficiario") as? PacienteData

        if (beneficiarioSeleccionado == null) {
            mostrarDialogo("Error", "No se encontró el beneficiario a asociar.")
            return
        }

        cargarTitulares()

        btnAsociar.setOnClickListener {
            titularSeleccionado?.let { titular ->
                beneficiarioSeleccionado?.let { beneficiario ->
                    asociarBeneficiarioConTitular(beneficiario, titular)
                }
            } ?: mostrarDialogo("Error", "Debe seleccionar un titular para asociar.")
        }
    }

    private fun cargarTitulares() {
        progressBar.visibility = View.VISIBLE

        pacienteApi.getTitulares().enqueue(object : Callback<ApiResponse<List<PacienteData>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<PacienteData>>>,
                response: Response<ApiResponse<List<PacienteData>>>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body()?.data != null) {
                    listaTitulares = response.body()!!.data!!
                    configurarSpinner()
                } else {
                    mostrarDialogo("Error", "No se pudieron cargar los titulares.")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<PacienteData>>>, t: Throwable) {
                progressBar.visibility = View.GONE
                mostrarDialogo("Error", "Error de conexión: ${t.localizedMessage}")
            }
        })
    }

    private fun configurarSpinner() {
        val nombresTitulares = listaTitulares.map {
            val nombre = it.usuario?.nombre ?: ""
            val apellido = it.usuario?.apellido ?: ""
            "$nombre $apellido (ID: ${it.idPaciente ?: "?"})"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresTitulares)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTitulares.adapter = adapter

        spinnerTitulares.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                titularSeleccionado = listaTitulares[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                titularSeleccionado = null
            }
        }
    }

    private fun asociarBeneficiarioConTitular(beneficiario: PacienteData, titular: PacienteData) {
        progressBar.visibility = View.VISIBLE

        val idBeneficiario = beneficiario.idPaciente
        val idTitular = titular.idPaciente

        if (idBeneficiario == null || idTitular == null) {
            progressBar.visibility = View.GONE
            mostrarDialogo("Error", "IDs inválidos para la asociación.")
            return
        }

        // 🔹 JSON que se enviará al backend
        val body = mapOf("paciente_id" to idTitular)

        // 🔹 Llamar a la API correcta (usa el ID del beneficiario)
        pacienteApi.asociarBeneficiario(idBeneficiario, body)
            .enqueue(object : Callback<ApiResponse<PacienteData>> {
                override fun onResponse(
                    call: Call<ApiResponse<PacienteData>>,
                    response: Response<ApiResponse<PacienteData>>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        mostrarDialogo(
                            "Éxito",
                            "El beneficiario fue asociado correctamente al titular ${titular.usuario?.nombre ?: ""}."
                        )
                    } else {
                        mostrarDialogo("Error", "No se pudo asociar el beneficiario.")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<PacienteData>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    mostrarDialogo("Error", "Error al conectar: ${t.localizedMessage}")
                }
            })
    }

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
