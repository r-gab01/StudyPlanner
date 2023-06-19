package com.example.studyplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ciao = "ciao"
        println(ciao)
        val prova= "prova"

        setContentView(R.layout.activity_main)
    }
}