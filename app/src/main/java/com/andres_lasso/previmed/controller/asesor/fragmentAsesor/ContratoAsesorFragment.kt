package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.andres_lasso.previmed.R
import com.andres_lasso.previmed.databinding.FragmentContratoAsesorBinding

class ContratoAsesorFragment : Fragment() {

    private var _binding: FragmentContratoAsesorBinding? = null
    private val binding get() = _binding!!

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

        for (i in 1..18) {

            val clause = getString(resources.getIdentifier("contrato_clausula_$i", "string", context?.packageName))

            val palabrasNegritas = listOf("PRIMERA", "SEGUNDA", "TERCERA", "CUARTA", "QUINTA", "SEXTA", "SEPTIMA", "OCTAVA","NOVENA","DÉCIMA","DÉCIMA PRIMERA","DÉCIMA SEGUNDA","DÉCIMA TERCERA","DÉCIMA CUARTA","DÉCIMA QUINTA","DÉCIMA SEXTA","DÉCIMA OCTAVA","Leandro Realpe Cisneros","PREVIMED TU MEDICO EN CASA","EL USUARIO","CLAUSULAS CONTRACTUALES:","OBJETO DEL CONTRATO:","PREVIMED","BENEFICIARIOS","PACIENTE","DESCRIPCION DEL SERVICIO","VIGENCIA DEL CONTRATO","CARCATERISTICAS DEL SERVICIO","EXCLUSION DE USUARIOS DEL CONTRATO:","PARAGRAFO PRIMERO","PARAGRAFO SEGUNDO","ADQUISICION DEL DERECHO DE USUARIO","FORMA Y PERIODO DE PAGO:","INTERESES DE MORA Y REQUERIMIENTO PREVIO A LA SUSPENSIÓN DEL SERVICIO Y TERINACION DEL CONTRATO POR MORA EN EL PAGO DE LA TARIFA","EL CONTRATANTE","OBLIGACIONES DE PREVIMED TU MEDICO EN CASA S.A.S FRENTE A LA PRESENTACION DEL SERVICIO.","RESPONSABILIDAD CIVIL Y ADMINISTRATIVA DE PREVIMED TU MEDICO EN CASA S.A.S","EL USUARIO y/o los USUARIOS","CAUSALES DE EXONERACION:","VERACIDAD DE LA INFORMACION","NOTIFICACIONES POR PARTE PREVIMED","AREA DE COBERTURA","TERMINACION DEL CONTRATO","MODIFICACIÓN AL CONTRATO.","NAVEGACIÓN DEL SERVICIO","PREVIMED TU MEDICO EN CASA S.A.S"," Leandro Realpe Cisneros")

            val spannableClause = SpannableString(clause)
            for (palabra in palabrasNegritas) {
                var start = clause.indexOf(palabra)
                while (start != -1) {
                    val end = start + palabra.length
                    spannableClause.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    start = clause.indexOf(palabra, start + 1)
                }
            }


            val clauseViewId = resources.getIdentifier("clausula$i", "id", context?.packageName)
            val clauseView = binding.root.findViewById<View>(clauseViewId) as TextView
            clauseView.text = spannableClause
        }

        binding.contratoFirma.text = getString(R.string.contrato_firma)
    }


}
