package com.example.studyplanner

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.studyplanner.database.ClientNetwork
import com.example.studyplanner.databinding.FragmentLoginBinding
import com.example.studyplanner.model.SharedData

class LoginFragment : Fragment(){

    private lateinit var binding : FragmentLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLoginBinding.inflate(inflater)

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

        tastoLogin.setOnClickListener{

            //QUERY AL DB PER VERIFICARE CREDENZIALI
            val nomeInserito = binding.EditTextNomeUtente.text.toString()
            val pwInserita = binding.EditTextPassword.text.toString()
            val query = "select * from autenticazione where nome_u_ref = '${nomeInserito}' and password = '${pwInserita}';"
            ClientNetwork.selectValue(query) { result, error ->
                if (error != null) {
                    // Gestisci l'errore
                    Log.e("DB", "Errore nella chiamata: ${error.message}")
                } else {
                    // Utilizza il JsonObject risultante
                    if (result != null) {
                        // Esegui le operazioni necessarie con il result
                        if(rememberMeCheckBox.isChecked){ //Solo se la check box è stata checkata
                            saveLoginData()
                        }
                        SharedData.nomeUtente = nomeInserito
                        SharedData.password = pwInserita
                        SharedData.correctLogin = true
                        Log.d("LOGIN", SharedData.nomeUtente)
                        Log.d("LOGIN", SharedData.password)
                        requireActivity().finish()

                    } else {
                        // Nessun result restituito
                        Log.e("BOUNDARYDB", "Dati Errati")
                        binding.EditTextNomeUtente.setBackgroundResource(R.drawable.error_border_element)
                        binding.EditTextPassword.setBackgroundResource(R.drawable.error_border_element)
                    }
                }
            }
        }

        return binding.root
    }

    private fun saveLoginData(){
        //Recuperiamo i dati scritti dall'utente in fase di Login
        val nomeUtente= binding.EditTextNomeUtente.text.toString()
        val password= binding.EditTextPassword.text.toString()
        val isLoggedIn= binding.checkBox.isChecked

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Nome Utente", nomeUtente)
        editor.putString("password", password)
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    private fun fragmentExists(manager: FragmentManager, tag: String ): Boolean {
        val fragment= manager.findFragmentByTag(tag)
        return fragment != null

    }

}