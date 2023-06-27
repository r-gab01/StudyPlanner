package com.example.studyplanner.model

class SharedData {
    companion object {
        var nomeUtente: String = ""
        var password: String = ""
        var correctLogin: Boolean = false

        fun getLogin(): Boolean {
            return correctLogin
        }
    }
}