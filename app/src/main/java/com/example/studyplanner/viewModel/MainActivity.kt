package com.example.studyplanner.viewModel

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.studyplanner.BuildConfig
import com.example.studyplanner.ProfileFragment
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.ActivityMainBinding
import com.example.studyplanner.model.DataSingleton


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentManager: FragmentManager

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

        fragmentManager = supportFragmentManager

        var sharedPreferences= this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        var loggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

        if(loggedIn){
            val nomeU: String? = sharedPreferences.getString("Nome Utente", "")
            val singleton= DataSingleton.ottieniIstanza()
            singleton.nomeUtente=nomeU
            Log.d("CALENDAR", "Dati ricevuti: $nomeU")

            ApiClient.selectStudente(nomeU){ data, error ->
                if (error != null) {
                    // Gestisci l'errore
                    Log.e("MAINACTIVITY", "Si è verificato un errore: $error")
                }else if (data != null) {
                    // Utilizza i dati restituiti
                    Log.d("MAINACTIVITY", "Dati ricevuti: $data")
                    //salvo i dati dello studente nel Sigleton
                    val singleton= DataSingleton.ottieniIstanza()
                    singleton.nome=data.nome
                    singleton.cognome=data.cognome
                    singleton.universita=data.universita
                    singleton.foto=data.foto
                    singleton.dataNascita=data.dataNascita
                    selectCorso(data.idCorso)
                }else{
                    Log.d("SELECTSTUDENTE", "Dati ricevuti: $data")
                }
            }
        }

     //   val nomeU: String? = sharedPreferences.getString("Nome Utente", "")
      // val singleton= DataSingleton.ottieniIstanza()
       // singleton.nomeUtente=nomeU


      /*  ApiClient.selectStudente(nomeU){ data, error ->
            if (error != null) {
                // Gestisci l'errore
                Log.e("MAINACTIVITY", "Si è verificato un errore: $error")
            }else if (data != null) {
                // Utilizza i dati restituiti
                Log.d("MAINACTIVITY", "Dati ricevuti: $data")
                //salvo i dati dello studente nel Sigleton
                singleton.nome=data.nome
                singleton.cognome=data.cognome
                singleton.universita=data.universita
                singleton.foto=data.foto
                singleton.dataNascita=data.dataNascita
                selectCorso(data.idCorso)
            }else{
                Log.d("SELECTSTUDENTE", "Dati ricevuti: $data")
            }
        } */



        val calendarTag = "CalendarFragment"
        //Di default avvio il fragment del calendario
       if (savedInstanceState == null) {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()

            transaction.add(R.id.fragmentContainerView, CalendarFragment(), calendarTag)
            transaction.commit()
        }


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


    fun selectCorso(idCorso: Int?) {
        //query per fare la select del corso di studi
        ApiClient.selectCorso(idCorso) { data, error ->
            if (error != null) {
                // Gestisci l'errore
                Log.e("CORSO", "Si è verificato un errore: $error")
            } else if (data != null) {
                // Utilizza i dati restituiti
                Log.d("MAINACTIVITY", "Dati ricevuti: $data")
                //salvo il corso nel Singleton
                val singleton = DataSingleton.ottieniIstanza()
                singleton.corsoStudi = data.nomeCorso
                singleton.idCorso=data.idCorso
            } else {
                Log.d("CORSO", "Dati ricevuti: $data")
            }
        }
    }

    private fun printBackStack(showFragments: String, numEntryInBackStack: Int) {
        Log.d("CALENDAR", "Dati ricevuti: $showFragments")
        Log.d("CALENDAR", "Numero di voci nello stack di ritorno: $numEntryInBackStack")
    }
}