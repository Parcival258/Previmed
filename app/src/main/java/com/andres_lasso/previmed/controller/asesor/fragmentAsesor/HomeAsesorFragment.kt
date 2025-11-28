package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.FragmentHomeAsesorBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeAsesorFragment : Fragment() {

    private var _binding: FragmentHomeAsesorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeAsesorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("HomeAsesor", "===== Iniciando HomeAsesorFragment =====")

        // ============================
        // 👉 Mostrar nombre del asesor
        // ============================
        val nombreAsesor = PreferenceHelper.getNombreUsuario(requireContext())

        Log.d("HomeAsesor", "Nombre asesor obtenido de preferencias: $nombreAsesor")

        if (!nombreAsesor.isNullOrEmpty()) {
            binding.txtNombre.text = nombreAsesor
            Log.d("HomeAsesor", "Nombre asignado al TextView correctamente.")
        } else {
            Log.e("HomeAsesor", "❌ El nombre del asesor viene vacío o NULL")
        }

        binding.btnPendientes.setOnClickListener {
            Log.d("HomeAsesor", "Botón Ver Pagos Pendientes presionado.")
            parentFragmentManager.beginTransaction()
                .replace(R.id.asesor_nav, PagosAsesorFragment())
                .addToBackStack(null)
                .commit()
        }

        cargarPagosPendientes()
    }

    private fun cargarPagosPendientes() {
        Log.d("HomeAsesor", "Iniciando carga de pagos pendientes...")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.pagoApi.obtenerPagos()
                Log.d("HomeAsesor", "Respuesta API pagos: $response")

                val listaPagos = response.data ?: emptyList()

                Log.d("HomeAsesor", "Total pagos recibidos: ${listaPagos.size}")

                val pendientes = listaPagos.count { it.estado == "Pendiente" }
                val totalPendiente = listaPagos
                    .filter { it.estado == "Pendiente" }
                    .sumOf { pago ->
                        val monto = pago.monto?.toDoubleOrNull() ?: 0.0
                        Log.d("HomeAsesor", "Sumando pago pendiente: $monto")
                        monto
                    }

                withContext(Dispatchers.Main) {
                    Log.d("HomeAsesor", "Pagos pendientes: $pendientes")
                    Log.d("HomeAsesor", "Total pendiente: $totalPendiente")

                    binding.txtPagosPendientes.text = "$pendientes pagos"
                    binding.txtTotalPendiente.text = "$${"%,.2f".format(totalPendiente)}"
                }

            } catch (e: Exception) {
                Log.e("HomeAsesor", "❌ Error al cargar pagos pendientes", e)

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Error al cargar pagos pendientes",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("HomeAsesor", "HomeAsesorFragment destruido.")
        _binding = null
    }
}
