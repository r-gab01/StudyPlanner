package com.example.studyplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.studyplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var loggedIn: Boolean = true            //variabile che mi permette di verificare se l'utente è loggato o meno
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!loggedIn){                 //se l'utente non è loggato, lancio la schermata di login
            login()
        }

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(R.id.fragmentContainerView, CalendarFragment())
        transaction.commit()


    }


    private fun login(){

    }
}