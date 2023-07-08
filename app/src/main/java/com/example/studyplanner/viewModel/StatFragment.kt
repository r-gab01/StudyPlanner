package com.example.studyplanner.viewModel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentStatBinding
import com.example.studyplanner.model.DataSingleton
import com.example.studyplanner.model.ExamModel
import com.example.studyplanner.model.MateriaModel
import java.util.ArrayList

class StatFragment : Fragment() {

    private lateinit var binding: FragmentStatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentStatBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


                    val recyclerMaterie= binding.recyclerMateria
                    val materie = ArrayList<MateriaModel>()

                    val layoutManager = LinearLayoutManager(requireContext())
                    recyclerMaterie.layoutManager = layoutManager


                    //faccio la query che mi serve per riempire la recycler view
                    ApiClient.selectCarriera(DataSingleton.ottieniIstanza().nomeUtente){ data, error ->
                        if (error != null) {
                            Log.e("STATFRAGMENT", "Si è verificato un errore: $error")
                            try {
                                Toast.makeText(requireContext(),"Errore durante la connessione al server",Toast.LENGTH_LONG).show()
                            } catch (_: Exception) {

                            }
                        } else if (data != null) {
                            for (i in data) {
                                Log.d("STATFRAGMENT", i.toString())
                                materie.add(MateriaModel(i?.cfu,i?.nomeMateria,i?.voto))
                                // materie.add(MateriaModel(i?.cfu,i?.nomeMateria,data.voto))
                            }
                        } else {
                            Log.e("STATFRAGMENT", "Errore")
                            try {
                                Toast.makeText(requireContext(),"Errore durante la connessione al server",Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {

                            }
                        }
                    }
       // val adapter = MateriaAdapter(materie)
       // recyclerMaterie.adapter = adapter

                }




    }

