package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.content.Intent
import android.os.Bundle
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
import com.andres_lasso.previmed.view.pagos.PagosAdd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PagosAsesorFragment : Fragment() {

    private var _binding: FragmentPagosAsesorBinding? = null
    private val binding get() = _binding!!

    private lateinit var pagosAdapter: PagosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            val intent = Intent(requireContext(), PagosAdd::class.java)
            startActivity(intent)
        }

        cargarPagos()
    }

    private fun cargarPagos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("PAGOS_API", "➡ Realizando solicitud a obtenerPagos()...")

                val response = RetrofitClient.pagoApi.obtenerPagos()

                val listaPagos = response.data

                withContext(Dispatchers.Main) {
                    pagosAdapter.setData(listaPagos ?: emptyList())
                }

            } catch (e: Exception) {
                Log.e("PAGOS_API", "❌ Error al obtener pagos: ${e.message}", e)
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
