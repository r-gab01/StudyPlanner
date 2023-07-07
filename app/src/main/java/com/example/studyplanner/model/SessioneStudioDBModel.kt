package com.example.studyplanner.model

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class SessioneStudioDBModel(

    @SerializedName("id_sessione")
    var idSessione: Int? = -1,

    @SerializedName("nome_u_ref")
    var nomeUtente: String? = "",

    @SerializedName("nome_m_ref")
    var nomeMateria: String?=" ",

    @SerializedName("id_c_ref")
    var idCorso: Int = -1,

    @SerializedName("data_appello")
    var dataAppello: Date,

    @SerializedName("fonte_studio")
    var fonteStudio: String? = "",

    @SerializedName("pagine_tot")
    var pagineTot: Int? = -1,

    @SerializedName("pagine_stud")
    var pagineStud: Int? = -1,
)
