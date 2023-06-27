package com.example.studyplanner.database

import com.google.gson.JsonObject


object BoundaryDBMS {

     fun login( nome: String, password: String ){
         val query = "select * from autenticazione where nome_u_ref = '${nome}' and password = '${password}';"
         val response = ClientNetwork.select(query)
         //val verifica = (response.get(0) as JsonObject).asString       //Semplificazione IDE che consiste nel tornare true se response != "" ossia se la risposta della query è null

     }
}

