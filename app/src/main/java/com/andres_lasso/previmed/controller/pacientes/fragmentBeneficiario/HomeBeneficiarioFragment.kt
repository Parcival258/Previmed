package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.Login
import com.andres_lasso.previmed.controller.pacientes.Beneficiarios
import com.andres_lasso.previmed.databinding.FragmentHomeBeneficiarioBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeBeneficiarioFragment : Fragment() {

    private var _binding: FragmentHomeBeneficiarioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBeneficiarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSaludo()
        cargarDatos()
        setupClickListeners()
    }

    private fun setSaludo() {
        val saludo = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Buenos días"
            in 12..17 -> "Buenas tardes"
            else -> "Buenas noches"
        }
        binding.saludoBeneficiario.text = "¡$saludo!"
    }

    private fun cargarDatos() {
        val usuarioId = PreferenceHelper.getUsuarioId(requireContext())
        val idPaciente = PreferenceHelper.getIdPaciente(requireContext())

        if (usuarioId.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Error: Usuario no identificado", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Obtener membresía por UUID
                val membresiaResponse = RetrofitClient.pagosApi.getMembresiaByUserId(usuarioId)

                if (membresiaResponse.isSuccessful && membresiaResponse.body() != null) {
                    val membresia = membresiaResponse.body()!!

                    // Estado del servicio
                    if (membresia.membresia.estado) {
                        binding.estadoServicio.text = "Servicio Activo"
                        binding.indicadorEstado.setBackgroundResource(R.drawable.circle_green)
                    } else {
                        binding.estadoServicio.text = "Servicio Inactivo"
                        binding.indicadorEstado.setBackgroundResource(R.drawable.circle_red)
                    }

                    // Plan
                    binding.tvPlan.text = membresia.membresia.plan.tipoPlan

                    // Visitas
                    cargarVisitas(membresia.pacienteId)
                }
            } catch (e: Exception) {
                binding.estadoServicio.text = "Error al cargar datos"
            }
        }
    }

    private fun cargarVisitas(pacienteId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val visitaResponse = RetrofitClient.visitasApi.getVisitas(pacienteId)

                if (visitaResponse.isSuccessful && visitaResponse.body() != null) {
                    val visitaList = visitaResponse.body()!!.msj

                    if (!visitaList.isNullOrEmpty()) {
                        val visita = visitaList.first()
                        val fechaFormato = formatearFecha(visita.fechaVisita ?: "")
                        binding.tvVisitas.text = "📅 $fechaFormato"
                    } else {
                        binding.tvVisitas.text = "Sin visitas agendadas"
                    }
                } else {
                    binding.tvVisitas.text = "Sin visitas agendadas"
                }
            } catch (e: Exception) {
                android.util.Log.e("HOME_VISITAS", "Error: ${e.message}", e)
                binding.tvVisitas.text = "Sin visitas agendadas"
            }
        }
    }

    private fun formatearFecha(fechaISO: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(fechaISO)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            fechaISO
        }
    }

    private fun setupClickListeners() {
        binding.irBeneficiarios.setOnClickListener {
            startActivity(Intent(requireContext(), Beneficiarios::class.java))
        }

        binding.btnLogout.setOnClickListener {
            try {
                PreferenceHelper.clearSessionButKeepBiometric(requireContext())
                val intent = Intent(requireContext(), Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}