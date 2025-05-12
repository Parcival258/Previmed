package com.andres_lasso.previmed.controller.asesor.recycler

class PlanesProvider {
    companion object{
        val planesList = listOf<PlanesClass>(
            PlanesClass(
                idPlan = 1,
                tipoPlan = "Basico",
                desripcionPlan = "Un plan basico",
                precioPlan = "22.900"
            ),
            PlanesClass(
                idPlan = 2,
                tipoPlan = "Super Basico",
                desripcionPlan = "Un plan super basico",
                precioPlan = "20.900"
            ),
            PlanesClass(
                idPlan = 3,
                tipoPlan = "Familiar",
                desripcionPlan = "Un plan para toda la familia",
                precioPlan = "34.900"
            ),
            PlanesClass(
                idPlan = 4,
                tipoPlan = "PRO",
                desripcionPlan = "Un plan bien PRO",
                precioPlan = "42.900"
            ),
            PlanesClass(
                idPlan = 5,
                tipoPlan = "VIP",
                desripcionPlan = "Un plan para los VIP",
                precioPlan = "62.900"
            )
        )
    }
}