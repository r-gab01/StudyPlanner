package com.example.studyplanner.viewModel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentPreparationMaterialeBinding
import com.example.studyplanner.databinding.MaterialCardViewBinding

class PreparationFragmentMateriale : Fragment() {
    private lateinit var binding: FragmentPreparationMaterialeBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPreparationMaterialeBinding.inflate(layoutInflater)

        //LAYOUT MANAGER
        binding.recyclerview.layoutManager=LinearLayoutManager(this.context)

        //Creazione della lista con il contenuto da mostrare
        val argomenti=ArrayList<String>()
        /*ApiClient.selectArgomenti(id) { data, error->
            if(error!=null){
                Log.e("PREPARAZIONE", "Si è verificato un errore: $error")
                try {
                    Toast.makeText(requireContext(),R.string.errore_ConnessioneServer.toString(), Toast.LENGTH_LONG).show()
                } catch (_: Exception){

                }
            }  else if(data!=null) {
                for (i in data){
                    if(i!=null){
                        argomenti.add(i.argomento.toString())
                    }
                }
            } else {
                binding.textError.text= R.string.errore_NoArgomenti.toString()
            }

        }*/


        recyclerView = binding.recyclerview
        //Configurazione dell’Adapter con la sorgente dati
        recyclerView.adapter=PreparationAdapter(argomenti)


        return binding.root
    }

    companion object {
        const val SEARCH_PREFIX = "https://www.google.com/search?q="
    }

}