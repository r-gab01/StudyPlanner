package com.example.studyplanner.viewModel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import com.example.studyplanner.BuildConfig
import com.example.studyplanner.R
import com.example.studyplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var loggedIn: Boolean = false            //variabile che mi permette di verificare se l'utente è loggato o meno
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        if (BuildConfig.DEBUG) {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build()
            StrictMode.setThreadPolicy(policy)
        }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!loggedIn){                 //se l'utente non è loggato, lancio la schermata di login
            login()
        }
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
                    if (!fragmentExist(statsTag)) {  //verifico se già il fragment è stato aperto tramite questa funzione definita sotto
                        transaction.replace(R.id.fragmentContainerView, StatFragment(), statsTag)
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
                        transaction.replace(R.id.fragmentContainerView, ProfileFragment(), profileTag)
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

    override fun onBackPressed() {
        val manager= supportFragmentManager
        val numEntryInBackStack = manager.backStackEntryCount //numero di voci nello stack
        if(numEntryInBackStack>0){
            for(i in 0 until numEntryInBackStack){
                manager.popBackStack() //Facciamo il pop di tutte le voci nel back stack
            }
        } else {
            super.onBackPressed(); //Ossia se Se il BackStack è vuoto, viene chiamato il metodo super.onBackPressed()
            // per eseguire l'azione di default, ovvero uscire dall'Activity.
        }
    }


        private fun login(){
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState) //Chiamo l'implementazione del metodo nella classe base per eseguire le operazioni di salvataggio dello stato di base.
        // Salvo lo stato del Fragment corrente
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        if (currentFragment != null) {
            supportFragmentManager.putFragment(outState, "currentFragment", currentFragment) //Se il fragment corrente non è nullo, utilizza il putFragment del FragmentManager per salvare il fragment nel Bundle
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState) //ripristino dello stato
        // Ripristina lo stato del Fragment corrente
        val currentFragment = supportFragmentManager.getFragment(savedInstanceState, "currentFragment")
        if (currentFragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, currentFragment) // sostituisco il contenuto del containerView_login con il fragment ripristinato.
                .commit()
        }
    }
}