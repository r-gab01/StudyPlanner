package com.example.studyplanner.viewModel

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


@Suppress("DEPRECATION")
class PreparationFragmentInfo : Fragment() {
    private lateinit var binding: FragmentPreparationInfoBinding
    private var esameSessione: SessioneStudioDBModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //1
        // Recupera i dati passati dall'Activity tramite il Bundle
        /*esamiSessione = arguments?.getParcelable("EsameCliccato")*/

        //2
        // Recupera i dati passati dall'Activity tramite il Bundle
        arguments?.let { bundle ->
            esameSessione = bundle.getParcelable("EsameCliccato")
        }
        Log.d("PASSAGGIO VALORI","valori rivecuti")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPreparationInfoBinding.inflate(inflater)

        val progressBar:ProgressBar=binding.progressBar
        val progressText:TextView=binding.progressoPagine //mostra la percentuale

        // Utilizzo i valori di esameSessione
        if (esameSessione != null) {
            binding.giornoEsame.text= esameSessione?.dataAppello.toString()
            binding.numPagineTotali.text= esameSessione?.pagineTot.toString().toInt().toString()
            binding.numPagineTotali.text= esameSessione?.pagineTot?.toString()?.toInt().toString()
        }

        //dopo aver preso i dati dal db
        /*val numeroPagineTotali=binding.numPagineTotali.text.toString()
       val numeroPagineStudiate=binding.numPagineStudiate.text.toString()
       val progresso=(numeroPagineStudiate.toDouble()/numeroPagineTotali.toDouble()*100).toInt()
       progressBar.progress=progresso
       progressText.text="$progresso%"*/

        return binding.root
    }

}