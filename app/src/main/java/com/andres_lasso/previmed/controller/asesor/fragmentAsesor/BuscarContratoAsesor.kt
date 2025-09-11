package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.asesor.ContratoDetalleDialog
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.MembresiaAdapter
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.Membresia
import kotlinx.coroutines.launch

class BuscarContratoAsesor : AppCompatActivity() {
    private var listaOriginal: List<Membresia> = listOf()
    private lateinit var adapter: MembresiaAdapter
    private var paginaActual = 1
    private val tamañoPagina = 20
    private var cargando = false
    private var finLista = false

    private val handler = Handler(Looper.getMainLooper())
    private val pollingInterval = 5000L // 5 segundos

    // Polling - refresca la lista periódicamente
    private val pollingRunnable = object : Runnable {
        override fun run() {
            paginaActual = 1
            finLista = false
            listaOriginal = listOf()
            cargarMembresiasPaginadas(reset = true)
            handler.postDelayed(this, pollingInterval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_contrato_asesor)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_personas)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = MembresiaAdapter(emptyList()) { membresia ->
            val dialog = ContratoDetalleDialog(membresia)
            dialog.show(supportFragmentManager, "DetalleContrato")
        }
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val total = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                if (!cargando && !finLista && lastVisible + 5 >= total) {
                    cargarMembresiasPaginadas()
                }
            }
        })

        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtro = newText?.lowercase()?.trim() ?: ""
                val resultado = listaOriginal.filter { membresia ->
                    val nombres = membresia.membresiaPaciente?.mapNotNull { mp ->
                        val u = mp.paciente?.usuario
                        listOf(u?.nombre, u?.segundoNombre, u?.apellido, u?.segundoApellido)
                            .filterNotNull().joinToString(" ")
                    }?.joinToString(" ")?.lowercase() ?: ""
                    val documentos = membresia.membresiaPaciente?.mapNotNull { mp ->
                        mp.paciente?.usuario?.numeroDocumento
                    }?.joinToString(" ")?.lowercase() ?: ""
                    val nc = membresia.numeroContrato?.lowercase() ?: ""
                    val fp = membresia.formaPago?.lowercase() ?: ""

                    nombres.contains(filtro) || documentos.contains(filtro) ||
                            nc.contains(filtro) || fp.contains(filtro)
                }
                adapter.actualizarLista(resultado)
                return true
            }
        })

        cargarMembresiasPaginadas()
    }

    override fun onResume() {
        super.onResume()
        handler.post(pollingRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(pollingRunnable)
    }

    // reset=true para reiniciar la lista (usado en polling)
    private fun cargarMembresiasPaginadas(reset: Boolean = false) {
        if (cargando || finLista) return
        cargando = true

        lifecycleScope.launch {
            try {
                val membresias = RetrofitClient.contratosApi.listarMembresias(paginaActual, tamañoPagina)
                Log.d("BuscarContratoAsesor", "Página: $paginaActual, recibidas: ${membresias.size}")

                if (membresias.isEmpty()) {
                    finLista = true
                } else {
                    listaOriginal = if (paginaActual == 1 || reset) {
                        membresias
                    } else {
                        listaOriginal + membresias.filter { nueva ->
                            listaOriginal.none { it.idMembresia == nueva.idMembresia }
                        }
                    }
                    adapter.actualizarLista(listaOriginal)
                    paginaActual++
                }
            } catch (e: Exception) {
                Toast.makeText(this@BuscarContratoAsesor, "Error al cargar datos: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                Log.e("BuscarContratoAsesor", "Error al cargar membresías", e)
            } finally {
                cargando = false
            }
        }
    }
}
