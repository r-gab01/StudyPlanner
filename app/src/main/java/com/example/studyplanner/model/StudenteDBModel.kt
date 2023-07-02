package com.example.studyplanner.model

import com.google.gson.annotations.SerializedName

data class StudenteDBModel(
    @SerializedName("nome_utente")
    var nomeUtente: String? = "",

    @SerializedName("nome")
    var nome: String? = "",

    @SerializedName("cognome")
    var cognome: String? = "",

    @SerializedName("foto")
    var foto: String? = "",

    @SerializedName("data_nascita")
    var dataNascita: String? = "",

    @SerializedName("universita")
    var universita: String? = "",

    @SerializedName("id_c_ref")
    var idCorso: Int = -1,

)
