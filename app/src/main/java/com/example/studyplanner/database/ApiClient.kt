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

    fun recuperoPW(nome: String, risposta: String, domanda: String, callback: (AccountDBModel?, Throwable?) -> Unit) {
        var data: AccountDBModel?
        val query = "select * " +
                "from autenticazione" +
                " where nome_u_ref = '${nome}' and domanda_s = '${domanda}' and risposta_s = '${risposta}';"
        Log.d("recuperoPW", query)
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
        val data = ArrayList<CorsoStudioDBModel?>()
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
        val data = ArrayList<MateriaDBModel?>()
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

    fun insInSessione(idSessione: Int?, nomeUtente: String, nomeMateria: String?, idCorso: Int?, dataAppello: String?,
                      fonteStudio: String?, pagineTot: Int?, pagineStudiate: Int?,
               callback: (Boolean?, Throwable?) -> Unit) {      //sfrutto la callback con un booleano per ottenere conferma sull'inserimento avvenuto
        val query =
            "insert into `sessione_studio` values('$idSessione','$nomeUtente', '$nomeMateria', '$idCorso', '$dataAppello', '$fonteStudio', '$pagineTot', '$pagineStudiate');"
        Log.d("insSessione", query)
        apiService.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("APICLIENT", res.toString())
                    callback(true, null)
                } else{
                    Log.e("APICLIENT", response.message())
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

    fun insArg(idRiferimento: Int, argomento: String?,
               callback: (Boolean?, Throwable?) -> Unit) {      //sfrutto la callback con un booleano per ottenere conferma sull'inserimento avvenuto
        val query = "insert into `argomento`(`id_sess_ref`,`argomento`) values('$idRiferimento', '$argomento');"
        Log.d("InsArg",query)
        apiService.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("APICLIENT", res.toString())
                    callback(true, null)
                } else{
                    Log.e("InsArg", response.message())
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

    fun selectEsamiSessione(callback: (List<SessioneStudioDBModel?>?, Throwable?) -> Unit) {
        val data = ArrayList<SessioneStudioDBModel?>()
        val query = "select * from `sessione_studio` where nome_u_ref='${DataSingleton.ottieniIstanza().nomeUtente}';"
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

    fun updatePagineStud(pagineStud: Int, idSessioneMateria: Int?,
                      callback: (Boolean?, Throwable?) -> Unit) {      //sfrutto la callback con un booleano per ottenere conferma sull'inserimento avvenuto
        val query = "UPDATE `sessione_studio` " +
                 "SET pagine_stud = pagine_stud + $pagineStud " +
        "WHERE id_sessione = '${idSessioneMateria}';"
        Log.d("InsArg",query)
        apiService.update(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("APICLIENT", res.toString())
                    callback(true, null)
                } else{
                    Log.e("InsArg", response.message())
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

    fun selectArgomenti(idSessione:Int?, callback: (List<ArgomentoDBModel?>?, Throwable?) -> Unit) {
        var data = ArrayList<ArgomentoDBModel?>()
        val query = "select * from argomento where id_sess_ref='$idSessione';"
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        for (i in 0 until res.size()) {
                            val result = res.get(i).asJsonObject
                            data.add(gson.fromJson(result, ArgomentoDBModel::class.java))
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

    fun selectMateria(nomeMateria: String?, callback: (MateriaDBModel?, Throwable?) -> Unit) {
        var data: MateriaDBModel?
        val query = "select * from materia where id_c_ref= '${DataSingleton.ottieniIstanza().idCorso}' and " +
                "nome_m = '$nomeMateria';"
        Log.d("SelectMateria",query)
        apiService.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()?.getAsJsonArray("queryset")
                    if (res != null && res.size() > 0) {
                        val result = res.get(0).asJsonObject
                        data = gson.fromJson(result, MateriaDBModel::class.java)
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

    fun updateCarriera(nomeMateria: String?, voto: Int?, lode: Boolean,
                        callback: (Boolean?, Throwable?) -> Unit) {
        val lodeIns: Int
        if (lode){
            lodeIns = 1
        } else{
            lodeIns = 0
        }
        val query = "update `carriera` set voto=${voto}, lode='$lodeIns' " +
                "where nome_u_ref='${DataSingleton.ottieniIstanza().nomeUtente}' and nome_m_ref ='$nomeMateria';"
        Log.d("ApiClientCompletaEsame", query)
        apiService.update(query).enqueue(object : Callback<JsonObject> {
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

    fun rimuoviEsame(idSessione: Int?, callback: (Boolean?, Throwable?) -> Unit) {
        val query = "delete from argomento where id_sess_ref='$idSessione'; " +
                "delete from sessione_studio where id_sessione='$idSessione';"
        Log.d("ApiClientCompletaEsame", query)
        apiService.remove(query).enqueue(object : Callback<JsonObject> {
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

    fun selectCarriera(nomeU: String?,  callback: (List<CarrieraDBModel?>?, Throwable?) -> Unit){        //sfrutto callback per gestire metodo post asincrono
        var data= ArrayList<CarrieraDBModel?>()   //scelgo la data class con cui voglio restituiti i dati
        val query = " Select nome_u_ref, nome_m_ref, c.id_c_ref, voto, lode, cfu FROM carriera c, materia m WHERE c.nome_m_ref=nome_m and c.id_c_ref=m.id_c_ref and c.nome_u_ref='${nomeU}';"
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

    fun deletCarriera(nomeU:String?, idC: Int?, callback: (Boolean?, Throwable?) -> Unit) {
        val query = "delete from carriera c  where c.nome_u_ref= '${nomeU}' and c.id_c_ref= '${idC}';"
        apiService.remove(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("APICLIENT", res.toString())
                    callback(true, null)
                } else {
                    Log.e("APICLIENT", response.message())
                    val error = Exception("Dati non eliminati correttamente nel Database")
                    callback(false, error)
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("OnFailure", "${t.message}")
                val error = Exception("La chiamata API non è stata eseguita correttamente.")
                callback(null, error)
            }
        })
    }

    fun insCarriera(nomeU: String?, nomeM: String?, idCorso: Int?, voto: Int?, lode: Int, callback: (Boolean?, Throwable?) -> Unit) {      //sfrutto la callback con un booleano per ottenere conferma sull'inserimento avvenuto
        val query = "insert into `carriera` values('$nomeU', '$nomeM' , '${idCorso}', '${voto}', '${lode}');"
        Log.d("InsCarriera",query)
        apiService.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d("APICLIENT", res.toString())
                    callback(true, null)
                } else{
                    Log.e("APICLIENT", response.message())
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