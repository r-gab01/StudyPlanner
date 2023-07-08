package com.example.studyplanner.model

import com.google.gson.annotations.SerializedName

data class ArgomentoDBModel(
    @SerializedName("id_arg")
    var idArgomento: Int = -1,

    @SerializedName("id_sess_ref")
    var idSessione: Int = -1,

    @SerializedName("argomento")
    var argomento: String? = ""

)