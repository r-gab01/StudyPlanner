package com.example.studyplanner.database

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.javaType
import kotlin.reflect.typeOf


object ApiClient {

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/webmobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService = retrofit.create(ApiInterface::class.java)
    val gson = Gson()

    /*
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

     */

    val data: MutableLiveData<Any> = MutableLiveData()
    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> selectValue(query: String) {
        apiService.selectValue(query).enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("CLIENTNETWORK","Errore nella chiamata: ${t.message}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size()>0 ){
                        val risultato = res.get(0).asJsonObject
                        val typeT = typeOf<T>().javaType
                        data.value = gson.fromJson(risultato, typeT::class.java)
                        Log.d("CLIENTNETWORK", data.value.toString())

                    } else{
                        data.value = null
                        Log.e("CLIENTNETWORK", "Opra")
                        Log.e("CLIENTNETWORK", data.value.toString())
                    }
                }
            }
        })
    }



}