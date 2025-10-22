package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.Login
import com.andres_lasso.previmed.controller.asesor.Barrios
import com.andres_lasso.previmed.controller.asesor.PlanesView
import com.andres_lasso.previmed.controller.asesor.fragmentAsesor.BuscarContratoAsesor
import com.andres_lasso.previmed.databinding.FragmentHomeAsesorBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.Plan
import com.andres_lasso.previmed.model.PlanesResponse
import com.andres_lasso.previmed.utils.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeAsesorFragment : Fragment() {
    private var _binding: FragmentHomeAsesorBinding? = null
    private val binding get() = _binding!!

    // Lista local para guardar planes cargados
    private val listaPlanes = mutableListOf<Plan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeAsesorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar planes desde backend al iniciar vista
        cargarPlanes()

        // 🔹 Ir a planes
        binding.btnPlanes.setOnClickListener {
            val ir_planes = Intent(requireContext(), PlanesView::class.java)
            startActivity(ir_planes)
        }

        // 🔹 Ir a barrios
        binding.btnBarrios.setOnClickListener {
            val ir_barrios = Intent(requireContext(), Barrios::class.java)
            startActivity(ir_barrios)
        }

        // 🔹 Ir a contratos
        binding.btnContratos.setOnClickListener {
            val ir_contratos = Intent(requireContext(), BuscarContratoAsesor::class.java)
            startActivity(ir_contratos)
        }

        // 🔹 Cerrar sesión
        binding.btnLogout.setOnClickListener {
            PreferenceHelper.clearSession(requireContext())

            val intent = Intent(requireContext(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            requireActivity().finish()
        }
    }

    private fun cargarPlanes() {
        RetrofitClient.planesApi.getPlanes().enqueue(object : Callback<PlanesResponse> {
            override fun onResponse(call: Call<PlanesResponse>, response: Response<PlanesResponse>) {
                if (response.isSuccessful) {
                    val planes = response.body()?.planes?.filter { it.estado } ?: listOf()
                    listaPlanes.clear()
                    listaPlanes.addAll(planes)
                    // Aquí puedes actualizar UI si quieres mostrar resumen en home
                } else {
                    Toast.makeText(requireContext(), "Error al cargar planes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlanesResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
