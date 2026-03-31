package com.example.studyplanner.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import androidx.core.content.ContextCompat
import com.example.studyplanner.R
import com.example.studyplanner.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //La condizione if (savedInstanceState == null) controlla se il parametro savedInstanceState è nullo.
        // Questo parametro contiene lo stato precedentemente salvato dell'Activity durante un'eventuale distruzione e ricreazione,
        // ad esempio durante una rotazione del dispositivo.

        //Se savedInstanceState è nullo, significa che l'Activity viene creata per la prima volta e non ci sono dati di stato precedenti
        // da ripristinare. In questo caso, viene eseguito il blocco di codice all'interno delle parentesi graffe.

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.containerView_login, LoginFragment(), "LoginFragment")
                .commit()
        }

        var sharedPreferences= this.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        var loggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

        if (loggedIn){                 //se l'utente non è loggato, lancio la schermata di login
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(R.anim.scale_in_animation,R.anim.scale_out_animation)
        }

    }

    //Il metodo onSaveInstanceState viene chiamato prima che l'activity venga distrutta, ad esempio durante un cambiamento di configurazione come la rotazione dello schermo.
    // È un punto in cui puoi salvare lo stato corrente dell'activity o dei suoi componenti, come i fragment, in un oggetto Bundle.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState) //Chiamo l'implementazione del metodo nella classe base per eseguire le operazioni di salvataggio dello stato di base.
        // Salvo lo stato del Fragment corrente
        val currentFragment = supportFragmentManager.findFragmentById(R.id.containerView_login)
        if (currentFragment != null) {
            supportFragmentManager.putFragment(outState, "currentFragment", currentFragment) //Se il fragment corrente non è nullo, utilizza il putFragment del FragmentManager per salvare il fragment nel Bundle
        }
    }

    //Il metodo onRestoreInstanceState viene chiamato dopo che l'activity è stata ricreata a seguito di un cambio di configurazione o di un ripristino dello stato.
    // Questo metodo ti consente di ripristinare lo stato salvato in precedenza.
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState) //ripristino dello stato
        // Ripristina lo stato del Fragment corrente
        val currentFragment = supportFragmentManager.getFragment(savedInstanceState, "currentFragment")
        if (currentFragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerView_login, currentFragment) // sostituisco il contenuto del containerView_login con il fragment ripristinato.
                .commit()
        }
    }
}