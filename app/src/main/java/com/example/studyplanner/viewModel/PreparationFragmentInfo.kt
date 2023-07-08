package com.example.studyplanner.viewModel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.studyplanner.databinding.FragmentPreparationInfoBinding


class PreparationFragmentInfo : Fragment() {
    private lateinit var binding: FragmentPreparationInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPreparationInfoBinding.inflate(inflater)

        val progressBar:ProgressBar=binding.progressBar
        val progressText:TextView=binding.progressoPagine //mostra la percentuale

        val numeroPagineTotali=binding.numPagineTotali.text.toString()
        val numeroPagineStudiate=binding.numPagineStudiate.text.toString()
        val progresso=(numeroPagineStudiate.toFloat()/numeroPagineTotali.toFloat()*100).toInt()
        progressBar.progress=progresso
        progressText.text="$progresso%"

        return binding.root
    }
}