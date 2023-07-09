package com.example.studyplanner.viewModel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.studyplanner.databinding.FragmentPreparationInfoBinding
import com.example.studyplanner.model.SessioneStudioDBModel

class PreparationFragmentInfo : Fragment() {
    private lateinit var binding: FragmentPreparationInfoBinding
    private var esameSessione: SessioneStudioDBModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPreparationInfoBinding.inflate(inflater)

        val progressBar:ProgressBar=binding.progressBar
        val progressText:TextView=binding.progressoPagine //mostra la percentuale

        val pagineTotali=arguments?.getString("pagineTotali").toString()
        binding.numPagineTotali.text=pagineTotali
        Log.d("PASSAGGIO VALORI","valore ricevuto $pagineTotali")

        val giornoEsame=arguments?.getString("giornoEsame").toString()
        binding.giornoEsame.text=giornoEsame
        Log.d("PASSAGGIO VALORI","valore ricevuto $giornoEsame")

        val pagineStudiate= arguments?.getInt("pagineStudiate")
        binding.numPagineStudiate.text = pagineStudiate.toString()
        Log.d("PASSAGGIO VALORI","valore ricevuto $pagineStudiate")

        //dopo aver preso i dati dal db
        /*val numeroPagineTotali=binding.numPagineTotali.text.toString()
       val numeroPagineStudiate=binding.numPagineStudiate.text.toString()
       val progresso=(numeroPagineStudiate.toDouble()/numeroPagineTotali.toDouble()*100).toInt()
       progressBar.progress=progresso
       progressText.text="$progresso%"*/

        return binding.root
    }
}