package com.example.studyplanner.model

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class CarrieraDBModel(
    @SerializedName("nome_u_ref")
    var nomeUtente: String? = " ",

    @SerializedName("nome_m_ref")
    var nomeMateria: String? = "",

    @SerializedName("id_c_ref")
    var idCorso: Int?= -1,

    @SerializedName("voto")
    var voto: Int = -1,

    @SerializedName("cfu")
    var cfu: Int = -1,
)
