package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andres_lasso.previmed.databinding.FragmentVisitaBeneficiarioBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.*
import com.andres_lasso.previmed.utils.MedicoCache
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class VisitaBeneficiarioFragment : Fragment() {

    private var _binding: FragmentVisitaBeneficiarioBinding? = null
    private val binding get() = _binding!!

    private val medicos = mutableListOf<Medico>()
    private val barrios = mutableListOf<Barrio>()
    private lateinit var edTelefono: EditText

    private val apiService = RetrofitClient.visitasApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentVisitaBeneficiarioBinding.inflate(inflater, container, false)
        .also { _binding = it }.root

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarMedicos()
        cargarBarrios()

        binding.spinnerMedicos.isEnabled = false
        binding.checkMedico.setOnCheckedChangeListener { _, isChecked ->
            binding.spinnerMedicos.isEnabled = isChecked
        }

        binding.btnSolicitar.setOnClickListener {
            val direccion = binding.edDireccion.text.toString().trim()
            val descripcion = binding.edDescripcion.text.toString().trim()
            val deseaMedico = binding.checkMedico.isChecked
            val medicoPos = binding.spinnerMedicos.selectedItemPosition
            val barrioPos = binding.spinnerBarrios.selectedItemPosition
            edTelefono = binding.edTelefono

            val idPacienteStr = PreferenceHelper.getIdPaciente(requireContext())
            val idPaciente = idPacienteStr?.toIntOrNull() ?: 0
            val telefono = edTelefono.text.toString().trim()

            // ---------- VALIDACIONES ----------
            if (telefono.isEmpty()) {
                Toast.makeText(requireContext(), "Ingresa un teléfono de contacto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!telefono.matches(Regex("^3[0-9]{9}$"))) {
                Toast.makeText(requireContext(), "Número debe tener 10 dígitos y empezar con 3", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (idPaciente == 0) {
                Toast.makeText(requireContext(), "No hay paciente autenticado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (direccion.isEmpty()) {
                Toast.makeText(requireContext(), "La dirección es obligatoria", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (barrioPos == 0) {
                Toast.makeText(requireContext(), "Selecciona un barrio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ---------- LÓGICA DEL MÉDICO ----------
            val medicoId: Int
            if (deseaMedico) {
                // Manual: selecciona uno disponible
                if (medicoPos == 0) {
                    Toast.makeText(requireContext(), "Selecciona un médico", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val medicoSeleccionado = medicos[medicoPos - 1]
                if (medicoSeleccionado.disponibilidad != true) {
                    Toast.makeText(requireContext(), "El médico seleccionado no está disponible", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                medicoId = medicoSeleccionado.id_medico
            } else {
                // Automático: primer médico disponible
                val medicoDisponible = medicos.firstOrNull { it.disponibilidad == true }
                if (medicoDisponible == null) {
                    Toast.makeText(requireContext(), "No hay médicos disponibles en este momento", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                medicoId = medicoDisponible.id_medico
            }

            val barrioId = barrios[barrioPos - 1].idBarrio
            val fechaVisita = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

            val visita = VisitasRequest(
                fechaVisita = fechaVisita,
                descripcion = descripcion.takeIf { it.isNotEmpty() },
                direccion = direccion,
                estado = true,
                telefono = telefono,
                pacienteId = idPaciente,
                medicoId = medicoId,
                barrioId = barrioId
            )

            lifecycleScope.launch {
                try {
                    val response = apiService.crearVisita(visita)
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Solicitud enviada correctamente", Toast.LENGTH_SHORT).show()
                        limpiarFormulario()
                    } else {
                        Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun limpiarFormulario() {
        binding.edDireccion.text.clear()
        binding.edDescripcion.text.clear()
        binding.checkMedico.isChecked = false
        binding.spinnerMedicos.setSelection(0)
        binding.spinnerBarrios.setSelection(0)
        edTelefono.text.clear()

        lifecycleScope.launch {
            try {
                val resp = RetrofitClient.visitasApi.getMedicos()
                if (resp.isSuccessful) {
                    resp.body()?.data?.let { MedicoCache.set(it) }
                }
            } catch (_: Exception) { }
        }
    }

    private fun mapToOldMedico(list: List<MedicoIndividualResponse>): List<Medico> =
        list.map { src ->
            Medico(
                id_medico = src.id_medico,
                disponibilidad = src.disponibilidad,
                estado = src.estado,
                usuario_id = src.usuario_id,
                usuario = UsuarioMedico(
                    id_usuario = src.usuario_id,
                    nombre = src.usuario?.nombre ?: "",
                    apellido = src.usuario?.apellido ?: "",
                    email = src.usuario?.email ?: "",
                    numero_documento = src.usuario?.numeroDocumento ?: ""
                )
            )
        }

    private fun cargarMedicos() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = apiService.getMedicos()
                if (response.isSuccessful) {
                    medicos.clear()
                    val nuevos = response.body()?.data ?: emptyList()
                    medicos.addAll(mapToOldMedico(nuevos))

                    // ✅ Manejo seguro de Boolean nulos
                    val disponibles = medicos.filter { (it.estado ?: false) && (it.disponibilidad ?: false) }

                    val listaNombres = mutableListOf("Seleccione un médico")
                    listaNombres.addAll(disponibles.map { med ->
                        val user = med.usuario
                        if (user != null) "${user.nombre} ${user.apellido}" else "Médico asignado"
                    })

                    val adaptador = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        listaNombres
                    )
                    adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerMedicos.adapter = adaptador
                } else {
                    Toast.makeText(requireContext(), "Error cargando médicos: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error cargando médicos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cargarBarrios() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = apiService.getBarrios()
                if (response.isSuccessful) {
                    barrios.clear()
                    barrios.addAll(response.body()?.msj ?: emptyList())
                    val listaNombres = mutableListOf("Seleccione un barrio")
                    listaNombres.addAll(barrios.map { it.nombreBarrio })
                    val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaNombres)
                    adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerBarrios.adapter = adaptador
                } else {
                    Toast.makeText(requireContext(), "Error cargando barrios: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error cargando barrios: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
