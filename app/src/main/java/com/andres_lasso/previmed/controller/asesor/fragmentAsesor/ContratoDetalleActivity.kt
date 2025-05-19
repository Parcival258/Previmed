package com.andres_lasso.previmed.controller.asesor.fragmentAsesor

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.andres_lasso.previmed.R

class ContratoDetalleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrato_detalle)

        val cedula = intent.getStringExtra("cedula")

        val titulo = findViewById<TextView>(R.id.tituloContrato)
        val etNombre = findViewById<EditText>(R.id.contrato_firma_nombre)
        val etCedula = findViewById<EditText>(R.id.contrato_firma_cedula)
        val etDireccion = findViewById<EditText>(R.id.contrato_firma_direccion)
        val etBarrio = findViewById<EditText>(R.id.contrato_firma_barrio)
        val etDia = findViewById<EditText>(R.id.et_dia)
        val etMes = findViewById<EditText>(R.id.et_mes)
        val etAnio = findViewById<EditText>(R.id.et_anio)

        if (cedula == "1061715858") {
            titulo.text = "Contrato de Daniela Fernanda Herrera Usuga"
            etNombre.setText("Daniela Fernanda Herrera Usuga")
            etCedula.setText("1061715858")
            etDireccion.setText("Carrera 30 #6-83") // puedes cambiar estos valores si tienes una dirección/barrio
            etBarrio.setText("San Jose")
        } else if (cedula == "1061786160") {
            titulo.text = "Contrato de Marlio Hernan Cañar Rosero"
            etNombre.setText("Marlio Hernan Cañar Rosero")
            etCedula.setText("1061786160")
            etDireccion.setText("Carrera 35 #9-30")
            etBarrio.setText("Los campos")
        } else {
            titulo.text = "Contrato no reconocido"
            etNombre.setText("")
            etCedula.setText("")
            etDireccion.setText("")
            etBarrio.setText("")
        }

        // Rellenar fecha actual (opcional, puedes cambiarlo a lo que necesites)
        etDia.setText("19")
        etMes.setText("05")
        etAnio.setText("2025")

        // Desactivar edición de campos
        val campos = listOf(etNombre, etCedula, etDireccion, etBarrio, etDia, etMes, etAnio)
        campos.forEach {
            it.isFocusable = false
            it.isClickable = false
            it.isFocusableInTouchMode = false
        }
    }
}
