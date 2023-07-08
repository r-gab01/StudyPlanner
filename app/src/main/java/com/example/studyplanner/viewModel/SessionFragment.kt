package com.example.studyplanner.viewModel

import android.app.AlertDialog
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
import com.example.studyplanner.databinding.FragmentSessionBinding
import java.util.ArrayList

class SessionFragment : Fragment() {
    private lateinit var binding: FragmentSessionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSessionBinding.inflate(inflater)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val nomiMaterie = ArrayList<String?>()
        val idSessioniMaterie = ArrayList<Int?>()
        val selettoreMateria = binding.selMateriaSession
        var idSessione: Int? = -1

        ApiClient.selectEsamiSessione { data, error ->
            if (error != null) {
                Log.e("ADDEXAM", "Si è verificato un errore: $error")
                try {
                    Toast.makeText(
                        requireContext(),
                        "Errore durante la connessione al server",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (_: Exception) {

                }
            } else if (data != null) {
                for (i in data) {
                    nomiMaterie.add(i?.nomeMateria)
                    idSessioniMaterie.add(i?.idSessione)
                }
                val arrayAdapterMaterie = ArrayAdapter(requireContext(), R.layout.dropdown_item, nomiMaterie)
                selettoreMateria.setAdapter(arrayAdapterMaterie)
            } else {
                Log.e("ADDEXAM", "Errore")
                try {
                    Toast.makeText(
                        requireContext(),
                        "Errore durante la connessione al server",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (_: Exception) {

                }
            }
        }

        //ottengo materia selezionata e idSessione
        selettoreMateria.setOnItemClickListener { _, _, position, _ ->
            idSessione = idSessioniMaterie[position]
        }
        //gestisco tasto conferma
        binding.confirmPageButton.setOnClickListener {
            val pagine = binding.editTextNumber.text
            if (pagine.isNullOrEmpty() || pagine.toString().toInt() <= 0){
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Errore")
                builder.setIcon(R.drawable.baseline_error_24)
                builder.setMessage("Inserire il numero di pagine studiate")
                builder.setPositiveButton("OK") { _, _ -> }
                builder.create().show()
            } else{
                ApiClient.updatePagineStud(pagine.toString().toInt(),idSessione){ response, error->
                    if (error != null) {
                        Log.e("ADDEXAM", "Si è verificato un errore: $error")
                        try {
                            Toast.makeText(requireContext(), "Errore durante la connessione al server", Toast.LENGTH_LONG).show()
                        } catch (_: Exception) {
                        }
                    } else if (response != null) {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Aggiornamento pagine")
                        builder.setIcon(R.drawable.round_check_circle_24)
                        builder.setMessage("Operazione completata con successo")
                        builder.setPositiveButton("OK") { _, _ -> }
                        builder.create().show()
                        binding.editTextNumber.text.clear()
                    } else {
                        Log.e("ADDEXAM", "Errore")
                        try {
                            Toast.makeText(requireContext(), "Errore durante la connessione al server", Toast.LENGTH_LONG).show()
                        } catch (_: Exception) {
                        }
                    }
                }
            }
        }
    }

}