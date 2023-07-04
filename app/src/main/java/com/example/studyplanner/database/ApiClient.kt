package com.example.studyplanner.database

import android.util.Log
import com.example.studyplanner.model.AccountDBModel
import com.example.studyplanner.model.CorsoStudioDBModel
import com.example.studyplanner.model.StudenteDBModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




object ApiClient {

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/webmobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService = retrofit.create(ApiInterface::class.java)
    val gson = Gson()       //per serializzare e deserializzare l'oggetto JSON nella data Class che mi serve

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

    fun login(nome: String, password: String, callback: (AccountDBModel?, Throwable?) -> Unit) {        //sfrutto callback per gestire metodo post asincrono
        var data: AccountDBModel?   //scelgo la data class con cui voglio restituiti i dati
        val query = "select * from autenticazione where nome_u_ref = '${nome}' and password = '${password}';"
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        val result = res.get(0).asJsonObject                            //result è un jsonObject
                        data = gson.fromJson(result, AccountDBModel::class.java)        //deserializzo l'oggetto nella classe selezionata
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

    fun recuperoPW(nome: String, domanda: String, risposta: String, callback: (AccountDBModel?, Throwable?) -> Unit) {
        var data: AccountDBModel?
        val query = "select * " +
                "from autenticazione" +
                " where nome_u_ref = '${nome}' and domanda_s = '${domanda}' and recupero_s = '${risposta}';"
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

    fun selectCorsoStudio( callback: (List<CorsoStudioDBModel?>?, Throwable?) -> Unit) {        //sfrutto callback per gestire metodo post asincrono
        var data = ArrayList<CorsoStudioDBModel?>() //scelgo la data class con cui voglio restituiti i dati
        val query = "select * from corso_di_studio;"
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        for (i in 0 until res.size()) {
                            val result = res.get(i).asJsonObject                            //result è un jsonObject
                            data.add(gson.fromJson(result, CorsoStudioDBModel::class.java))        //deserializzo l'oggetto nella classe selezionata
                        }
                        Log.d("APICLIENT", data.toString())
                        callback(data.toList(), null)
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

    fun selectStudente(nomeU: String?, callback: (StudenteDBModel?, Throwable?) -> Unit){        //sfrutto callback per gestire metodo post asincrono
        var data: StudenteDBModel?   //scelgo la data class con cui voglio restituiti i dati
        val query = "select * from studente where nome_utente = '${nomeU}';"
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        val result = res.get(0).asJsonObject                            //result è un jsonObject
                        data = gson.fromJson(result, StudenteDBModel::class.java)        //deserializzo l'oggetto nella classe selezionata
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
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(null, error)
            }
        })
    }


    //Funzione per fare l'update della password
    fun updatePass(nomeU: String?, newPass:String?, callback: (Boolean?, Throwable?) -> Unit){        //sfrutto callback per gestire metodo post asincrono
        val query = "update autenticazione a set a.password = '${newPass}' where a.nome_u_ref = '${nomeU}';"
        apiService.update(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("APICLIENT", res.toString())
                    callback(true, null)
                } else {
                    Log.e("APLICLIENT", response.message())
                    Log.e("APLICLIENT", response.body().toString())
                    val error = Exception("La chiamata API non è stata eseguita correttamente.")
                    callback(false, error) // Nessun risultato trovato
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("OnFailure", "${t.message}")
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(false, error) // Nessun risultato trovato
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