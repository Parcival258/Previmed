package com.andres_lasso.previmed.controller.pacientes.fragmentBeneficiario

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andres_lasso.previmed.databinding.FragmentVisitaBeneficiarioBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.*
import com.andres_lasso.previmed.utils.PreferenceHelper
import kotlinx.coroutines.CancellationException
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

        // Estado inicial del mensaje
        binding.txtMensajeCita.visibility = View.GONE
        binding.txtMensajeCita.text = ""

        cargarMedicos()
        cargarBarrios()

        binding.spinnerMedicos.isEnabled = false
        binding.checkMedico.setOnCheckedChangeListener { _, isChecked ->
            binding.spinnerMedicos.isEnabled = isChecked
        }

        binding.btnSolicitar.setOnClickListener {
            solicitarVisita()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun solicitarVisita() {
        Log.d("VISITA_DEBUG", "Iniciando solicitud de visita")

        // Ocultar mensaje previo
        binding.txtMensajeCita.visibility = View.GONE
        binding.txtMensajeCita.text = ""

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
            return
        }
        if (!telefono.matches(Regex("^3[0-9]{9}$"))) {
            Toast.makeText(requireContext(), "Número debe tener 10 dígitos y empezar con 3", Toast.LENGTH_SHORT).show()
            return
        }
        if (idPaciente == 0) {
            Toast.makeText(requireContext(), "No hay paciente autenticado", Toast.LENGTH_SHORT).show()
            return
        }
        if (direccion.isEmpty()) {
            Toast.makeText(requireContext(), "La dirección es obligatoria", Toast.LENGTH_SHORT).show()
            return
        }
        if (barrioPos == 0) {
            Toast.makeText(requireContext(), "Selecciona un barrio", Toast.LENGTH_SHORT).show()
            return
        }

        // ---------- LÓGICA DEL MÉDICO ----------
        val medicoId: Int = if (deseaMedico) {
            if (medicoPos == 0) {
                Toast.makeText(requireContext(), "Selecciona un médico", Toast.LENGTH_SHORT).show()
                return
            }
            val medicoSeleccionado = medicos[medicoPos - 1]
            if (medicoSeleccionado.disponibilidad != true) {
                Toast.makeText(requireContext(), "El médico seleccionado no está disponible", Toast.LENGTH_SHORT).show()
                return
            }
            medicoSeleccionado.id_medico
        } else {
            val medicoDisponible = medicos.firstOrNull { it.disponibilidad == true }
            if (medicoDisponible == null) {
                Toast.makeText(requireContext(), "No hay médicos disponibles en este momento", Toast.LENGTH_SHORT).show()
                return
            }
            medicoDisponible.id_medico
        }

        // BARRIO
        if (barrioPos - 1 < 0 || barrioPos - 1 >= barrios.size) {
            Toast.makeText(requireContext(), "Error al obtener el barrio", Toast.LENGTH_SHORT).show()
            return
        }
        val barrioId = barrios[barrioPos - 1].idBarrio
        if (barrioId == null || barrioId == 0) {
            Toast.makeText(requireContext(), "Barrio inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val fechaVisita = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

        val visita = VisitasRequest(
            fechaVisita = fechaVisita,
            descripcion = descripcion.ifEmpty { null },
            direccion = direccion,
            estado = true,
            telefono = telefono,
            pacienteId = idPaciente,
            medicoId = medicoId,
            barrioId = barrioId
        )

        Log.d(
            "VISITA_DEBUG", """
            Enviando visita:
            - fechaVisita: $fechaVisita
            - descripcion: ${visita.descripcion}
            - direccion: $direccion
            - telefono: $telefono
            - pacienteId: $idPaciente
            - medicoId: $medicoId
            - barrioId: $barrioId
        """.trimIndent()
        )

        // Deshabilitar botón mientras se envía
        binding.btnSolicitar.isEnabled = false

        // Usar SIEMPRE viewLifecycleOwner.lifecycleScope
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = apiService.crearVisita(visita)

                if (!isAdded || _binding == null) {
                    Log.w("VISITA_DEBUG", "Fragment ya no está adjunto al recibir respuesta")
                    return@launch
                }

                Log.d("VISITA_DEBUG", "Respuesta Retrofit: code=${response.code()}")

                if (response.isSuccessful) {
                    // Ocultar teclado para que se vea el mensaje
                    try {
                        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                    } catch (_: Exception) { }

                    Toast.makeText(requireContext(), "Solicitud enviada correctamente", Toast.LENGTH_SHORT).show()

                    // 🔴 MOSTRAR MENSAJE ABAJO Y FORZAR QUE SE VEA
                    binding.txtMensajeCita.apply {
                        text = "✅ Tu visita se guardó correctamente."
                        visibility = View.VISIBLE
                        bringToFront()
                        requestLayout()
                        invalidate()
                    }
                    // También forzamos redibujo del root por si acaso
                    binding.root.requestLayout()

                    // Si quieres limpiar el formulario, descomenta:
                    // limpiarFormulario()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("VISITA_ERROR", "Error: ${response.code()} - $errorBody")

                    binding.txtMensajeCita.apply {
                        text = "⚠️ Ocurrió un error al guardar la visita."
                        visibility = View.VISIBLE
                        bringToFront()
                        requestLayout()
                        invalidate()
                    }

                    Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                if (!isAdded || _binding == null) return@launch

                if (e !is CancellationException) {
                    Log.e("VISITA_EXCEPTION", "Exception: ${e.message}", e)

                    binding.txtMensajeCita.apply {
                        text = "❌ Error de conexión al guardar la visita."
                        visibility = View.VISIBLE
                        bringToFront()
                        requestLayout()
                        invalidate()
                    }

                    Toast.makeText(requireContext(), "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                if (isAdded && _binding != null) {
                    binding.btnSolicitar.isEnabled = true
                }
            }
        }
    }

    private fun limpiarFormulario() {
        if (!isAdded || _binding == null) return

        binding.edDireccion.text.clear()
        binding.edDescripcion.text.clear()
        binding.edTelefono.text.clear()
        binding.checkMedico.isChecked = false
        binding.spinnerMedicos.setSelection(0)
        binding.spinnerBarrios.setSelection(0)
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

                if (!isAdded || _binding == null) return@launch

                if (response.isSuccessful) {
                    medicos.clear()
                    val nuevos = response.body()?.data ?: emptyList()
                    medicos.addAll(mapToOldMedico(nuevos))

                    val disponibles = medicos.filter {
                        (it.estado ?: false) && (it.disponibilidad ?: false)
                    }

                    val listaNombres = mutableListOf("Seleccione un médico")
                    listaNombres.addAll(
                        disponibles.map { med ->
                            val u = med.usuario
                            if (u != null) "${u.nombre} ${u.apellido}" else "Médico asignado"
                        }
                    )

                    val adaptador = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        listaNombres
                    )
                    adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerMedicos.adapter = adaptador
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error cargando médicos: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                if (isAdded && _binding != null && e !is CancellationException) {
                    Toast.makeText(
                        requireContext(),
                        "Error cargando médicos: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun cargarBarrios() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = apiService.getBarrios()

                if (!isAdded || _binding == null) return@launch

                if (response.isSuccessful) {
                    barrios.clear()
                    barrios.addAll(response.body()?.msj ?: emptyList())

                    val listaNombres = mutableListOf("Seleccione un barrio")
                    listaNombres.addAll(barrios.map { it.nombreBarrio })

                    val adaptador = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        listaNombres
                    )
                    adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerBarrios.adapter = adaptador
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error cargando barrios: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                if (isAdded && _binding != null && e !is CancellationException) {
                    Toast.makeText(
                        requireContext(),
                        "Error cargando barrios: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
