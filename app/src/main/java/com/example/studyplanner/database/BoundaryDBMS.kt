package com.example.studyplanner.database

import android.view.View
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoundaryDBMS {

    private fun loginUtente ( nome: String, password: String ){

        val query = "select * from persona where username = '${nome}' and password = '${password}';"

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            response.body()?.get("")
                            //getImageProfilo((response.body()?.get("queryset") as JsonArray).get(0) as JsonObject)
                        } else {
                            //Toast.makeText(
                              //  this@LoginActivity,
                               // "credenziali errate",
                               // Toast.LENGTH_LONG
                            //).show()
                            //binding.progressBar.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    /*
                     * gestisci qui il fallimento della richiesta
                     */

                }
            }
        )


    }
}