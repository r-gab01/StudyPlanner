package com.example.studyplanner.viewModel

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentRegistrazioneBinding

class RegistrazioneFragment : Fragment() {

    private lateinit var binding: FragmentRegistrazioneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentRegistrazioneBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nomeCorsi = ArrayList<String?>()
        val idCorsi = ArrayList<Int?>()
        val nomeIns = binding.insUtente.text
        val pwIns = binding.insPass.text
        val confPwIns = binding.confPass.text
        val univIns = binding.insUni.text
        val corsoIns = binding.insCorso
        val domandaIns = binding.insDomanda
        val rispIns = binding.rispSicurezza.text
        val errore = binding.mostraErrori
        var idCorsoSelected: Int? = -1
        var domSelected: String? = ""

        //Query per ottenere i corsi da DB e riempire l'elemento di selezione
        ApiClient.selectCorsoStudio{ data,error ->
            if (error != null){
                Log.e("REGISTRAZIONEFRAGMENT", "Si è verificato un errore: $error")
                try {
                    Toast.makeText(requireContext(),"Errore durante la connessione al server", Toast.LENGTH_LONG).show()
                } catch (_: Exception){

                }
            }else if (data != null){
                for (i in data){
                    nomeCorsi.add(i?.nomeCorso)
                    idCorsi.add(i?.idCorso)
                }
                val arrayAdapterCorso = ArrayAdapter(requireContext(), R.layout.dropdown_item, nomeCorsi)
                corsoIns.setAdapter(arrayAdapterCorso)
            }
            else{
                Log.e("REGISTRAZIONEFRAGMENT", "Errore")
                try {
                    Toast.makeText(requireContext(),"Errore durante la connessione al server", Toast.LENGTH_LONG).show()
                } catch (_: Exception){

                }
            }
        }
        //riempio il selettore di domande di sicurezza
        val domande = resources.getStringArray(R.array.spinner_domanda_s_items)
        val arrayAdapterDomanda = ArrayAdapter(requireContext(), R.layout.dropdown_item, domande)
        domandaIns.setAdapter(arrayAdapterDomanda)

        //ottengo elemento selezionato nei 2 'spinner'
        binding.insCorso.setOnItemClickListener { _, _, position, _ ->
            idCorsoSelected = idCorsi[position]
        }
        binding.insDomanda.setOnItemClickListener { parent, _, position, _ ->
            domSelected = parent.getItemAtPosition(position).toString()
        }

        binding.ButtonCreateAccount.setOnClickListener {
            if (nomeIns.toString().isEmpty() || pwIns.isEmpty() || confPwIns.isEmpty()
                || univIns.isEmpty() || idCorsoSelected==-1 || domSelected.isNullOrEmpty() || rispIns.isEmpty()){
                errore.text = resources.getString(R.string.erroreCampo)
            }else if(!nomeIns.matches(Regex("[a-zA-Z]{4,12}\\d*"))){    //il nome utente può contenere solo lettere maiuscole minuscole e numeri
                Log.d("REG", "Nome utente non consentito")
                errore.text = resources.getString(R.string.erroreNomeUtente)
            }else if (!pwIns.matches(Regex("^(?=.*\\d)(?=.*\\W)[\\w\\W]{1,12}$"))){      //La password deve contenere una lettera maiuscola, una minuscola e un carattere speciale
                Log.d("REG", "Password non consentita")
                errore.text = resources.getString(R.string.errorePassword)
            }else if(confPwIns.toString() != pwIns.toString()) {
                Log.d("REG", "Password e conferma password non corrispondono")
                Log.d("REG", pwIns.toString())
                Log.d("REG", confPwIns.toString())
                errore.text = resources.getString(R.string.erroreConfPassword)
            }else {
                //Verifico se il nome utente è presente
                ApiClient.verificaNomeUtente(nomeIns.toString()) { data, error ->
                    if (error != null) {
                        Log.e("REG", "VerificaNomeUtente: Si è verificato un errore: $error")
                        Toast.makeText(context,"Errore durante la connessione al server", Toast.LENGTH_LONG).show()
                    } else if (data != null) {
                        //data != null ossia la query ha trovato nome utente
                        Log.d("REG", "VerificaNomeUtente:$data")
                        errore.text = resources.getString(R.string.erroreNomeUtentePresente)
                    } else {
                        //query andata a buon fine non ha trovato nome utente, allora è valido
                        Log.e("REG", "VerificaNomeUtente: registro")
                        Log.d("Registra", pwIns.toString())
                        Log.d("Registra", domSelected.toString())
                        Log.d("Registra", rispIns.toString())
                        ApiClient.registraStudente(nomeIns.toString(),univIns.toString(), idCorsoSelected, pwIns.toString(),domSelected.toString(),rispIns.toString()){ response, error->   //insert nella table Studente
                            if (error!=null){
                                Log.e("REGISTRAZIONEFRAGMENT", "Si è verificato un errore: $error")
                                try {
                                    Toast.makeText(requireContext(),"Errore durante la connessione al server", Toast.LENGTH_LONG).show()
                                } catch (_: Exception){

                                }
                            }else if (response != null){
                                errore.setTextColor(Color.GREEN)
                                errore.text = resources.getString(R.string.confermaRegistrazone)
                            } else{
                                Log.e("REGISTRAZIONEFRAGMENT", "Errore in registra studente")
                                try {
                                    Toast.makeText(requireContext(),"Errore durante la connessione al server", Toast.LENGTH_LONG).show()
                                } catch (_: Exception){

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
