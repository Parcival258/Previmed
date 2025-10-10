package com.andres_lasso.previmed.model

import com.google.gson.annotations.SerializedName

data class MedicoListWrapper(
    @SerializedName("data") val data: List<MedicoIndividualResponse>
)