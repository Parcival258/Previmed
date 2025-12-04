package com.andres_lasso.previmed.view.pagos

import PagoRequest
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

    private val TAG = "PagosAdd"

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
    private var selectedFormaPagoId: Int? = null

    // ========================
    // PERMISOS
    // ========================
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            Log.d(TAG, "Permisos recibidos: $permissions")
            val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
            val storageGranted =
                permissions[Manifest.permission.READ_MEDIA_IMAGES]
                    ?: permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                    ?: false

            if (!cameraGranted || !storageGranted) {
                Log.e(TAG, "Permisos denegados")
                Toast.makeText(this, "Necesitas conceder permisos.", Toast.LENGTH_LONG).show()
            } else {
                Log.d(TAG, "Permisos otorgados correctamente")
            }
        }

    // ========================
    // Seleccionar imagen
    // ========================
    private val seleccionarImagen =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            Log.d(TAG, "Imagen seleccionada: $uri")
            uri?.let {
                fotoUri = it
                ivPreview.setImageURI(it)
            }
        }

    // ========================
    // Tomar foto con cámara
    // ========================
    private val tomarFoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "Resultado cámara: ${result.resultCode}")

            if (result.resultCode == RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                if (bitmap == null) {
                    Log.e(TAG, "Bitmap nulo desde cámara")
                    return@registerForActivityResult
                }

                ivPreview.setImageBitmap(bitmap)

                val file = File(cacheDir, "foto_capturada.jpg")
                val stream = ByteArrayOutputStream()

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                file.writeBytes(stream.toByteArray())

                fotoUri = Uri.fromFile(file)

                Log.d(TAG, "Foto guardada en cache: ${fotoUri}")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_add)

        Log.d(TAG, "Iniciando PagosAdd")

        initViews()
        pedirPermisos()
        cargarFormasPago()
        cargarTitulares()

        etFechaInicio.setOnClickListener { showDatePicker(etFechaInicio) }
        etFechaFin.setOnClickListener { showDatePicker(etFechaFin) }
        etFechaPago.setOnClickListener { showDatePicker(etFechaPago) }

        btnSubirFoto.setOnClickListener {
            Log.d(TAG, "Click subir foto desde galería")
            if (verificarPermisoLectura()) seleccionarImagen.launch("image/*")
        }

        btnTomarFoto.setOnClickListener {
            Log.d(TAG, "Click tomar foto")
            if (verificarPermisoCamara()) abrirCamara()
        }

        btnGuardar.setOnClickListener {
            Log.d(TAG, "Click guardar pago")
            guardarPago()
        }
    }

    // ========================
    // PERMISOS
    // ========================

    private fun pedirPermisos() {
        Log.d(TAG, "Solicitando permisos...")
        val permisos = mutableListOf<String>()

        permisos.add(Manifest.permission.CAMERA)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permisos.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permisos.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        requestPermissionsLauncher.launch(permisos.toTypedArray())
    }

    private fun verificarPermisoCamara(): Boolean {
        val granted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        Log.d(TAG, "Permiso cámara: $granted")
        return granted == PackageManager.PERMISSION_GRANTED
    }

    private fun verificarPermisoLectura(): Boolean {
        val permiso =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else
                Manifest.permission.READ_EXTERNAL_STORAGE

        val granted = ContextCompat.checkSelfPermission(this, permiso)
        Log.d(TAG, "Permiso lectura: $granted")
        return granted == PackageManager.PERMISSION_GRANTED
    }

    private fun abrirCamara() {
        Log.d(TAG, "Abriendo cámara...")
        tomarFoto.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    // ========================
    // VIEWS
    // ========================
    private fun initViews() {
        Log.d(TAG, "Inicializando vistas...")

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
    }

    // ========================
    // AUTO COMPLETE TITULARES
    // ========================
    private fun configurarAutoComplete() {
        Log.d(TAG, "Configurando autocomplete con ${listaTitulares.size} titulares")

        val listaStrings = listaTitulares.map {
            "${it.usuario?.nombre} - ${it.usuario?.numeroDocumento}"
        }

        etBuscarTitular.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, listaStrings)
        )

        etBuscarTitular.setOnItemClickListener { _, _, position, _ ->
            selectedTitularId = listaTitulares[position].idPaciente
            Log.d(TAG, "Titular seleccionado: $selectedTitularId")
        }
    }

    private fun cargarTitulares() {
        Log.d(TAG, "Cargando titulares...")

        RetrofitClient.pacienteApi.getPacientes()
            .enqueue(object : retrofit2.Callback<ApiResponse<List<PacienteData>>> {
                override fun onResponse(
                    call: retrofit2.Call<ApiResponse<List<PacienteData>>>,
                    response: retrofit2.Response<ApiResponse<List<PacienteData>>>
                ) {
                    Log.d(TAG, "Respuesta titulares: ${response.code()}")

                    if (response.isSuccessful) {
                        listaTitulares = response.body()?.data ?: emptyList()
                        Log.d(TAG, "Titulares cargados: ${listaTitulares.size}")
                        configurarAutoComplete()
                    } else {
                        Log.e(TAG, "Error cargando titulares: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: retrofit2.Call<ApiResponse<List<PacienteData>>>, t: Throwable) {
                    Log.e(TAG, "Fallo en cargar titulares", t)
                }
            })
    }

    // ========================
    // FORMAS DE PAGO
    // ========================
    private fun cargarFormasPago() {
        Log.d(TAG, "Cargando formas de pago...")

        RetrofitClient.formaPagoApi.getFormasPago()
            .enqueue(object : retrofit2.Callback<List<FormaPago>> {
                override fun onResponse(
                    call: retrofit2.Call<List<FormaPago>>,
                    response: retrofit2.Response<List<FormaPago>>
                ) {
                    Log.d(TAG, "Respuesta formas de pago: ${response.code()}")

                    if (response.isSuccessful) {
                        formaPagoList = response.body()?.filter { it.estado } ?: emptyList()

                        Log.d(TAG, "Formas de pago cargadas: ${formaPagoList.size}")

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
                                    Log.d(TAG, "Forma pago seleccionada: $selectedFormaPagoId")
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                    } else {
                        Log.e(TAG, "Error formas de pago: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<FormaPago>>, t: Throwable) {
                    Log.e(TAG, "Fallo cargando formas de pago", t)
                }
            })
    }

    // ========================
    // DATE PICKER
    // ========================
    private fun showDatePicker(edt: EditText) {
        val c = Calendar.getInstance()
        Log.d(TAG, "Mostrando DatePicker")

        DatePickerDialog(
            this,
            { _, year, month, day ->
                val fecha = String.format("%04d-%02d-%02d", year, month + 1, day)
                edt.setText(fecha)
                Log.d(TAG, "Fecha seleccionada: $fecha")
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // ========================
    // GUARDAR PAGO
    // ========================
    private fun guardarPago() {
        Log.d(TAG, "Iniciando proceso de guardar pago...")

        val monto = etMonto.text.toString()
        val fechaInicio = etFechaInicio.text.toString()
        val fechaFin = etFechaFin.text.toString()
        val fechaPago = etFechaPago.text.toString()
        val numeroRecibo = etNumeroRecibo.text.toString().ifBlank { null }
        val cobradorId = PreferenceHelper.getIdAsesor(this)

        Log.d(TAG, "Datos ingresados -> monto:$monto fechaInicio:$fechaInicio fechaFin:$fechaFin fechaPago:$fechaPago recibo:$numeroRecibo titular:$selectedTitularId formaPago:$selectedFormaPagoId cobrador:$cobradorId fotoUri:$fotoUri")

        if (selectedTitularId == null) {
            Log.e(TAG, "Titular no seleccionado")
            Toast.makeText(this, "Seleccione un titular", Toast.LENGTH_LONG).show()
            return
        }

        if (selectedFormaPagoId == null) {
            Log.e(TAG, "Forma de pago no seleccionada")
            Toast.makeText(this, "Seleccione una forma de pago", Toast.LENGTH_LONG).show()
            return
        }

        if (monto.isBlank()) {
            Log.e(TAG, "Monto vacío")
            Toast.makeText(this, "Ingrese un monto", Toast.LENGTH_LONG).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.pagoApi
                Log.d(TAG, "Preparando solicitud API...")

                val response = if (fotoUri != null) {
                    Log.d(TAG, "Enviando pago con foto...")

                    api.createPagoConFoto(
                        monto.toRequest(),
                        fechaInicio.toRequest(),
                        fechaFin.toRequest(),
                        fechaPago.toRequest(),
                        selectedTitularId.toString().toRequest(),
                        selectedFormaPagoId.toString().toRequest(),
                        numeroRecibo.toRequestOrNull(),
                        cobradorId!!.toRequest(),
                        "Pendiente".toRequest(),
                        prepareFilePart("foto", fotoUri!!)
                    )
                } else {
                    Log.d(TAG, "Enviando pago SIN foto...")

                    api.createPago(
                        PagoRequest(
                            monto = monto.toDouble(),
                            fecha_inicio = fechaInicio,
                            fecha_fin = fechaFin,
                            fecha_pago = fechaPago,
                            membresia_id = selectedTitularId!!,
                            forma_pago_id = selectedFormaPagoId!!,
                            numero_recibo = numeroRecibo,
                            cobrador_id = cobradorId!!,
                            estado = "Pendiente",
                            foto = null
                        )
                    )
                }

                Log.d(TAG, "Respuesta API recibida -> código: ${response.code()}")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.i(TAG, "Pago registrado exitosamente")
                        Toast.makeText(this@PagosAdd, "Pago registrado ✔", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e(TAG, "Error en API: $errorBody")
                        Toast.makeText(
                            this@PagosAdd,
                            "Error: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "Excepción durante guardarPago", e)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PagosAdd, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun String.toRequest(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())

    private fun String?.toRequestOrNull(): RequestBody? =
        if (this.isNullOrBlank()) null else this.toRequest()

    private fun prepareFilePart(partName: String, uri: Uri): MultipartBody.Part {
        Log.d(TAG, "Preparando archivo para enviar: $uri")

        val file = File(cacheDir, "upload_image.jpg")

        try {
            contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error leyendo archivo desde uri", e)
        }

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        Log.d(TAG, "Archivo preparado: ${file.absolutePath} (${file.length()} bytes)")

        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }
}
