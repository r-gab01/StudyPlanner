package com.example.studyplanner.database

import android.provider.ContactsContract.RawContacts.Data
import android.util.Log
import com.example.studyplanner.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Date


object ApiClient {

    val gson = GsonBuilder()  //per serializzare e deserializzare l'oggetto JSON nella data Class che mi serve
        .setDateFormat("yyyy-MM-dd")
        .create()

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/webmobile/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService = retrofit.create(ApiInterface::class.java)
   // val gson = Gson()




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
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(null, error)
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
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(null, error)
            }
        })
    }

    fun selectCorsoStudio( callback: (List<CorsoStudioDBModel?>?, Throwable?) -> Unit) {
        var data = ArrayList<CorsoStudioDBModel?>()
        val query = "select * from corso_di_studio;"
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        for (i in 0 until res.size()) {
                            val result = res.get(i).asJsonObject
                            data.add(gson.fromJson(result, CorsoStudioDBModel::class.java))
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
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(null, error)
            }
        })
    }

    fun verificaNomeUtente(nomeUtente: String, callback: (AccountDBModel?, Throwable?) -> Unit) {
        var data: AccountDBModel?   //scelgo la data class con cui voglio restituiti i dati
        val query = "select * from autenticazione where nome_u_ref = '${nomeUtente}' ;"
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
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(null, error)
            }
        })
    }
    //Metodo di INSERT
    fun registraStudente(nomeUtente: String, universita: String, corsoStudi: Int?,
                         pass: String?, domSic: String?, rispSic: String?,
                         callback: (Boolean?, Throwable?) -> Unit) {      //sfrutto la callback con un booleano per ottenere conferma sull'inserimento avvenuto
        val query = "insert into `studente` values('$nomeUtente', null, null, null, null,'$universita', '$corsoStudi');" +
                "insert into `autenticazione` values('$nomeUtente', '$pass', '$domSic', '$rispSic');"
        apiService.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("APICLIENT", res.toString())
                    callback(true, null)
                } else{
                    Log.e("APICLIENT", response.message())
                    Log.e("APICLIENT", response.body().toString())
                    val error = Exception("La chiamata API non è stata eseguita correttamente.")
                    callback(false , error)
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("OnFailure", "${t.message}")
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(false, error)
            }
        })
    }

    fun selectMaterieCorso(idCorso: Int?, callback: (List<MateriaDBModel?>?, Throwable?) -> Unit) {
        var data = ArrayList<MateriaDBModel?>()
        val query = "select * from `materia` where id_c_ref='$idCorso';"
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        for (i in 0 until res.size()) {
                            val result = res.get(i).asJsonObject
                            data.add(gson.fromJson(result, MateriaDBModel::class.java))
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
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(null, error)
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

    fun selectCorso(idCorso: Int?, callback: (CorsoStudioDBModel?, Throwable?) -> Unit){        //sfrutto callback per gestire metodo post asincrono
        var data: CorsoStudioDBModel?   //scelgo la data class con cui voglio restituiti i dati
        val query = "select * from corso_di_studio where id_corso = '${idCorso}';"
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        val result = res.get(0).asJsonObject                            //result è un jsonObject
                        data = gson.fromJson(result, CorsoStudioDBModel::class.java)        //deserializzo l'oggetto nella classe selezionata
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

    fun updateStudente(newNome: String?,newCognome: String?, newUni: String?, newCorso: Int?, nomeU: String?, callback: (Boolean?, Throwable?) -> Unit){        //sfrutto callback per gestire metodo post asincrono
        val query = "update studente s set s.nome = '${newNome}', s.cognome= '${newCognome}', s.universita='${newUni}' , s.id_c_ref= '${newCorso}' where s.nome_utente= '${nomeU}';"
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


    fun registraAccount(nomeUtente: String, pass: String?, domSic: String?, rispSic: String?,
                        callback: (Boolean?, Throwable?) -> Unit) {
        val query = "insert into `autenticazione` values('$nomeUtente', '$pass', '$domSic', '$rispSic');"
        apiService.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("APICLIENT", res.toString())
                    callback(true, null)
                } else{
                    Log.e("APICLIENT", response.message())
                    val error = Exception("Dati non inseriti correttamente nel Database")
                    callback(false , error)
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("OnFailure", "${t.message}")
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(false, error)
            }
        })
    }

    fun selectSessioneStudio(nomeU: String?, dataA: String? , callback: (List<SessioneStudioDBModel?>?, Throwable?) -> Unit){        //sfrutto callback per gestire metodo post asincrono
        var data= ArrayList<SessioneStudioDBModel?>()   //scelgo la data class con cui voglio restituiti i dati
        val query = "select * from sessione_studio where nome_u_ref = '${nomeU}' and data_appello = '${dataA}';"
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        for (i in 0 until res.size()) {
                            val result = res.get(i).asJsonObject
                            data.add(gson.fromJson(result, SessioneStudioDBModel::class.java))
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
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(null, error)
            }
        })
    }

    fun selectCarriera(nomeU: String?,  callback: (List<CarrieraDBModel?>?, Throwable?) -> Unit){        //sfrutto callback per gestire metodo post asincrono
        var data= ArrayList<CarrieraDBModel?>()   //scelgo la data class con cui voglio restituiti i dati
        val query = "select distinct  c.nome_m_ref, c.voto, m.cfu  from carriera c INNER JOIN materia m where c.nome_u_ref = '${nomeU}'and c.nome_m_ref=m.nome_m and c.id_c_ref=m.id_c_ref ; ;"
        Log.d("APICLIENT", query)
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        for (i in 0 until res.size()) {
                            val result = res.get(i).asJsonObject
                            data.add(gson.fromJson(result, CarrieraDBModel::class.java))
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
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(null, error)
            }
        })
    }


    /*
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

     */



}