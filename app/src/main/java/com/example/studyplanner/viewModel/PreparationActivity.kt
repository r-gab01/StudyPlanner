package com.example.studyplanner.viewModel

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.ActivityPreparationBinding
import com.example.studyplanner.model.SessioneStudioDBModel

class PreparationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreparationBinding
    private lateinit var fragmentInfo: PreparationFragmentInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityPreparationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //ottengo i dati dell'esame sessione che ha cliccato
        var extras: Bundle? = intent.extras
        val esameSessione: SessioneStudioDBModel? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                extras?.getParcelable("EsameCliccato", SessioneStudioDBModel::class.java)
        } else {
            extras?.getParcelable("EsameCliccato")
        }
        binding.titleCardView.text = esameSessione?.nomeMateria
        ApiClient.selectMateria(esameSessione?.nomeMateria){ data, error ->
            if (error != null){
                Toast.makeText(this,"Errore durante la connessione al server", Toast.LENGTH_LONG).show()
            }
            else if(data != null){
                binding.numCfu.text = data.cfu.toString()
                binding.nomeDocente.text = data.docente
            } else{
                Toast.makeText(this,"Errore durante la connessione al server, riprovare", Toast.LENGTH_LONG).show()
            }
        }

        //MOSTRA FRAGMENT INFO GENERALI
        val chipInfoGenerali=binding.chipInfoGenerali
        chipInfoGenerali.setOnClickListener {
            //caricare fragment e passargli come dato: Da activity a fragment
                //esameSessione

            fragmentInfo = PreparationFragmentInfo()
            // Creare un Bundle per i dati presi dal database
            val bundle = Bundle()
            bundle.putParcelable("EsameCliccato", esameSessione)
            // Imposta il bundle come argomento del fragment
            fragmentInfo.arguments = bundle
            Log.d("PASSAGGIO VALORI","valori inviati")

            val manager=supportFragmentManager
            val transaction=manager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView,PreparationFragmentInfo())
            transaction.commit()
        }

        //MOSTRA FRAGMENT MATERIALE
        val chipMateriale=binding.chipMateriale
        chipMateriale.setOnClickListener {
            //caricare fragment e passargli come dato:
                    //esameSessione?.idSessione
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, PreparationFragmentMateriale())
            transaction.commit()
        }
    }

}