package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.andres_lasso.previmed.controller.asesor.recycler.PagosAdapter
import com.andres_lasso.previmed.databinding.FragmentPagosAsesorBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.PagoModel
import com.andres_lasso.previmed.view.pagos.PagosAdd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PagosAsesorFragment : Fragment() {

    private var _binding: FragmentPagosAsesorBinding? = null
    private val binding get() = _binding!!

    private lateinit var pagosAdapter: PagosAdapter

    private var listaPagosCompleta = listOf<PagoModel>()   // << Para filtrar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPagosAsesorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagosAdapter = PagosAdapter()

        binding.recyclerPagosAsesor.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagosAdapter
        }

        binding.btnIrPagosAdd.setOnClickListener {
            startActivity(Intent(requireContext(), PagosAdd::class.java))
        }

        configurarBuscador()
        cargarPagos()
    }

    private fun configurarBuscador() {
        binding.etBuscarPago.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarPagos(text.toString())
            }
        })
    }

    private fun filtrarPagos(query: String) {
        if (query.isEmpty()) {
            pagosAdapter.setData(listaPagosCompleta)
            return
        }

        val filtrados = listaPagosCompleta.filter { pago ->

            val titular = listOfNotNull(
                pago.membresia?.membresiaPaciente?.firstOrNull()?.paciente?.usuario?.nombre,
                pago.membresia?.membresiaPaciente?.firstOrNull()?.paciente?.usuario?.apellido
            ).joinToString(" ")

            val recibo = pago.numeroRecibo ?: ""

            titular.contains(query, ignoreCase = true) ||
                    recibo.contains(query, ignoreCase = true)
        }

        pagosAdapter.setData(filtrados)
    }

    private fun cargarPagos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.pagoApi.obtenerPagos()
                val listaPagos = response.data ?: emptyList()

                listaPagosCompleta = listaPagos

                withContext(Dispatchers.Main) {
                    pagosAdapter.setData(listaPagosCompleta)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error al cargar pagos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
