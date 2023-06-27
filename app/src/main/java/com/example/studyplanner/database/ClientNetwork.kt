package com.example.studyplanner.database

import android.util.Log
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

    fun select(query: String) : JsonArray {
        var jsonResponse = JsonArray()
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.d("SELECT", response.body()?.get("queryset").toString())
                    jsonResponse = response.body()?.get("queryset") as JsonArray

                } else {
                    Log.e("DB","Errore nella risposta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("DB","Errore nella chiamata: ${t.message}")
            }
        })
        return jsonResponse
    }


}