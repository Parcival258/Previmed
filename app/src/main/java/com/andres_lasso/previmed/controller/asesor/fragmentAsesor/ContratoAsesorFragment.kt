package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.FragmentContratoAsesorBinding

class ContratoAsesorFragment : Fragment() {

    private var _binding: FragmentContratoAsesorBinding? = null
    private val binding get() = _binding!!

    private val palabrasNegritas = listOf(
        "PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA", "SEPTIMA", "OCTAVA", "NOVENA",
        "DÉCIMA", "DÉCIMA PRIMERA", "DÉCIMA SEGUNDA", "DÉCIMA TERCERA", "DÉCIMA CUARTA", "DÉCIMA QUINTA",
        "DÉCIMA SEXTA", "DÉCIMA OCTAVA", "PREVIMED TU MEDICO EN CASA", "EL USUARIO", "CLAUSULAS CONTRACTUALES:",
        "OBJETO DEL CONTRATO:", "PREVIMED", "BENEFICIARIOS", "PACIENTE", "DESCRIPCION DEL SERVICIO",
        "VIGENCIA DEL CONTRATO", "CARCATERISTICAS DEL SERVICIO", "EXCLUSION DE USUARIOS DEL CONTRATO:",
        "PARAGRAFO PRIMERO", "PARAGRAFO SEGUNDO", "ADQUISICION DEL DERECHO DE USUARIO", "FORMA Y PERIODO DE PAGO:",
        "INTERESES DE MORA Y REQUERIMIENTO PREVIO A LA SUSPENSIÓN DEL SERVICIO Y TERMINACION DEL CONTRATO POR MORA EN EL PAGO DE LA TARIFA",
        "EL CONTRATANTE", "OBLIGACIONES DE PREVIMED TU MEDICO EN CASA S.A.S FRENTE A LA PRESENTACION DEL SERVICIO.",
        "RESPONSABILIDAD CIVIL Y ADMINISTRATIVA DE PREVIMED TU MEDICO EN CASA S.A.S", "EL USUARIO y/o los USUARIOS",
        "CAUSALES DE EXONERACION:", "VERACIDAD DE LA INFORMACION", "NOTIFICACIONES POR PARTE PREVIMED", "AREA DE COBERTURA",
        "TERMINACION DEL CONTRATO", "MODIFICACIÓN AL CONTRATO.", "NAVEGACIÓN DEL SERVICIO", "PREVIMED TU MEDICO EN CASA S.A.S",
        "Leandro Realpe Cisneros", "Representante Legal"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContratoAsesorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tituloContrato.text = getString(R.string.title_contrato)

        binding.clausulaIntroduccion.text = getString(R.string.contrato_introduccion)

        val cláusulas = listOf(
            binding.clausula1, binding.clausula2, binding.clausula3, binding.clausula4,
            binding.clausula5, binding.clausula6, binding.clausula7, binding.clausula8,
            binding.clausula9, binding.clausula10, binding.clausula11, binding.clausula12,
            binding.clausula13, binding.clausula14, binding.clausula15, binding.clausula16,
            binding.clausula17, binding.clausula18
        )

        for (i in 1..18) {
            val clause = getString(resources.getIdentifier("contrato_clausula_$i", "string", requireContext().packageName))
            cláusulas[i - 1].text = aplicarNegritas(clause)
        }

        binding.firmacontratoTexto.text = getString(R.string.firmacontrato_texto)

        binding.contratoRepresentante.text = aplicarNegritas(getString(R.string.contrato_representante))

        binding.etDia.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val dia = binding.etDia.text.toString().padStart(2, '0')
                binding.etDia.setText(dia.takeLast(2))
            }
        }

        binding.etMes.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val mes = binding.etMes.text.toString().padStart(2, '0')
                binding.etMes.setText(mes.takeLast(2))
            }
        }

        binding.etAnio.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val anio = binding.etAnio.text.toString().padStart(4, '0')
                binding.etAnio.setText(anio.takeLast(4))
            }
        }
        binding.contratoFirmaNombre.setHint("Nombre")
        binding.contratoFirmaDireccion.setHint("Dirección")

        binding.contratoFirmaBarrio.setHint("Barrio")

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.contratoFirmaNombre.text.toString()
            val cedula = binding.contratoFirmaCedula.text.toString()
            val direccion = binding.contratoFirmaDireccion.text.toString()
            val barrio = binding.contratoFirmaBarrio.text.toString()

            if (nombre.isNotBlank() && cedula.isNotBlank() && direccion.isNotBlank() && barrio.isNotBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Datos guardados:\nNombre: $nombre\nCédula: $cedula\nDirección: $direccion\nBarrio: $barrio",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun aplicarNegritas(texto: String): SpannableString {
        val spannable = SpannableString(texto)

        palabrasNegritas.forEach { palabra ->
            var start = texto.indexOf(palabra)
            while (start >= 0) {
                val end = start + palabra.length
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                start = texto.indexOf(palabra, start + 1)
            }
        }

        return spannable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
