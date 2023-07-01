package com.example.studyplanner.viewModel

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studyplanner.model.ExamModel
import com.example.studyplanner.databinding.FragmentExamsBinding

class ExamsFragment : Fragment() {

    private lateinit var binding: FragmentExamsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExamsBinding.inflate(inflater)

        //Gestisco la RecyclerView
        val context : Context = requireContext()        //Ottengo il contesto
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        //qua prendo i dati da mandare all'adapter,
        //DOVREI PRENDERLI DAL DB
        val data = ArrayList<ExamModel>()
        data.add(ExamModel("GEOMETRIA", "14 luglio", 15, 0))
        data.add(ExamModel("Analisi", "14 luglio", 15, 0))
        data.add(ExamModel("ESERCIZI DI LATINO", "28 giugno", 7,1))

        val adapter = ExamAdapter(data)
        binding.recyclerview.adapter = adapter

        return binding.root
    }
}