package com.example.studyplanner.database

import android.util.Log
import com.example.studyplanner.model.MyJsonArray
import com.google.gson.JsonArray
import com.google.gson.JsonObject


object BoundaryDBMS {

     fun login( nome: String, password: String ): Boolean {
         val query = "select * from autenticazione where nome_u_ref = '${nome}' and password = '${password}';"
         ClientNetwork.select(query)
         val result = MyJsonArray.getSize()
         return result==1
     }
}

