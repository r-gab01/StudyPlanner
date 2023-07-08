package com.example.studyplanner.viewModel

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentExamsBinding
import com.example.studyplanner.model.SessioneStudioDBModel

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

        //qua prendo i dati da mandare all'adapter
        val esami = ArrayList<SessioneStudioDBModel>()
        ApiClient.selectEsamiSessione { data, error ->
            if (error != null){
                Toast.makeText(requireContext(),"Errore durante la connessione al server", Toast.LENGTH_LONG).show()
            }
            else if(data != null){
                for (i in data){
                    if (i != null) {
                        Log.d("ExamsFragment", i.toString())
                        esami.add(i)
                    }
                }
                val adapter = ExamAdapter(esami)
                binding.recyclerview.adapter = adapter
            } else{
            }
        }

        return binding.root
    }

}