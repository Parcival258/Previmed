package com.andres_lasso.previmed.controller.asesor.recycler
import com.andres_lasso.previmed.controller.asesor.recycler.adapter.Beneficios

class PlanesProvider {
    companion object{
        val planesList = listOf<PlanesClass>(
            PlanesClass(
                idPlan = 1,
                tipoPlan = "Basico",
                beneficios = listOf(Beneficios(id=1,nombre="Acceso a médicos generales",idPlan=1), Beneficios(id = 2, nombre = "Ser saludable",idPlan = 1)),
                desripcionPlan = "Plan basico",
                precioPlan = "22.900"
            ),
            PlanesClass(
                idPlan = 3,
                tipoPlan = "Familiar",
                beneficios = listOf(Beneficios(id=1,nombre="Acceso a médicos generales",idPlan=3), Beneficios(id = 2, nombre = "Ser saludable",idPlan = 3)),
                desripcionPlan = "Un plan para toda la familia",
                precioPlan = "34.900"
            ),
            PlanesClass(
                idPlan = 4,
                tipoPlan = "PRO",
                beneficios = listOf(Beneficios(id=1,nombre="Acceso a médicos generales",idPlan=4), Beneficios(id = 2, nombre = "Ser saludable",idPlan = 4)),
                desripcionPlan = "Un plan bien PRO",
                precioPlan = "42.900"
            ),
            PlanesClass(
                idPlan = 5,
                tipoPlan = "VIP",
                beneficios = listOf(Beneficios(id=1,nombre="Acceso a médicos generales",idPlan=5), Beneficios(id = 2, nombre = "Ser saludable",idPlan = 5)),
                desripcionPlan = "Un plan para los VIP",
                precioPlan = "62.900"
            )
        )
    }
}