package com.andres_lasso.previmed.controller.paciente

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.model.*
import com.andres_lasso.previmed.interfaces.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SolicitarVisitaActivity : AppCompatActivity() {

    private lateinit var spinnerBarrios: Spinner
    private lateinit var spinnerMedicos: Spinner
    private lateinit var editFecha: EditText
    private lateinit var editDescripcion: EditText
    private lateinit var editDireccion: EditText
    private lateinit var editTelefono: EditText
    private lateinit var btnSolicitar: Button
    private lateinit var checkMedico: CheckBox
    private lateinit var tvLabelMedico: TextView

    private var barrios: List<Barrio> = emptyList()
    private var medicos: List<Medico> = emptyList()
    private val idPaciente: Int = 123 // 🔧 reemplaza con tu id real de paciente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitar_visita)

        // Vinculación de vistas
        spinnerBarrios = findViewById(R.id.spinnerBarrios)
        spinnerMedicos = findViewById(R.id.spinnerMedicos)
        editFecha = findViewById(R.id.edFecha)
        editDescripcion = findViewById(R.id.edDescripcion)
        editDireccion = findViewById(R.id.edDireccion)
        editTelefono = findViewById(R.id.edTelefono)
        btnSolicitar = findViewById(R.id.btnSolicitar)
        checkMedico = findViewById(R.id.checkMedico)
        tvLabelMedico = findViewById(R.id.tvLabelMedico)

        btnSolicitar.isEnabled = false

        // Listener para mostrar/ocultar spinner de médicos
        checkMedico.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                spinnerMedicos.visibility = View.VISIBLE
                spinnerMedicos.isEnabled = true
                tvLabelMedico.visibility = View.VISIBLE
            } else {
                spinnerMedicos.visibility = View.GONE
                spinnerMedicos.isEnabled = false
                tvLabelMedico.visibility = View.GONE
            }
        }

        cargarBarrios()
        cargarMedicos()

        btnSolicitar.setOnClickListener {
            val barrioSeleccionado = barrios.getOrNull(spinnerBarrios.selectedItemPosition)
            val medicoSeleccionado = if (checkMedico.isChecked)
                medicos.getOrNull(spinnerMedicos.selectedItemPosition) else null

            val fecha = editFecha.text.toString().trim()
            val descripcion = editDescripcion.text.toString().trim()
            val direccion = editDireccion.text.toString().trim()
            val telefono = editTelefono.text.toString().trim()

            if (barrioSeleccionado == null || fecha.isEmpty() || descripcion.isEmpty()
                || direccion.isEmpty() || telefono.isEmpty()
                || (checkMedico.isChecked && medicoSeleccionado == null)
            ) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val visita = Visita(
                fecha_visita = fecha,
                descripcion = descripcion,
                direccion = direccion,
                estado = "pendiente",
                paciente_id = idPaciente,
                medico_id = medicoSeleccionado?.id_medico, // puede ser null si no selecciona
                telefono = telefono,
                barrio_id = barrioSeleccionado.idBarrio
            )

            Log.d("VISITA", "Enviando visita: $visita")
            guardarVisita(visita)
        }
    }

    private fun cargarBarrios() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.barriosApi.listarBarrios()
                Log.d("API", "Código de respuesta barrios: ${response.code()}")
                Log.d("API", "Body barrios: ${response.body()}")

                val barriosResponse = response.body()
                barrios = barriosResponse?.msj ?: emptyList()

                Log.d("BARRIOS", "Barrios recibidos: $barrios")

                withContext(Dispatchers.Main) {
                    if (barrios.isNotEmpty()) {
                        val adapter = ArrayAdapter(
                            this@SolicitarVisitaActivity,
                            android.R.layout.simple_spinner_item,
                            barrios.map { it.nombreBarrio }
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerBarrios.adapter = adapter
                        habilitarBotonSiDatosCargados()
                    } else {
                        Toast.makeText(
                            this@SolicitarVisitaActivity,
                            "No hay barrios disponibles",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SolicitarVisitaActivity,
                        "Error al cargar barrios: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("BARRIOS", "Error cargar barrios", e)
                }
            }
        }
    }

    private fun cargarMedicos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.medicoApi.listarMedicos()
                Log.d("API", "Código de respuesta médicos: ${response.code()}")
                Log.d("API", "Body médicos: ${response.body()}")

                medicos = response.body()?.data ?: emptyList()
                Log.d("MEDICOS", "Médicos recibidos: $medicos")

                withContext(Dispatchers.Main) {
                    if (medicos.isNotEmpty()) {
                        val adapter = ArrayAdapter(
                            this@SolicitarVisitaActivity,
                            android.R.layout.simple_spinner_item,
                            medicos.map { it.usuario.nombre }
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerMedicos.adapter = adapter
                        habilitarBotonSiDatosCargados()
                    } else {
                        Toast.makeText(
                            this@SolicitarVisitaActivity,
                            "No hay médicos disponibles",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SolicitarVisitaActivity,
                        "Error al cargar médicos: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("MEDICOS", "Error cargar médicos", e)
                }
            }
        }
    }

    private fun habilitarBotonSiDatosCargados() {
        btnSolicitar.isEnabled = barrios.isNotEmpty() && medicos.isNotEmpty()
    }

    private fun guardarVisita(visita: Visita) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.visitaApi.crearVisita(visita)
                withContext(Dispatchers.Main) {
                    Log.d("VISITA", "Response: ${response.code()} - ${response.message()}")
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@SolicitarVisitaActivity,
                            "Visita registrada con éxito",
                            Toast.LENGTH_LONG
                        ).show()
                        limpiarCampos()
                    } else {
                        Toast.makeText(
                            this@SolicitarVisitaActivity,
                            "Error al registrar visita",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SolicitarVisitaActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("VISITA", "Error guardar visita", e)
                }
            }
        }
    }

    private fun limpiarCampos() {
        editFecha.text.clear()
        editDescripcion.text.clear()
        editDireccion.text.clear()
        editTelefono.text.clear()
        spinnerBarrios.setSelection(0)
        spinnerMedicos.setSelection(0)
        checkMedico.isChecked = false
    }
}
