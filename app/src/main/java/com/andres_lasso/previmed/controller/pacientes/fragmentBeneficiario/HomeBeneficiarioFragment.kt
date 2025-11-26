package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.Login
import com.andres_lasso.previmed.controller.pacientes.Beneficiarios
import com.andres_lasso.previmed.databinding.FragmentHomeBeneficiarioBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.utils.PreferenceHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeBeneficiarioFragment : Fragment() {

    private var _binding: FragmentHomeBeneficiarioBinding? = null
    private val binding get() = _binding!!

    // Datos del perfil
    private var nombrePaciente = ""
    private var planPaciente = ""
    private var estadoPaciente = ""

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
        setupListeners()
    }

    private fun setSaludo() {
        val saludo = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Buenos días"
            in 12..17 -> "Buenas tardes"
            else -> "Buenas noches"
        }

        binding.saludoBeneficiario.text = saludo
    }

    private fun cargarDatos() {
        // Sacamos el nombre desde el contexto
        nombrePaciente = PreferenceHelper.getRole(requireContext()) ?: ""

        // Asignamos nombre e iniciales
        binding.nombreP.text = nombrePaciente
        binding.tvIniciales.text = obtenerIniciales(nombrePaciente)

        // Cargar datos de membresía
        val usuarioId = PreferenceHelper.getUsuarioId(requireContext())
        if (usuarioId.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Error: Usuario no identificado", Toast.LENGTH_SHORT).show()
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val res = RetrofitClient.pagosApi.getMembresiaByUserId(usuarioId)

                if (res.isSuccessful && res.body() != null) {
                    val datos = res.body()!!
                    planPaciente = datos.membresia.plan.tipoPlan

                    // Estado del servicio
                    if (datos.membresia.estado) {
                        estadoPaciente = "Activo"
                        binding.estadoServicio.text = "Servicio Activo"
                        binding.indicadorEstado.setBackgroundResource(R.drawable.circle_green)
                    } else {
                        estadoPaciente = "Inactivo"
                        binding.estadoServicio.text = "Servicio Inactivo"
                        binding.indicadorEstado.setBackgroundResource(R.drawable.circle_red)
                    }

                    // Tipo de plan
                    binding.tvPlan.text = planPaciente

                    // Visitas
                    cargarVisitas(datos.pacienteId)
                }

            } catch (e: Exception) {
                binding.estadoServicio.text = "Error al cargar datos"
            }
        }
    }

    private fun cargarVisitas(pacienteId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val res = RetrofitClient.visitasApi.getVisitas(pacienteId)
                if (res.isSuccessful && res.body() != null) {
                    val visitas = res.body()!!.msj
                    if (!visitas.isNullOrEmpty()) {
                        val visita = visitas.first()
                        val fecha = formatearFecha(visita.fechaVisita ?: "")
                        binding.tvVisitas.text = fecha
                    } else {
                        binding.tvVisitas.text = "Sin visitas agendadas"
                    }
                } else {
                    binding.tvVisitas.text = "Sin visitas agendadas"
                }
            } catch (e: Exception) {
                binding.tvVisitas.text = "Sin visitas agendadas"
            }
        }
    }

    private fun formatearFecha(f: String): String {
        return try {
            val inF = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outF = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inF.parse(f)
            outF.format(date ?: Date())
        } catch (e: Exception) {
            f
        }
    }

    // -----------------------------------------------------
    // LISTENERS
    // -----------------------------------------------------
    private fun setupListeners() {
        // Ir a Beneficiarios
        binding.irBeneficiarios.setOnClickListener {
            startActivity(Intent(requireContext(), Beneficiarios::class.java))
        }

        // Mostrar perfil al tocar las iniciales
        binding.tvIniciales.setOnClickListener {
            mostrarPerfil()
        }

        // CLICK EN EL BOTÓN + → ir a Solicitar visita
        binding.masM.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.paciente_nav, VisitaBeneficiarioFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    // -----------------------------------------------------
    // BOTTOM SHEET PERFIL
    // -----------------------------------------------------
    private fun mostrarPerfil() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.perfil_paciente, null)
        dialog.setContentView(view)

        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        val tvInicial = view.findViewById<TextView>(R.id.tvInicialesPerfil)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombrePerfil)
        val tvPlan = view.findViewById<TextView>(R.id.tvPlanPerfil)
        val tvEstado = view.findViewById<TextView>(R.id.tvEstadoPerfil)
        val btnCerrar = view.findViewById<TextView>(R.id.btnCerrarSesion)
        val btnSalir = view.findViewById<TextView>(R.id.btnCerrarDialogo)

        tvInicial.text = obtenerIniciales(nombrePaciente)
        tvNombre.text = nombrePaciente
        tvPlan.text = "Plan: $planPaciente"
        tvEstado.text = "Estado: $estadoPaciente"

        btnSalir.setOnClickListener { dialog.dismiss() }

        btnCerrar.setOnClickListener {
            cerrarSesion()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun obtenerIniciales(nombre: String): String {
        val partes = nombre.trim().split(" ").filter { it.isNotBlank() }
        return when (partes.size) {
            0 -> ""
            1 -> partes[0].take(2).uppercase()
            else -> "${partes.first()[0]}${partes.last()[0]}".uppercase()
        }
    }

    private fun cerrarSesion() {
        PreferenceHelper.clearSessionButKeepBiometric(requireContext())
        val intent = Intent(requireContext(), Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
