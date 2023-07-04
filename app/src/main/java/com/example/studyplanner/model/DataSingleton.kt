package com.example.studyplanner.model

class DataSingleton private constructor() {
    companion object {
        private var istanza: DataSingleton? = null

        fun ottieniIstanza(): DataSingleton {
            if (istanza == null) {
                istanza = DataSingleton()
            }
            return istanza as DataSingleton
        }
    }

    fun reset(){
        nomeUtente=" "
        password=" "
        domandaS=""
        rispostaS=""
        nome=""
        cognome=""
        foto=""
        dataNascita=""
        universita=""
        idCorso=-1
    }


        var nomeUtente: String? = ""
        var password: String? = ""
        var domandaS: String? = ""
        var rispostaS: String? = ""
        var nome: String? = ""
        var cognome: String? = ""
        var foto: String? = ""
        var dataNascita: String? = ""
        var universita: String? = ""
        var idCorso: Int = -1

}