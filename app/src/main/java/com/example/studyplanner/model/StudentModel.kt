package com.example.studyplanner.model

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class StudentModel(
    @SerializedName("nome utente")
    var nomeUtente : String?,

    @SerializedName("nome")
    var nome : String?,

    @SerializedName("cognome")
    var cognome : String?,

    @SerializedName("foto")
    var foto : String?,

    @SerializedName("data nascita")
    var dataNascita : Date?,

    @SerializedName("universita")
    var universita : String?,

    @SerializedName("id corso")
    var corsoStudi : Int?,


)
