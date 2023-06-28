package com.example.studyplanner

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.studyplanner.databinding.FragmentRegistrazioneBinding


class RegistrazioneFragment : Fragment() {

    private lateinit var binding: FragmentRegistrazioneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRegistrazioneBinding.inflate(inflater)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner= binding.spinner
        val textSpinner= binding.textSpinner

        // Definisco l'adapter per lo Spinner
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.spinner_items, android.R.layout.simple_spinner_item)
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
                val editableText = Editable.Factory.getInstance().newEditable(selectedItem)
                textSpinner.text = editableText

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
              /*  val defaultText = "Seleziona domanda di sicurezza"
                textSpinner.setText(Editable.Factory.getInstance().newEditable(defaultText)) */
            }
        }
    }
}
