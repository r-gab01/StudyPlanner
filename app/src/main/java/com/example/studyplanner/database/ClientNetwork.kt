package com.example.studyplanner.database

import android.util.Log
import com.example.studyplanner.model.MyJsonArray
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ClientNetwork {

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/webmobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService = retrofit.create(UserAPI::class.java)

    fun selectValue(query: String, callback: (JsonObject?, Throwable?) -> Unit) {
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        val result = res.get(0).asJsonObject
                        callback(result, null)
                    } else {
                        callback(null, null) // Nessun risultato trovato
                    }
                } else {
                    val error = Exception("La chiamata API non è stata eseguita correttamente.")
                    callback(null, error)
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("OnFailure", "error")
            }
        })
    }


    /*
    fun select(query: String) {
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val response = response.body()?.getAsJsonArray("queryset")
                    if (response != null){
                        MyJsonArray.set(response, response.size())
                        Log.d("SELECT", response?.get(0).toString())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("DB","Errore nella chiamata: ${t.message}")
            }
        })
    }

     */


}