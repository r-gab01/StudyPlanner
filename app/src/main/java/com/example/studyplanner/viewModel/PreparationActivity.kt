package com.example.studyplanner.viewModel

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
            val fragment = PreparationFragmentInfo()
            val bundle = Bundle()
            fragment.arguments = bundle
            //arguments di un Fragment è un Bundle che può essere utilizzato per passare dati al Fragment durante la sua creazione.

            val pagineTotali=esameSessione?.pagineTot.toString()
            bundle.putString("pagineTotali", pagineTotali)
            Log.d("PASSAGGIO VALORI","valore inviato $pagineTotali")

            val giornoEsame=esameSessione?.dataAppello
            bundle.putString("giornoEsame", giornoEsame.toString())
            Log.d("PASSAGGIO VALORI","valore inviato $giornoEsame")

            val pagineStudiate=esameSessione?.pagineStud
            if (pagineStudiate != null) {
                bundle.putInt("pagineStudiate", pagineStudiate)
            }
            Log.d("PASSAGGIO VALORI","valore inviato $pagineStudiate")

            val manager=supportFragmentManager
            val transaction=manager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView,fragment)
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