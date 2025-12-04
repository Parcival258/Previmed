package com.andres_lasso.previmed.view.pagos

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.ApiResponse
import com.andres_lasso.previmed.model.FormaPago
import com.andres_lasso.previmed.model.PacienteData
import com.andres_lasso.previmed.model.PagoRequest
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class PagosAdd : AppCompatActivity() {

    private val tag = "PagosAdd"

    private lateinit var etBuscarTitular: AutoCompleteTextView
    private lateinit var btnSubirFoto: Button
    private lateinit var btnTomarFoto: Button
    private lateinit var ivPreview: ImageView
    private lateinit var etFechaInicio: EditText
    private lateinit var etFechaFin: EditText
    private lateinit var etFechaPago: EditText
    private lateinit var spinnerFormaPago: Spinner
    private lateinit var etNumeroRecibo: EditText
    private lateinit var etMonto: EditText
    private lateinit var btnGuardar: Button

    private var fotoUri: Uri? = null
    private var listaTitulares: List<PacienteData> = emptyList()
    private var formaPagoList: List<FormaPago> = emptyList()
    private var selectedTitularId: Int? = null
    private var selectedMembresiaId: Int? = null
    private var selectedFormaPagoId: Int? = null

    // ===== Permisos =====
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
            val storageGranted =
                permissions[Manifest.permission.READ_MEDIA_IMAGES]
                    ?: permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                    ?: false
            if (!cameraGranted || !storageGranted) {
                Toast.makeText(this, "Necesitas conceder permisos.", Toast.LENGTH_LONG).show()
            }
        }

    // Galería
    private val seleccionarImagen =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                fotoUri = it
                ivPreview.setImageURI(it)
            }
        }

    // Cámara
    private val tomarFoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap ?: return@registerForActivityResult
                ivPreview.setImageBitmap(bitmap)

                val file = File(cacheDir, "foto_capturada.jpg")
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                file.writeBytes(stream.toByteArray())
                fotoUri = Uri.fromFile(file)
            }
        }

    // ===== Ciclo de vida =====
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_add)

        initViews()
        pedirPermisos()
        cargarFormasPago()
        cargarTitulares()

        etFechaInicio.setOnClickListener { showDatePicker(etFechaInicio) }
        etFechaFin.setOnClickListener { showDatePicker(etFechaFin) }
        etFechaPago.setOnClickListener { showDatePicker(etFechaPago) }

        btnSubirFoto.setOnClickListener {
            if (verificarPermisoLectura()) seleccionarImagen.launch("image/*")
        }

        btnTomarFoto.setOnClickListener {
            if (verificarPermisoCamara()) abrirCamara()
        }

        btnGuardar.setOnClickListener {
            guardarPago()
        }
    }

    // ===== Inicialización =====
    private fun initViews() {
        etBuscarTitular = findViewById(R.id.etBuscarTitular)
        btnSubirFoto = findViewById(R.id.btnSubirFoto)
        btnTomarFoto = findViewById(R.id.btnTomarFoto)
        ivPreview = findViewById(R.id.ivPreview)
        etFechaInicio = findViewById(R.id.etFechaInicio)
        etFechaFin = findViewById(R.id.etFechaFin)
        etFechaPago = findViewById(R.id.etFechaPago)
        spinnerFormaPago = findViewById(R.id.spinnerFormaPago)
        etNumeroRecibo = findViewById(R.id.etNumeroRecibo)
        etMonto = findViewById(R.id.etMonto)
        btnGuardar = findViewById(R.id.btnGuardar)

        // 👇 Muy importante para que autocomplete funcione “rápido”
        etBuscarTitular.threshold = 1

        // Mostrar dropdown al hacer foco
        etBuscarTitular.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && listaTitulares.isNotEmpty()) {
                (v as AutoCompleteTextView).showDropDown()
            }
        }

        // Y también al hacer click en el campo
        etBuscarTitular.setOnClickListener {
            if (listaTitulares.isNotEmpty()) {
                etBuscarTitular.showDropDown()
            }
        }
    }

    private fun pedirPermisos() {
        val permisos = mutableListOf<String>()
        permisos.add(Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT >= 33) {
            permisos.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permisos.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        requestPermissionsLauncher.launch(permisos.toTypedArray())
    }

    private fun verificarPermisoCamara(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun verificarPermisoLectura(): Boolean {
        val permiso =
            if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_IMAGES
            else Manifest.permission.READ_EXTERNAL_STORAGE
        return ContextCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED
    }

    private fun abrirCamara() {
        tomarFoto.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    // ===== Datos de backend =====

    /** Carga TODOS los pacientes; validamos membresía al guardar. */
    private fun cargarTitulares() {
        RetrofitClient.pacienteApi.getTitulares()
            .enqueue(object : retrofit2.Callback<ApiResponse<List<PacienteData>>> {
                override fun onResponse(
                    call: retrofit2.Call<ApiResponse<List<PacienteData>>>,
                    response: retrofit2.Response<ApiResponse<List<PacienteData>>>
                ) {
                    if (response.isSuccessful) {
                        val titulares = response.body()?.data ?: emptyList()

                        // 🔹 Filtrar solo los que tengan membresías activas
                        listaTitulares = titulares.filter { titular ->
                            !titular.membresiaPaciente.isNullOrEmpty()
                        }

                        Log.d(tag, "Titulares recibidos: ${titulares.size}, con membresía: ${listaTitulares.size}")

                        if (listaTitulares.isEmpty()) {
                            Toast.makeText(
                                this@PagosAdd,
                                "No hay titulares con membresía activa.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        configurarAutoComplete()
                    } else {
                        Log.e(tag, "Error al cargar titulares: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ApiResponse<List<PacienteData>>>,
                    t: Throwable
                ) {
                    Log.e(tag, "Fallo al cargar titulares: ${t.message}")
                }
            })
    }





    private fun configurarAutoComplete() {
        if (listaTitulares.isEmpty()) return

        // 🔹 Armamos el texto que verá el usuario en el dropdown
        val listaStrings = listaTitulares.map { titular ->
            val nombre = titular.usuario?.nombre ?: "Sin nombre"
            val documento = titular.usuario?.numeroDocumento ?: "Sin doc"

            // Tomamos la primera membresía asociada al titular
            val membresiaId = titular.membresiaPaciente
                ?.firstOrNull()
                ?.membresiaId

            val membresiaTexto = membresiaId?.let { "Membresía $it" } ?: "Sin membresía"

            "$nombre - $documento - $membresiaTexto"
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            listaStrings
        )

        etBuscarTitular.setAdapter(adapter)
        etBuscarTitular.threshold = 1

        etBuscarTitular.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && listaTitulares.isNotEmpty()) {
                (v as AutoCompleteTextView).showDropDown()
            }
        }

        etBuscarTitular.setOnClickListener {
            if (listaTitulares.isNotEmpty()) {
                etBuscarTitular.showDropDown()
            }
        }

        // 🔹 Cuando seleccionan un item, usamos el "position" para
        // obtener el mismo titular de la lista
        etBuscarTitular.setOnItemClickListener { _, _, position, _ ->
            val titular = listaTitulares[position]

            // Guardamos el id del titular seleccionado
            selectedTitularId = titular.idPaciente

            // Sacamos la primera relación de membresía y su id
            val relacionMembresia = titular.membresiaPaciente?.firstOrNull()
            selectedMembresiaId = relacionMembresia?.membresiaId

            Log.d(
                tag,
                "✅ Titular seleccionado -> idPaciente=${titular.idPaciente}, " +
                        "pacienteId=${titular.pacienteId}, membresiaId=${relacionMembresia?.membresiaId}"
            )
        }
    }




    private fun cargarFormasPago() {
        RetrofitClient.formaPagoApi.getFormasPago()
            .enqueue(object : retrofit2.Callback<List<FormaPago>> {
                override fun onResponse(
                    call: retrofit2.Call<List<FormaPago>>,
                    response: retrofit2.Response<List<FormaPago>>
                ) {
                    if (response.isSuccessful) {
                        formaPagoList = response.body()?.filter { it.estado } ?: emptyList()
                        spinnerFormaPago.adapter = ArrayAdapter(
                            this@PagosAdd,
                            android.R.layout.simple_spinner_item,
                            formaPagoList.map { it.tipoPago ?: "Sin nombre" }
                        )
                        spinnerFormaPago.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: android.view.View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    selectedFormaPagoId = formaPagoList[position].idFormaPago
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                    } else {
                        Log.e(tag, "Error cargando formas de pago: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<FormaPago>>, t: Throwable) {
                    Log.e(tag, "Error cargando formas de pago: ${t.message}")
                }
            })
    }

    // ===== UI helpers =====

    private fun showDatePicker(edt: EditText) {
        val c = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                edt.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day))
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // ===== Guardar pago =====

    private fun guardarPago() {
        val monto = etMonto.text.toString()
        val fechaInicio = etFechaInicio.text.toString()
        val fechaFin = etFechaFin.text.toString()
        val fechaPago = etFechaPago.text.toString()
        val numeroRecibo = etNumeroRecibo.text.toString().ifBlank { "" }
        val cobradorId = PreferenceHelper.getIdAsesor(this) ?: ""

        if (selectedTitularId == null) {
            Toast.makeText(this, "Seleccione un titular", Toast.LENGTH_LONG).show()
            return
        }
        if (selectedFormaPagoId == null) {
            Toast.makeText(this, "Seleccione una forma de pago", Toast.LENGTH_LONG).show()
            return
        }
        if (monto.isBlank()) {
            Toast.makeText(this, "Ingrese un monto", Toast.LENGTH_LONG).show()
            return
        }

        // Validamos membresía aquí (no en el filtro)
        if (selectedMembresiaId == null) {
            Toast.makeText(
                this,
                "El titular seleccionado no tiene una membresía asociada",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        Log.d(
            tag,
            "🧾 Enviando -> monto=$monto, membresiaId=$selectedMembresiaId, formaPagoId=$selectedFormaPagoId, cobradorId=$cobradorId"
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.pagoApi
                val response = if (fotoUri != null) {
                    api.createPagoConFoto(
                        monto.toRequest(),
                        fechaInicio.toRequestOrEmpty(),
                        fechaFin.toRequestOrEmpty(),
                        fechaPago.toRequest(),
                        selectedMembresiaId!!.toRequest(),
                        selectedFormaPagoId!!.toRequest(),
                        numeroRecibo.toRequest(),
                        cobradorId.toRequest(),
                        "Pendiente".toRequest(),
                        prepareFilePart("foto", fotoUri!!)
                    )
                } else {
                    api.createPago(
                        PagoRequest(
                            monto = monto.toDouble(),
                            fecha_inicio = fechaInicio.ifBlank { null },
                            fecha_fin = fechaFin.ifBlank { null },
                            fecha_pago = fechaPago,
                            membresia_id = selectedMembresiaId!!,
                            forma_pago_id = selectedFormaPagoId!!,
                            numero_recibo = numeroRecibo,
                            cobrador_id = cobradorId,
                            estado = "Pendiente",
                            foto = null
                        )
                    )
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@PagosAdd,
                            "Pago registrado correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@PagosAdd,
                            "Error: ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PagosAdd, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e(tag, "Error guardando pago", e)
                }
            }
        }
    }

    // ===== Helpers Multipart =====

    private fun String.toRequest(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())

    private fun String?.toRequestOrEmpty(): RequestBody =
        (this ?: "").toRequest()

    private fun Int.toRequest(): RequestBody =
        this.toString().toRequest()

    private fun prepareFilePart(partName: String, uri: Uri): MultipartBody.Part {
        val file = File(cacheDir, "upload_image.jpg")
        contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }
}
