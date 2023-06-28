package com.example.studyplanner.database

import android.util.Log
import com.example.studyplanner.model.MyJsonArray
import com.example.studyplanner.model.MyJsonObject
import com.example.studyplanner.model.SharedData


object BoundaryDBMS {

    fun login( nome: String, password: String) {
        val query = "select * from autenticazione where nome_u_ref = '${nome}' and password = '${password}';"

        ClientNetwork.selectValue(query) { result, error ->
            if (error != null) {
                // Gestisci l'errore
                Log.e("DB", "Errore nella chiamata: ${error.message}")
            } else {
                // Utilizza il JsonObject risultante
                if (result != null) {
                    // Esegui le operazioni necessarie con il result
                    Log.d("BOUNDARYDB", result.toString())
                    Log.d("BOUNDARYDB", result.size().toString())
                } else {
                    // Nessun result restituito
                    Log.d("BOUNDARYDB", "Nessun risultato ottenuto.")
                }
            }
        }
    }

}

