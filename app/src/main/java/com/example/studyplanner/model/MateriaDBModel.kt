package com.example.studyplanner.model

import com.google.gson.annotations.SerializedName

data class MateriaDBModel(

@SerializedName("nome_m")
var nomeMateria: String? = "",

@SerializedName("id_c_ref")
var idCorso: Int = -1,

@SerializedName("docente")
var docente: String? = "",

@SerializedName("cfu")
var cfu: Int = -1,
)
