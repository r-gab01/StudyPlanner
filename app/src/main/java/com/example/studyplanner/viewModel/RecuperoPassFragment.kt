package com.example.studyplanner.viewModel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentRecuperoPassBinding


class RecuperoPassFragment : Fragment() {

    private lateinit var binding: FragmentRecuperoPassBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecuperoPassBinding.inflate(inflater)

        binding.buttonRecupero.setOnClickListener {

            val nomeInserito = binding.nomeUtenteTextEdit.text.toString()
            val domandaInserita = binding.campoInserisciDomanda.text.toString()
            val rispostaInserita = binding.campoInserisciRisposta.text.toString()
            if (nomeInserito.isEmpty())
                binding.nomeUtenteTextEdit.setBackgroundResource(R.drawable.error_border_element)
            if (domandaInserita.isEmpty())
                binding.campoInserisciDomanda.setBackgroundResource(R.drawable.error_border_element)
            if (rispostaInserita.isEmpty())
                binding.campoInserisciRisposta.setBackgroundResource(R.drawable.error_border_element)
            else {
                ApiClient.recuperoPW(
                    nomeInserito,
                    domandaInserita,
                    rispostaInserita
                ) { data, error ->
                    if (error != null) {
                        // Gestisci l'errore
                        Log.e("RECUPEROPW", "Si è verificato un errore: $error")
                        Log.e("RECUPEROPW", domandaInserita)
                        Log.e("RECUPEROPW", rispostaInserita)
                    } else if (data != null) {
                        // Utilizza i dati restituiti
                        Log.d("RECUPEROPW", "Dati ricevuti: $data")
                        binding.mostraPass1.text = "La tua password è:"
                        binding.mostraPass2.text = data.password.toString()
                    } else{
                        Toast.makeText(activity,"Dati errati", Toast.LENGTH_LONG).show()
                        Log.e("RECUPEROPW", domandaInserita)
                        Log.e("RECUPEROPW", rispostaInserita)
                        binding.nomeUtenteTextEdit.setBackgroundResource(R.drawable.error_border_element)
                        binding.campoInserisciDomanda.setBackgroundResource(R.drawable.error_border_element)
                        binding.campoInserisciRisposta.setBackgroundResource(R.drawable.error_border_element)
                    }
                }
            }
        }
        return binding.root
    }
}
