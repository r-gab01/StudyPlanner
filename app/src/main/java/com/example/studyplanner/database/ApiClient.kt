package com.example.studyplanner.database

import android.util.Log
import com.example.studyplanner.model.AccountDBModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.javaType
import kotlin.reflect.typeOf
import kotlinx.coroutines.*



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
    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> login(nome: String, password: String, crossinline callback: (T?, Throwable?) -> Unit) {
        var data: T?
        val query = "select * from autenticazione where nome_u_ref = '${nome}' and password = '${password}';"
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        val result = res.get(0).asJsonObject
                        val typeT = typeOf<T>().javaType
                        data = gson.fromJson(result, typeT)
                        Log.d("APICLIENT", data.toString())
                        callback(data, null)
                    } else {
                        callback(null, null) // Nessun risultato trovato
                    }
                } else {
                    val error = Exception("La chiamata API non è stata eseguita correttamente.")
                    callback(null, error)
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("OnFailure", "${t.message}")
            }
        })
    }

     */

    fun login(nome: String, password: String, callback: (AccountDBModel?, Throwable?) -> Unit) {
        var data: AccountDBModel?
        val query = "select * from autenticazione where nome_u_ref = '${nome}' and password = '${password}';"
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        val result = res.get(0).asJsonObject
                        data = gson.fromJson(result, AccountDBModel::class.java)
                        Log.d("APICLIENT", data.toString())
                        callback(data, null)
                    } else {
                        callback(null, null) // Nessun risultato trovato
                    }
                } else {
                    val error = Exception("La chiamata API non è stata eseguita correttamente.")
                    callback(null, error)
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("OnFailure", "${t.message}")
            }
        })
    }

    /*
    fun select(query: String) {
        apiService.select(query).enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("APICLIENT","Errore nella chiamata: ${t.message}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size()>0 ){
                        val risultato = res.get(0).asJsonObject
                        //risultato contiene la prima tupla
                        Log.e("APICLIENT", risultato.toString())

                    } else{
                        //La query non restituisce tuple
                        Log.e("APICLIENT", "Dati errati")
                    }
                }
            }
        })
    }

    val accountData: MutableLiveData<AccountDBModel> = MutableLiveData()
    fun login(query: String) {
        apiService.login(query).enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("APICLIENT","Errore nella chiamata: ${t.message}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size()>0 ){
                        val risultato = res.get(0).asJsonObject
                        accountData.value = gson.fromJson(risultato, AccountDBModel::class.java)
                        Log.d("APICLIENT", accountData.value.toString())

                    } else{
                        accountData.value = null
                        Log.e("APICLIENT", "Dati errati")
                        Log.e("APICLIENT", accountData.value.toString())
                    }
                }
            }
        })
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T> selectValue(query: String) : T? {
        var data: T? = null
        apiService.select(query).enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("CLIENTNETWORK","Errore nella chiamata: ${t.message}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size()>0 ){

                        val risultato = res.get(0).asJsonObject
                        val typeT = typeOf<T>().javaType

                        data = gson.fromJson(risultato, typeT)
                        Log.d("APICLIENT", data.toString())

                    } else{
                        data = null
                        Log.e("APICLIENT", "Opra")
                        Log.e("APICLIENT", data.toString())
                    }
                }
            }
        })
        Log.d("APICLIENT", data.toString())
        return data
    }
     */



}