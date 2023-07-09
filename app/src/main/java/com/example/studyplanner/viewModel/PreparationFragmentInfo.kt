package com.example.studyplanner.viewModel


import android.graphics.Color
import android.os.Build
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
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class PreparationFragmentInfo : Fragment() {
    private lateinit var binding: FragmentPreparationInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPreparationInfoBinding.inflate(inflater)

        val progressBar:ProgressBar=binding.progressBar
        val progressText:TextView=binding.progressoPagine //mostra la percentuale

        val esameSessione = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {    //esameSessione variabile che contiene tutti i dati
            arguments?.getParcelable("arguments", SessioneStudioDBModel::class.java)
        } else {
            arguments?.getParcelable("arguments")
        }
        //riempio tutte le TextView coi propri dati
        val pagineTotali = esameSessione?.pagineTot
        binding.numPagineTotali.text=pagineTotali.toString()
        Log.d("PASSAGGIO VALORI","valore ricevuto $pagineTotali")

        val giornoEsame = esameSessione?.dataAppello
        binding.giornoEsame.text = giornoEsame.toString()
        Log.d("PASSAGGIO VALORI","valore ricevuto $giornoEsame")
        val today = LocalDate.now()
        val examDate: LocalDate? = giornoEsame?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate() //converto in LocalDate il tipo Date del mio esame
        var diff = ChronoUnit.DAYS.between(today, examDate)
        binding.numGiorniRimanenti.text = diff.toString()
        if (diff <= 10) {
            binding.numGiorniRimanenti.setTextColor(Color.RED)
        }

        val pagineStudiate : Int? = esameSessione?.pagineStud
        binding.numPagineStudiate.text = pagineStudiate.toString()
        Log.d("PASSAGGIO VALORI","valore ricevuto $pagineStudiate")

        val pagineRimanenti = (pagineTotali?.minus(pagineStudiate ?: 0))
        if(diff<0){
            diff = 1
        }
        val consigliate = (pagineRimanenti ?: 0) / (diff ?: 1)
            binding.numPagineConsigliate.text = consigliate.toString()

                    //gestisco Progress Bar
        if (pagineTotali != 0 && pagineStudiate != 0) {
            val progresso = ((pagineStudiate?.toFloat() ?: 0f) / (pagineTotali?.toFloat() ?: 1f)* 100).toInt()
            progressBar.progress = progresso
            progressText.text = "$progresso%"
        } else {
            progressBar.progress = 0
            progressText.text = "0%"
        }

        return binding.root
    }
}