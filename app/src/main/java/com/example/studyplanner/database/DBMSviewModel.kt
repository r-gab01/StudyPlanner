package com.example.studyplanner.database

import androidx.lifecycle.MutableLiveData


object DBMSviewModel {

    /*
    fun login( nome: String, password: String): AccountModel {
        val query = "select * from autenticazione where nome_u_ref = '${nome}' and password = '${password}';"
        var account = ClientNetwork.login(query)
        return account
    }
     */

    fun login( nome: String, password: String) {
        val data: MutableLiveData<Any> = MutableLiveData()
        val query = "select * from autenticazione where nome_u_ref = '${nome}' and password = '${password}';"
        ApiClient.selectValue<AccountDBModel>(query)

    }
}

