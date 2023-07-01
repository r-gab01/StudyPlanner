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

            }
        }
        return binding.root
    }
}
