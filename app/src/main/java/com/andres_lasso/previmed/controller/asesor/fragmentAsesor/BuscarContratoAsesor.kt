package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.ContratoDetalleDialog
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.MembresiaAdapter
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.Membresia
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscarContratoAsesor : AppCompatActivity() {

    private var listaOriginal: List<Membresia> = listOf()
    private lateinit var adapter: MembresiaAdapter

    private val handler = Handler(Looper.getMainLooper())
    private val pollingInterval = 5000L

    private val pollingRunnable = object : Runnable {
        override fun run() {
            cargarMembresias()
            handler.postDelayed(this, pollingInterval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Configuración de la barra de estado (igual que PlanesView)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        setContentView(R.layout.activity_buscar_contrato_asesor)

        // Evitar que la barra de estado tape contenido
        val root = findViewById<android.view.View>(R.id.rootLayoutBuscarContrato)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, status.top, 0, 0)
            insets
        }

        // RecyclerView
        val recycler = findViewById<RecyclerView>(R.id.recycler_personas)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = MembresiaAdapter(emptyList()) { membresia ->
            val dialog = ContratoDetalleDialog(membresia)
            dialog.show(supportFragmentManager, "DetalleContrato")
        }
        recycler.adapter = adapter

        // 🔍 Búsqueda
        val etBuscar = findViewById<EditText>(R.id.etBuscarContrato)
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarLista(text.toString())
            }
        })

        // Cargar datos
        cargarMembresias()
    }

    override fun onResume() {
        super.onResume()
        handler.post(pollingRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(pollingRunnable)
    }

    private fun cargarMembresias() {
        RetrofitClient.membresiaApi.listarMembresias()
            .enqueue(object : Callback<List<Membresia>> {
                override fun onResponse(
                    call: Call<List<Membresia>>,
                    response: Response<List<Membresia>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        listaOriginal = response.body()!!
                        adapter.actualizarLista(listaOriginal)
                    } else {
                        Toast.makeText(this@BuscarContratoAsesor, "Error al cargar contratos", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Membresia>>, t: Throwable) {
                    Toast.makeText(this@BuscarContratoAsesor, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun filtrarLista(filtro: String) {
        val f = filtro.lowercase().trim()

        val filtrada = listaOriginal.filter { membresia ->

            val nombres = membresia.membresiaPaciente?.joinToString(" ") { mp ->
                val u = mp.paciente?.usuario
                listOfNotNull(
                    u?.nombre,
                    u?.segundoNombre,
                    u?.apellido,
                    u?.segundoApellido
                ).joinToString(" ")
            }?.lowercase().orEmpty()

            val documentos = membresia.membresiaPaciente?.joinToString(" ") { mp ->
                mp.paciente?.usuario?.numeroDocumento ?: ""
            }?.lowercase().orEmpty()

            val numContrato = membresia.numeroContrato?.lowercase().orEmpty()
            val formaPago = membresia.formaPago?.lowercase().orEmpty()

            f in nombres || f in documentos || f in numContrato || f in formaPago
        }

        adapter.actualizarLista(filtrada)
    }
}
