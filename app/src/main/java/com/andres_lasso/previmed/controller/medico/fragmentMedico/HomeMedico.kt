package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
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
    private val refreshInterval = 5000L
    private lateinit var estadoReceiver: BroadcastReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeMedicoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idMedico = PreferenceHelper.getIdMedico(requireContext())
        val usuarioId = PreferenceHelper.getUsuarioId(requireContext())

        if (idMedico != -1) {
            obtenerYRenderPorId(idMedico)
            startAutoRefresh(idMedico)
        } else if (!usuarioId.isNullOrBlank()) {
            obtenerYRenderPorUsuario(usuarioId)
        }

        // 🔄 Escuchar broadcast cuando se inicia o finaliza una visita
        estadoReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: android.content.Intent?) {
                if (PreferenceHelper.isVisitaActiva(requireContext())) {
                    actualizarBotonesVisualesAnimado(true, false)
                } else {
                    actualizarBotonesVisualesAnimado(true, true)
                }
            }
        }
        requireContext().registerReceiver(estadoReceiver, IntentFilter("ACTUALIZAR_ESTADO_MEDICO"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        requireContext().unregisterReceiver(estadoReceiver)
        _binding = null
    }

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
                    } catch (_: Exception) { }
                }
                handler.postDelayed(this, refreshInterval)
            }
        }, refreshInterval)
    }

    private fun obtenerYRenderPorId(idMedico: Int) {
        lifecycleScope.launch {
            val response = RetrofitClient.medicoApi.obtenerMedicoPorId(idMedico)
            renderResponse(response)
        }
    }

    private fun obtenerYRenderPorUsuario(usuarioId: String) {
        lifecycleScope.launch {
            val response = RetrofitClient.medicoApi.obtenerMedicoPorUsuario(usuarioId)
            renderResponse(response)
        }
    }

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
        """.trimIndent()

        if (PreferenceHelper.isVisitaActiva(requireContext())) {
            actualizarBotonesVisualesAnimado(true, false)
        } else {
            actualizarBotonesVisualesAnimado(medico.disponibilidad ?: false, medico.estado ?: false)
        }

        Glide.with(requireContext()).load(R.drawable.doctor).into(binding.imgDoctor)
        configurarBotones()
    }

    private fun configurarBotones() {
        val medico = medicoActual ?: return
        binding.btnEstado.setOnClickListener {
            lifecycleScope.launch {
                val nuevaDisponibilidad = !(medico.disponibilidad ?: false)
                val nuevoEstado = nuevaDisponibilidad
                medico.disponibilidad = nuevaDisponibilidad
                medico.estado = nuevoEstado
                actualizarBotonesVisualesAnimado(nuevaDisponibilidad, nuevoEstado)
                actualizarBackend(medico.id_medico, nuevaDisponibilidad, nuevoEstado)
            }
        }
    }

    private suspend fun actualizarBackend(idMedico: Int?, disponibilidad: Boolean, estado: Boolean) {
        if (idMedico == null) return
        try {
            val body = MedicoUpdateRequest(disponibilidad = disponibilidad, estado = estado)
            val response = RetrofitClient.medicoApi.actualizarMedico(idMedico, body)
            if (response.isSuccessful) {
                val medicoSrv = response.body()?.data?.medicoOrNull
                medicoSrv?.let {
                    medicoActual = it
                    actualizarBotonesVisualesAnimado(it.disponibilidad ?: false, it.estado ?: false)
                }
            }
        } catch (_: Exception) { }
    }

    private fun animarColor(vista: View, colorInicial: Int, colorFinal: Int) {
        val anim = ValueAnimator.ofObject(ArgbEvaluator(), colorInicial, colorFinal)
        anim.duration = 400
        anim.addUpdateListener { vista.setBackgroundColor(it.animatedValue as Int) }
        anim.start()
    }

    private fun actualizarBotonesVisualesAnimado(disponible: Boolean, activo: Boolean) {
        val ocupado = disponible && !activo
        val color = ContextCompat.getColor(requireContext(), when {
            ocupado -> R.color.amarillo_estado
            disponible && activo -> R.color.verde_estado
            else -> R.color.rojo_estado
        })
        binding.btnEstado.text = when {
            ocupado -> "Ocupado ⏳"
            disponible && activo -> "Disponible "
            else -> "No disponible "
        }
        binding.btnActivo.text = when {
            ocupado -> "Inactivo "
            disponible && activo -> "Activo "
            else -> "Inactivo "
        }
        animarColor(binding.btnEstado, R.color.white, color)
        animarColor(binding.btnActivo, R.color.white, color)
    }
}
