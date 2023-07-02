package com.example.studyplanner.viewModel

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentRegistrazioneBinding


class RegistrazioneFragment : Fragment() {

    private lateinit var binding: FragmentRegistrazioneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRegistrazioneBinding.inflate(inflater)
        ApiClient.selectCorsoStudio { data, error ->
            if (error != null) {
                // Gestisci l'errore
                Log.e("REGISTRAZIONE", "Si è verificato un errore: $error")
            } else if (data != null) {
                // Utilizza i dati restituiti
                Log.d("REGISTRAZIONE", "Dati ricevuti: $data")

            } else{
                Log.d("REGISTRAZIONE", "Dati ricevuti: $data")
            }
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val corsi = ArrayList<String>()
        val nomeIns = binding.insUtente.text
        val pwIns = binding.insPass.text
        val confPwIns = binding.confPass.text
        val univIns = binding.insUni.text
        val corsoIns = binding.insCorso
        val domandaIns = binding.insDomanda
        var errore = binding.mostraErrori
        var corsoSelected: String? = ""
        var domSelected: String? = ""

        //Query per ottenere i corsi da DB
        ApiClient.selectCorsoStudio{ data,error ->
            if (error != null){
                Log.e("REGISTRAZIONEFRAGMENT", "Si è verificato un errore: $error")
            }else if (data != null){
                for (i in data){
                    corsi.add(i?.nomeCorso.toString())
                }
                val arrayAdapterCorso = ArrayAdapter(requireContext(), R.layout.dropdown_item, corsi)
                corsoIns.setAdapter(arrayAdapterCorso)
            }
            else{
                Log.e("REGISTRAZIONEFRAGMENT", "Errore")
            }
        }
        val domande = resources.getStringArray(R.array.spinner_domanda_s_items)
        val arrayAdapterDomanda = ArrayAdapter(requireContext(), R.layout.dropdown_item, domande)
        domandaIns.setAdapter(arrayAdapterDomanda)

        /*
        // Definisco l'adapter per lo Spinner
        val adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.spinner_domanda_s_items, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Imposto il listener per il cambio di selezione dello Spinner
        //object : AdapterView.OnItemSelectedListener: Questa sintassi crea una nuova istanza di un oggetto anonimo che implementa l'interfaccia AdapterView.OnItemSelectedListener.
        // L'interfaccia richiede l'implementazione dei metodi onItemSelected e onNothingSelected.
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //Questo metodo viene chiamato quando un elemento viene selezionato nello spinner. Riceve come parametri parent (l'AdapterView contenente lo spinner), view (la vista selezionata), position (la posizione dell'elemento selezionato) e id (l'ID dell'elemento selezionato).
            // All'interno di questo metodo, viene estratto l'elemento selezionato dalla posizione e viene assegnato al testo della textSpinner.
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //dato che La text view aspetta un tipo Editabile prima convertiamolo in Editable:
                val selectedItem = parent?.getItemAtPosition(position).toString()
                domSelected = selectedItem
                val editableText = Editable.Factory.getInstance().newEditable(selectedItem)
                textSpinner.text = editableText

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
              /*  val defaultText = "Seleziona domanda di sicurezza"
                textSpinner.setText(Editable.Factory.getInstance().newEditable(defaultText)) */
            }
        }

         */

        binding.insCorso.setOnItemClickListener { parent, _, position, _ ->
            // L'utente ha selezionato un'opzione nell'AutoCompleteTextView
            corsoSelected = parent.getItemAtPosition(position).toString()
        }

        binding.ButtonCreateAccount.setOnClickListener {
            if (nomeIns.toString().isEmpty() || pwIns.isEmpty() || confPwIns.isEmpty()
                || univIns.isEmpty() || corsoSelected.isNullOrEmpty() || domSelected.isNullOrEmpty()){
                Log.d("REG",nomeIns.toString())
                Log.d("REG",pwIns.toString())
                Log.d("REG",confPwIns.toString())
                Log.d("REG",univIns.toString())
                Log.d("REG",corsoSelected.toString())
                Log.d("REG",domSelected.toString())
                errore.text = "Inserire tutti i campi"
            }
        }
    }
}
