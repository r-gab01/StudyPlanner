package com.example.studyplanner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.studyplanner.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

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
        //Utilizzo un file delle preferenze condivise. Lo gestisco nella funzione sabveLoginData()
        var tastoLogin= binding.buttonLogin
        var rememberMeCheckBox= binding.checkBox

        tastoLogin.setOnClickListener{
            if(rememberMeCheckBox.isChecked){ //Solo se la check box è stata checkata
                saveLoginData()
            }else {
                val i = Intent(requireContext(), MainActivity::class.java)
                startActivity(i)
            }
        }

        return binding.root
    }

    private fun saveLoginData(){
        //Recuperiamo i dati scritti dall'utente in fase di Login
        val nomeUtente= binding.EditTextNomeUtente.text.toString()
        val password= binding.EditTextPassword.text.toString()

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Nome Utente", nomeUtente)
        editor.putString("password", password)
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