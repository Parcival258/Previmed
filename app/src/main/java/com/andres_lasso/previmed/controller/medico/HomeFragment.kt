package com.andres_lasso.previmed.controller.medico.fragmentMedico

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.controller.Login
import com.andres_lasso.previmed.interfaces.RetrofitClient
import com.andres_lasso.previmed.model.ApiResponse
import com.andres_lasso.previmed.model.Medico
import com.andres_lasso.previmed.model.MedicoResponse
import com.andres_lasso.previmed.model.MedicoUpdateRequest
import com.andres_lasso.previmed.utils.PreferenceHelper
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeFragment : Fragment() {

    private var medicoActual: Medico? = null

    private val urlDoctorHombre =
        "https://res.cloudinary.com/dudqqzt1k/image/upload/v1761411224/doctor-hombre_bcfuxr.jpg"
    private val urlDoctorMujer =
        "https://res.cloudinary.com/dudqqzt1k/image/upload/v1761411382/doctor-mujer_lbizms.jpg"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_medico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val txtName = view.findViewById<TextView>(R.id.txtName)
        val txtSpecialty = view.findViewById<TextView>(R.id.txtSpecialty)
        val txtAbout = view.findViewById<TextView>(R.id.txtAbout)
        val btnDisponibilidad = view.findViewById<AppCompatButton>(R.id.btnEstado)
        val btnEstado = view.findViewById<AppCompatButton>(R.id.btnActivo)
        val btnLogout = view.findViewById<AppCompatButton>(R.id.btnLogout)
        val imgDoctor = view.findViewById<ImageView>(R.id.imgDoctor)

        val idMedico = PreferenceHelper.getIdMedico(requireContext())
        if (idMedico != -1) {
            obtenerDatosMedico(
                idMedico,
                txtName,
                txtSpecialty,
                txtAbout,
                btnDisponibilidad,
                btnEstado,
                imgDoctor
            )
        } else {
            Toast.makeText(
                requireContext(),
                "No se encontró el ID del médico",
                Toast.LENGTH_SHORT
            ).show()
        }

        // 🔴 Cerrar sesión
        btnLogout.setOnClickListener {
            PreferenceHelper.clearSessionButKeepBiometric(requireContext())
            val intent = Intent(requireContext(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    /** ==================== 🔹 OBTENER DATOS ==================== **/
    private fun obtenerDatosMedico(
        idMedico: Int,
        txtName: TextView,
        txtSpecialty: TextView,
        txtAbout: TextView,
        btnDisponibilidad: AppCompatButton,
        btnEstado: AppCompatButton,
        imgDoctor: ImageView
    ) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.medicoApi.obtenerMedicoPorId(idMedico)
                if (response.isSuccessful && response.body() != null) {
                    val medicoResponse = response.body()!!
                    val medico = medicoResponse.data ?: return@launch
                    medicoActual = medico
                    val usuario = medico.usuario ?: return@launch

                    txtName.text = "Dr(a). ${usuario.nombre} ${usuario.apellido}"
                    txtSpecialty.text = "Médico general"
                    txtAbout.text =
                        "Acerca de:\nRecuerda que nuestros pacientes son muy importantes. Brinda siempre una atención de calidad."

                    actualizarBotonDisponibilidad(btnDisponibilidad, medico.disponibilidad == true)
                    actualizarBotonEstado(
                        btnEstado,
                        medico.estado == true,
                        medico.disponibilidad == true
                    )

                    val urlImagen =
                        if (usuario.nombre.lowercase().endsWith("a")) urlDoctorMujer else urlDoctorHombre
                    Glide.with(requireContext())
                        .load(urlImagen)
                        .placeholder(R.drawable.doctor)
                        .into(imgDoctor)

                    // 🟢 Cambiar disponibilidad
                    btnDisponibilidad.setOnClickListener {
                        lifecycleScope.launch {
                            val nuevaDisponibilidad = !(medico.disponibilidad ?: false)

                            // ✅ Reglas:
                            // Si se pone "No disponible" → también debe quedar "Inactivo"
                            // Si se pone "Disponible" → pasa a "Activo"
                            val nuevoEstado = if (nuevaDisponibilidad) true else false

                            val exito =
                                actualizarCampoMedico(idMedico, nuevaDisponibilidad, nuevoEstado)
                            if (exito) {
                                medico.disponibilidad = nuevaDisponibilidad
                                medico.estado = nuevoEstado
                                actualizarBotonDisponibilidad(
                                    btnDisponibilidad,
                                    nuevaDisponibilidad
                                )
                                actualizarBotonEstado(
                                    btnEstado,
                                    nuevoEstado,
                                    nuevaDisponibilidad
                                )
                            }
                        }
                    }

                    // 🔵 Cambiar estado (solo si disponible)
                    btnEstado.setOnClickListener {
                        lifecycleScope.launch {
                            val nuevoEstado = !(medico.estado ?: false)

                            // ⚠️ Si el médico NO está disponible, no puede volverse activo
                            if (medico.disponibilidad == false && nuevoEstado) {
                                Toast.makeText(
                                    requireContext(),
                                    "No puedes estar activo si no estás disponible",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@launch
                            }

                            val nuevaDisponibilidad =
                                if (nuevoEstado) true else medico.disponibilidad ?: false

                            val exito =
                                actualizarCampoMedico(idMedico, nuevaDisponibilidad, nuevoEstado)
                            if (exito) {
                                medico.disponibilidad = nuevaDisponibilidad
                                medico.estado = nuevoEstado
                                actualizarBotonDisponibilidad(
                                    btnDisponibilidad,
                                    nuevaDisponibilidad
                                )
                                actualizarBotonEstado(
                                    btnEstado,
                                    nuevoEstado,
                                    nuevaDisponibilidad
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar los datos", Toast.LENGTH_SHORT)
                    .show()
                Log.e("HomeMedicoFragment", "Error: ${e.message}", e)
            }
        }
    }

    /** ==================== 🔹 ACTUALIZAR BACKEND ==================== **/
    private suspend fun actualizarCampoMedico(
        idMedico: Int,
        disponibilidad: Boolean,
        estado: Boolean
    ): Boolean {
        return try {
            val body = MedicoUpdateRequest(
                disponibilidad = disponibilidad,
                estado = estado
            )

            val response: Response<ApiResponse<MedicoResponse>> =
                RetrofitClient.medicoApi.actualizarMedico(idMedico, body)

            if (response.isSuccessful) {
                Log.d("HomeMedicoFragment", "Backend actualizado: $body")
                true
            } else {
                Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
                false
            }
        } catch (e: Exception) {
            Log.e("HomeMedicoFragment", "Error al actualizar médico: ${e.message}")
            Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
            false
        }
    }

    /** ==================== 🔹 ACTUALIZAR VISUAL ==================== **/
    private fun actualizarBotonDisponibilidad(btn: AppCompatButton, disponible: Boolean) {
        if (disponible) {
            btn.text = "🟢 Disponible"
            btn.setBackgroundResource(R.drawable.btn_disponible)
        } else {
            btn.text = "🔴 No disponible"
            btn.setBackgroundResource(R.drawable.btn_no_disponible)
        }
    }

    private fun actualizarBotonEstado(btn: AppCompatButton, activo: Boolean, disponible: Boolean) {
        when {
            // ✅ Disponible y Activo
            disponible && activo -> {
                btn.text = "🟢 Activo"
                btn.setBackgroundResource(R.drawable.btn_disponible)
            }

            // ⚠️ Disponible pero Inactivo → En visita médica
            disponible && !activo -> {
                btn.text = "🟡 En visita médica"
                btn.setBackgroundResource(R.drawable.btn_ocupado)
            }

            // ❌ No disponible → Siempre inactivo (rojo)
            !disponible -> {
                btn.text = "🔴 Inactivo"
                btn.setBackgroundResource(R.drawable.btn_no_disponible)
            }
        }
    }
}
