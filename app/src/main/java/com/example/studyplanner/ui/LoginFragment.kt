package com.example.studyplanner.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.studyplanner.R
import com.example.studyplanner.model.AccountDBModel
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentLoginBinding
import com.example.studyplanner.model.DataSingleton
import kotlin.math.sin

class LoginFragment : Fragment(){

    private lateinit var binding : FragmentLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var passwordVisible = true
    private lateinit var eyeIcon: Drawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding= FragmentLoginBinding.inflate(inflater)

        var eyeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_remove_red_eye_24)!!

        // Aggiungo il seguente codice per impostare l'OnClickListener sull'icona dell'occhio
        binding.EditTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
            ContextCompat.getDrawable(requireContext(), R.drawable.baseline_lock_24),
            null,
            eyeIcon,
            null
        )
        binding.EditTextPassword.setOnTouchListener { _, event ->
            val drawableEnd = 2 // L'indice 2 corrisponde all'icona a destra nella vista.
            // Verifica se l'area di tocco corrisponde all'icona dell'occhio.
            val isDrawableClicked =
                event.x >= (binding.EditTextPassword.width - binding.EditTextPassword.paddingEnd - binding.EditTextPassword.compoundDrawables[drawableEnd].bounds.width()) && event.action == MotionEvent.ACTION_UP
            //Controllo se la coordinata X dell'evento di tocco è maggiore o uguale alla larghezza dell'EditTextPassword meno il padding a destra meno la larghezza dell'icona dell'occhio.
            //event.action == MotionEvent.ACTION_UP - In questo modo verifico se l'azione del tocco è un rilascio del dito (evento ACTION_UP).
            if (isDrawableClicked) {
                if (event.action == MotionEvent.ACTION_UP) {
                    PasswordVisibility()
                }
                true
            } else {
                false
            }
        }

        binding.textViewPasswordDimenticata.setOnClickListener{
             val manager=parentFragmentManager
            if(!fragmentExists(manager, "RecuperoFragment")) {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.containerView_login, RecuperoPassFragment(), "RecuperoFragment")
                transaction.addToBackStack("RecuperoFragment")
                transaction.commit()
            }
        }

        binding.textRegistrazione.setOnClickListener{
            val manager=parentFragmentManager
            if(!fragmentExists(manager, "RegistrazioneFragment")) {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.containerView_login, RegistrazioneFragment(), "RegistrazioneFragment")
                transaction.addToBackStack("RegistrazioneFragment")
                transaction.commit()
            }
        }

        //Gestisco il salvataggio dei dati quando premo la checkBox e vado avanti con il bottone Login
        //Utilizzo un file delle preferenze condivise. Lo gestisco nella funzione saveLoginData()
        var tastoLogin= binding.buttonLogin
        var rememberMeCheckBox= binding.checkBox

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Verifico se l'utente ha già effettuato l'accesso in precedenza
     /*var isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val savedUsername = sharedPreferences.getString("Nome Utente", "")
            val savedPassword = sharedPreferences.getString("password", "")

            binding.EditTextNomeUtente.setText(savedUsername)
            binding.EditTextPassword.setText(savedPassword)
            rememberMeCheckBox.isChecked = true
        }*/

        tastoLogin.setOnClickListener {
            val nomeInserito = binding.EditTextNomeUtente.text.toString().trim()
            val pwInserita = binding.EditTextPassword.text.toString().trim()
            if (nomeInserito.isEmpty())
                binding.EditTextNomeUtente.setBackgroundResource(R.drawable.error_border_element)
            if (pwInserita.isEmpty())
                binding.EditTextPassword.setBackgroundResource(R.drawable.error_border_element)
            else {
                ApiClient.login(nomeInserito,pwInserita) { data, error ->
                    if (error != null) {
                        // Gestisci l'errore
                        Log.e("LOGINFRAGMENT", "Si è verificato un errore: $error")
                    } else if (data != null) {
                        // Utilizza i dati restituiti
                        //Intanto salvo alcuni dati nel singleton che mi potranno servire dopo
                        val singleton= DataSingleton.ottieniIstanza()
                        singleton.domandaS=data.domandaS
                        singleton.rispostaS=data.rispostaS
                        Log.d("LOGINFRAGMENT", "Dati ricevuti: $data")
                        ApiClient.selectStudente(nomeInserito){data,error ->
                            if (error != null) {
                                // Gestisci l'errore
                                Log.e("SELECTSTUDENTE", "Si è verificato un errore: $error")
                            }else if (data != null) {
                                // Utilizza i dati restituiti
                                Log.d("SELECTSTUDENTE", "Dati ricevuti: $data")
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
                        }
                        if (rememberMeCheckBox.isChecked) { //Solo se la check box è stata checkata
                            saveLoginData(nomeInserito,pwInserita)
                            saveRoutine()
                        }else{
                            saveData(nomeInserito,pwInserita)
                        }
                        requireActivity().finish()
                        val i = Intent(requireContext(), MainActivity()::class.java)
                        startActivity(i)
                        activity?.overridePendingTransition(R.anim.scale_in_animation,R.anim.scale_out_animation)
                    } else {
                        // Nessun risultato trovato
                        Log.e("LOGINFRAGMENT", "Dati Errati")
                        binding.EditTextNomeUtente.setBackgroundResource(R.drawable.error_border_element)
                        binding.EditTextPassword.setBackgroundResource(R.drawable.error_border_element)
                    }
                }
            }

        }

        return binding.root
    }

    private fun saveLoginData(nomeU: String,pass: String){ //funzione che gestisce il salvataggio dei dati nel caso in cui l'utente prema la check box (tramite le sharedPreferences)

        val editor = sharedPreferences.edit()
        editor.putString("Nome Utente", nomeU)
        editor.putString("password", pass)
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
        val singleton= DataSingleton.ottieniIstanza()
        singleton.nomeUtente= nomeU
        singleton.password= pass
    }

    private fun saveData(nomeU: String,pass: String){ //funzione che gestisce il salvataggio dei dati nel caso in cui l'utente NON preme la checkbox
        val singleton= DataSingleton.ottieniIstanza()
        singleton.nomeUtente= nomeU
        singleton.password= pass
    }

    private fun saveRoutine(){
        val editor = sharedPreferences.edit()
        //Per ogni giorno della settimana setto una stringa che indica: yes=Prevista routine di studio per quel giorno. no= Nessuna sessione di studio prevista
        editor.putString("isStudyLun","yes")
        editor.putString("isStudyMar","yes")
        editor.putString("isStudyMer","yes")
        editor.putString("isStudyGio","yes")
        editor.putString("isStudyVen","yes")
        editor.putString("isStudySab","yes")
        editor.putString("isStudyDom","no")
        editor.apply()
    }

    private fun fragmentExists(manager: FragmentManager, tag: String ): Boolean {
        val fragment= manager.findFragmentByTag(tag)
        return fragment != null
    }

  fun PasswordVisibility() {
      val passwordEditText= binding.EditTextPassword
        passwordVisible = !passwordVisible

        if (passwordVisible) {
            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            eyeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.icons8_eye_24)!!
        } else {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            eyeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_remove_red_eye_24)!!
        }

        passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
            ContextCompat.getDrawable(requireContext(), R.drawable.baseline_lock_24),
            null,
            eyeIcon,
            null
        )
    }

    fun selectCorso(idCorso: Int?){
        //query per fare la select del corso di studi
        ApiClient.selectCorso(idCorso){ data, error ->
            if (error != null) {
                // Gestisci l'errore
                Log.e("CORSO", "Si è verificato un errore: $error")
            }else if (data != null) {
                // Utilizza i dati restituiti
                Log.d("CORSO", "Dati ricevuti: $data")
                //salvo il corso nel Singleton
                val singleton= DataSingleton.ottieniIstanza()
                singleton.corsoStudi=data.nomeCorso
                singleton.idCorso=data.idCorso
            }else{
                Log.d("CORSO", "Dati ricevuti: $data")
            }
        }
    }

}