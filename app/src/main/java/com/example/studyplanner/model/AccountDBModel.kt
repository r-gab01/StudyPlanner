package com.example.studyplanner.model

import com.google.gson.annotations.SerializedName

data class AccountDBModel(
    @SerializedName("nome_u_ref")
    var nomeUtente: String? = "",

    @SerializedName("password")
    var password: String? = "",

    @SerializedName("domanda_s")
    var domandaS: String? = "",

    @SerializedName("risposta_s")
    var rispostaS: String? = "",
    )
