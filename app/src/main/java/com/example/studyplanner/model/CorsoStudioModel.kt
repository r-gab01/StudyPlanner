package com.example.studyplanner.model

import com.google.gson.annotations.SerializedName

data class CorsoStudioModel(
    @SerializedName("id_corso")
    var idCorso: Int? = -1,

    @SerializedName("nome_corso")
    var nomeCorso: String? = ""
)
