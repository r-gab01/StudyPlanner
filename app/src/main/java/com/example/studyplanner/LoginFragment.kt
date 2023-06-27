package com.example.studyplanner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.studyplanner.database.BoundaryDBMS
import com.example.studyplanner.database.ClientNetwork
import com.example.studyplanner.databinding.FragmentLoginBinding

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
            val nomeInserito = binding.EditTextNomeUtente.text.toString()
            val pwInserita = binding.EditTextPassword.text.toString()
            val loginDone = BoundaryDBMS.login(nomeInserito, pwInserita)        //Metodo che verifica se i dati sono presenti nel DB
            /*
            if(rememberMeCheckBox.isChecked){ //Solo se la check box è stata checkata
                saveLoginData()
            }
            if (loginDone) {
                // Termino l'Activity corrente e ritorna alla MainActivity
                requireActivity().finish()
            }else
                Log.e("LOGIN", "Dati errati")
             */
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
        if (fragment == null)
            return false
        else
            return true

    }
}