package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.FragmentHomeMedicoBinding
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.Medico
import com.andres_lasso.previmed.model.MedicoResponse
import com.andres_lasso.previmed.model.MedicoUpdateRequest
import com.andres_lasso.previmed.utils.PreferenceHelper
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeMedico : Fragment() {

    private var _binding: FragmentHomeMedicoBinding? = null
    private val binding get() = _binding!!

    private var medicoActual: Medico? = null
    private val handler = Handler(Looper.getMainLooper())
    private val refreshInterval = 5000L // 🔁 cada 5 segundos

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMedicoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idMedico = PreferenceHelper.getIdMedico(requireContext())
        val usuarioId = PreferenceHelper.getUsuarioId(requireContext())

        if (idMedico != null && idMedico != -1) {
            obtenerYRenderPorId(idMedico)
            startAutoRefresh(idMedico)
        } else if (!usuarioId.isNullOrBlank()) {
            obtenerYRenderPorUsuario(usuarioId)
        }
    }

    /** ==================== 🔄 AUTO REFRESCO ==================== **/
    private fun startAutoRefresh(idMedico: Int?) {
        if (idMedico == null || idMedico == -1) return
        handler.postDelayed(object : Runnable {
            override fun run() {
                lifecycleScope.launch {
                    try {
                        val response = RetrofitClient.medicoApi.obtenerMedicoPorId(idMedico)
                        if (response.isSuccessful) {
                            renderResponse(response)
                        }
                    } catch (_: Exception) {
                    }
                }
                handler.postDelayed(this, refreshInterval)
            }
        }, refreshInterval)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }

    /** ==================== 🔹 OBTENER DATOS ==================== **/
    private fun obtenerYRenderPorId(idMedico: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.medicoApi.obtenerMedicoPorId(idMedico)
                renderResponse(response)
            } catch (_: Exception) {
            }
        }
    }

    private fun obtenerYRenderPorUsuario(usuarioId: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.medicoApi.obtenerMedicoPorUsuario(usuarioId)
                renderResponse(response)
            } catch (_: Exception) {
            }
        }
    }

    /** ==================== 🔹 MOSTRAR DATOS ==================== **/
    private fun renderResponse(response: Response<MedicoResponse>) {
        if (!response.isSuccessful) return
        val medico = response.body()?.medicoOrNull ?: return
        medicoActual = medico

        val usuario = medico.usuario
        binding.txtName.text = "Dr(a). ${usuario?.nombre ?: ""} ${usuario?.apellido ?: ""}"
        binding.txtSpecialty.text = "Médico general"
        binding.txtAbout.text = """
            Acerca de:
            Recuerda que nuestros pacientes son muy importantes.
            Brinda siempre una atención de calidad.
            
            Documento: ${usuario?.numero_documento ?: "No disponible"}
            Email: ${usuario?.email ?: "Sin correo registrado"}
        """.trimIndent()

        actualizarBotonesVisualesAnimado(medico.disponibilidad ?: false, medico.estado ?: false)

        Glide.with(requireContext())
            .load(R.drawable.doctor)
            .into(binding.imgDoctor)

        configurarBotones()
    }

    /** ==================== 🔹 CONFIGURAR BOTONES ==================== **/
    private fun configurarBotones() {
        val medico = medicoActual ?: return

        // 🔘 Botón de disponibilidad (Disponible / No disponible)
        binding.btnEstado.setOnClickListener {
            lifecycleScope.launch {
                val nuevaDisponibilidad = !(medico.disponibilidad ?: false)
                var nuevoEstado = medico.estado ?: false

                // 🔁 Reglas personalizadas
                if (!nuevaDisponibilidad) {
                    // Si pasa a "No disponible" → también pasa a "Inactivo"
                    nuevoEstado = false
                } else {
                    // Si pasa a "Disponible" → también pasa a "Activo"
                    nuevoEstado = true
                }

                medico.disponibilidad = nuevaDisponibilidad
                medico.estado = nuevoEstado

                actualizarBotonesVisualesAnimado(nuevaDisponibilidad, nuevoEstado)
                actualizarBackend(medico.id_medico, nuevaDisponibilidad, nuevoEstado)
            }
        }

        // 🔘 Botón de estado (Activo / Inactivo)
        binding.btnActivo.setOnClickListener {
            lifecycleScope.launch {
                var nuevoEstado = !(medico.estado ?: false)
                var nuevaDisponibilidad = medico.disponibilidad ?: false

                // 🔁 Reglas personalizadas
                if (nuevoEstado) {
                    // Si pasa a "Activo" → también pasa a "Disponible"
                    nuevaDisponibilidad = true
                } else {
                    // Si pasa a "Inactivo" → puede seguir disponible (ocupado)
                    // No se cambia la disponibilidad
                }

                medico.estado = nuevoEstado
                medico.disponibilidad = nuevaDisponibilidad

                actualizarBotonesVisualesAnimado(nuevaDisponibilidad, nuevoEstado)
                actualizarBackend(medico.id_medico, nuevaDisponibilidad, nuevoEstado)
            }
        }
    }

    /** ==================== 🔹 ACTUALIZAR BACKEND ==================== **/
    private suspend fun actualizarBackend(idMedico: Int?, disponibilidad: Boolean, estado: Boolean) {
        if (idMedico == null) return
        try {
            val body = MedicoUpdateRequest(
                disponibilidad = disponibilidad,
                estado = estado
            )

            val response = RetrofitClient.medicoApi.actualizarMedico(idMedico, body)

            if (response.isSuccessful) {
                val medicoSrv = response.body()?.data?.medicoOrNull
                Toast.makeText(requireContext(), "Estado actualizado correctamente", Toast.LENGTH_SHORT).show()

                // 🔄 Refresca UI inmediatamente con lo que devolvió el backend
                medicoSrv?.let {
                    medicoActual = it
                    actualizarBotonesVisualesAnimado(it.disponibilidad ?: false, it.estado ?: false)
                }
            } else {
                Toast.makeText(requireContext(), "Error al actualizar estado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
        }
    }





    /** ==================== 🔹 ANIMACIÓN DE COLOR ==================== **/
    private fun animarColor(vista: View, colorInicial: Int, colorFinal: Int) {
        val anim = ValueAnimator.ofObject(ArgbEvaluator(), colorInicial, colorFinal)
        anim.duration = 400 // duración suave
        anim.addUpdateListener { valueAnimator ->
            val colorActual = valueAnimator.animatedValue as Int
            vista.setBackgroundColor(colorActual)
        }
        anim.start()
    }

    /** ==================== 🔹 VISUAL SEGÚN COMBINACIÓN + ANIMACIÓN ==================== **/
    private fun actualizarBotonesVisualesAnimado(disponible: Boolean, activo: Boolean) {
        val ocupado = disponible && !activo

        val colorActualEstado =
            (binding.btnEstado.background as? android.graphics.drawable.ColorDrawable)?.color
                ?: ContextCompat.getColor(requireContext(), R.color.rojo_estado)
        val colorActualActivo =
            (binding.btnActivo.background as? android.graphics.drawable.ColorDrawable)?.color
                ?: ContextCompat.getColor(requireContext(), R.color.rojo_estado)

        when {
            ocupado -> {
                binding.btnEstado.text = "Ocupado ⏳"
                binding.btnActivo.text = "Inactivo 🔴"
                val nuevoColor = ContextCompat.getColor(requireContext(), R.color.amarillo_estado)
                animarColor(binding.btnEstado, colorActualEstado, nuevoColor)
                animarColor(binding.btnActivo, colorActualActivo, nuevoColor)
            }

            disponible && activo -> {
                binding.btnEstado.text = "Disponible ✅"
                binding.btnActivo.text = "Activo 🟢"
                val nuevoColor = ContextCompat.getColor(requireContext(), R.color.verde_estado)
                animarColor(binding.btnEstado, colorActualEstado, nuevoColor)
                animarColor(binding.btnActivo, colorActualActivo, nuevoColor)
            }

            else -> {
                binding.btnEstado.text = "No disponible ❌"
                binding.btnActivo.text = "Inactivo 🔴"
                val nuevoColor = ContextCompat.getColor(requireContext(), R.color.rojo_estado)
                animarColor(binding.btnEstado, colorActualEstado, nuevoColor)
                animarColor(binding.btnActivo, colorActualActivo, nuevoColor)
            }
        }
    }
}