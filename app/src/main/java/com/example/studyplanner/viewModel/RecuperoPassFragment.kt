package com.example.studyplanner.viewModel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentRecuperoPassBinding
import com.example.studyplanner.model.SharedData


class RecuperoPassFragment : Fragment() {

    private lateinit var binding: FragmentRecuperoPassBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecuperoPassBinding.inflate(inflater)

        binding.buttonRecupero.setOnClickListener {

            val nomeInserito = binding.nomeUtenteTextEdit.text.trim()
            val domandaInserita = binding.campoInserisciDomanda.text.trim()
            val rispostaInserita = binding.campoInserisciRisposta.text.trim()
            if (nomeInserito.isEmpty())
                binding.nomeUtenteTextEdit.setBackgroundResource(R.drawable.error_border_element)
            if (domandaInserita.isEmpty())
                binding.campoInserisciDomanda.setBackgroundResource(R.drawable.error_border_element)
            if (rispostaInserita.isEmpty())
                binding.campoInserisciRisposta.setBackgroundResource(R.drawable.error_border_element)
            else {
                val query = "select * from autenticazione where nome_u_ref = '${nomeInserito}' and domanda_s='${domandaInserita}' and risposta_s='${rispostaInserita}';"
                ApiClient.selectValue(query) { result, error ->
                    if (error != null) {
                        // Gestisci l'errore
                        Log.e("DB", "Errore nella chiamata: ${error.message}")
                    } else {
                        // Utilizza il JsonObject risultante
                        if (result != null) {
                            binding.mostraPass1.text = "La tua password è:"
                            binding.mostraPass2.text = result.get("password").toString()
                            Log.d("RECUPEROPW", SharedData.nomeUtente)
                            Log.d("RECUPEROPW", SharedData.password)

                        } else {
                            // Nessun result restituito
                            Log.e("RECUPEROPW", "Dati Errati")
                            binding.nomeUtenteTextEdit.setBackgroundResource(R.drawable.error_border_element)
                            binding.campoInserisciDomanda.setBackgroundResource(R.drawable.error_border_element)
                            binding.campoInserisciRisposta.setBackgroundResource(R.drawable.error_border_element)
                        }
                    }
                }
            }
        }
        return binding.root
    }
}
