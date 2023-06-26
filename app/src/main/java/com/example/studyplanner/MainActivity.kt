package com.example.studyplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.studyplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //private var loggedIn: Boolean = false            //variabile che mi permette di verificare se l'utente è loggato o meno
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

      //  if (!loggedIn){                 //se l'utente non è loggato, lancio la schermata di login
            login()
        //}
        //Di default avvio il fragment del calendario
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val calendarTag = "CalendarFragment"
        transaction.add(R.id.fragmentContainerView, CalendarFragment(), calendarTag)
        transaction.commit()

        binding.bottomBar.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.calendar_button -> {
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    if (!fragmentExist(calendarTag)) {  //verifico se già il fragment è stato aperto tramite questa funzione definita sotto
                        transaction.replace(R.id.fragmentContainerView, CalendarFragment(), calendarTag)
                        transaction.commit()
                    }
                    true
                }
                R.id.exams_button ->{
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    val examsTag = "ExamsFragment"
                    if (!fragmentExist(examsTag)) {  //verifico se già il fragment è stato aperto tramite questa funzione definita sotto
                        transaction.replace(R.id.fragmentContainerView, ExamsFragment(), examsTag)
                        transaction.commit()
                    }
                    true
                }
                R.id.stats_button ->{
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    val statsTag = "StatsFragment"
                    if (!fragmentExist(calendarTag)) {  //verifico se già il fragment è stato aperto tramite questa funzione definita sotto
                        transaction.replace(R.id.fragmentContainerView, CalendarFragment(), statsTag)
                        transaction.commit()
                    }
                    true
                }
                R.id.studio_button ->{
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    val studyTag = "studioFragment"
                    if (!fragmentExist(studyTag)) {  //verifico se già il fragment è stato aperto tramite questa funzione definita sotto
                        transaction.replace(R.id.fragmentContainerView, StudyFragment(), studyTag)
                        transaction.commit()
                    }
                    true
                }
                R.id.profile_button ->{
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    val profileTag = "ProfileFragment"
                    if (!fragmentExist(profileTag)) {  //verifico se già il fragment è stato aperto tramite questa funzione definita sotto
                        transaction.replace(R.id.fragmentContainerView, CalendarFragment(), profileTag)
                        transaction.commit()
                    }
                    true
                }
                else ->false
            }
        }

    }

    fun fragmentExist(tag: String): Boolean {       //funzione che mi permette di trovare se un fragment è presente tramite il uso tag
        val fragmentManager = supportFragmentManager
        return (fragmentManager.findFragmentByTag(tag) != null)         //scrittura compatta che restituisce true o false se quella condizione si verifica o meno
    }


        private fun login(){
            val i = Intent(this, LoginActivity:: class.java)
            startActivity(i)
        }

}