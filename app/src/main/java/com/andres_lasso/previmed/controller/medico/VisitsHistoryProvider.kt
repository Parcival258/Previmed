package com.andres_lasso.previmed.controller.medico

import com.andres_lasso.previmed.controller.medico.medicoDAO.VisitHistoryDao

class VisitsHistoryProvider {
    companion object{
        val visitsHistoryList = listOf<VisitHistoryDao>(
            VisitHistoryDao("andres","lasso","ojeda","calle 123"),
            VisitHistoryDao("yeison","lopez","lopez","calle 124"),
            VisitHistoryDao("carlos","rodriguez","martinez","avenida libertador 456"),
            VisitHistoryDao("maria","gonzalez","perez","calle bolivar 789"),
            VisitHistoryDao("jose","fernandez","garcia","carrera 15 #23-45"),
            VisitHistoryDao("ana","lopez","ruiz","calle 50 #12-34"),
            VisitHistoryDao("luis","martinez","silva","avenida americas 567"),
            VisitHistoryDao("sofia","herrera","morales","calle 72 #45-67"),
            VisitHistoryDao("diego","castro","vargas","carrera 7 #89-12"),
            VisitHistoryDao("laura","jimenez","torres","calle real 234"),
            VisitHistoryDao("miguel","ramirez","santos","avenida caracas 890"),
            VisitHistoryDao("carmen","flores","ramos","calle 85 #56-78"),
            VisitHistoryDao("pedro","moreno","cruz","carrera 30 #67-89"),
            VisitHistoryDao("elena","ortega","mendez","calle central 345"),
            VisitHistoryDao("ricardo","guerrero","rojas","avenida norte 678"),
            VisitHistoryDao("patricia","vega","diaz","calle 92 #34-56"),
            VisitHistoryDao("fernando","delgado","luna","carrera 11 #78-90")
        )
    }
}
