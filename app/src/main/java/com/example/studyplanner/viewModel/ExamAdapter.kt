package com.example.studyplanner.viewModel

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.ExamsCardViewBinding
import com.example.studyplanner.model.SessioneStudioDBModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class ExamAdapter(private val examsList: ArrayList<SessioneStudioDBModel>) : RecyclerView.Adapter<ExamAdapter.ViewHolder>() {

    class ViewHolder(binding: ExamsCardViewBinding): RecyclerView.ViewHolder(binding.root) {
        val cardView = binding.cardView
        val title = binding.titleCardView
        val date = binding.dataEsameTextView
        val remainingDays = binding.GiorniRimTextView
        val image = binding.imageviewCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ExamsCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = ViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val examViewModel = examsList[position]     //variabile contenente una examList ( ossia lista di examViewModel)
        //prendo i valori dalla lista e li passo al viewHolder che li inserisce nelle CardView
        holder.date.text = examViewModel.dataAppello.toString()
        holder.title.text = examViewModel.nomeMateria
        //confronto data esame con quella attuale, se minore di 10 giorni la evidenzio in rosso
        val today = LocalDate.now()
        val examDate: LocalDate = examViewModel.dataAppello.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() //converto in LocalDate il tipo Date del mio esame
        val diff = ChronoUnit.DAYS.between(today, examDate)
        holder.remainingDays.text = diff.toString()
        if (diff <= 10) {
            holder.remainingDays.setTextColor(Color.RED)
            holder.cardView.strokeColor = Color.RED
        }
        holder.cardView.setOnClickListener {
            //viene ottenuto il contesto (context) dalla View su cui è stato effettuato il clic
            val context = it.context
            val intent = Intent(context, PreparationActivity::class.java)
            intent.putExtra("EsameCliccato", examsList[position])
            context.startActivity(intent)
        }
        //Gestione fine esame, tenendo premuto apro un dialog personalizzato dove inserisce il voto
        holder.cardView.setOnLongClickListener {
            val context = it.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Esame superato?")
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_insert_page, null)
            builder.setView(view)

            val votoIns = view.findViewById<EditText>(R.id.insVoto)
            builder.setPositiveButton("Conferma") { dialog, _ ->
                val lode = view.findViewById<CheckBox>(R.id.lodeCheckBox).isChecked
                if(votoIns.text.isNullOrEmpty()){
                    Toast.makeText(context,"Inserire un voto", Toast.LENGTH_LONG).show()
                }else if((votoIns.text.toString().toInt() in 18..30 && (!lode)) ||
                    (votoIns.text.toString().toInt() == 30 && lode) ) {
                    Log.d("PRova", "lode: $lode")
                        val voto = votoIns.text.toString().toInt()
                        ApiClient.updateCarriera(
                            examsList[position].nomeMateria, voto, lode) { response, error ->
                            if (error != null) {
                                Log.e("CompletaEsame", "questo errore")
                                try {
                                    Toast.makeText(
                                        context,
                                        "Errore durante la connessione al server",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } catch (_: Exception) {
                                }
                            } else if (response != null) {
                                ApiClient.rimuoviEsame(examsList[position].idSessione){ response, error ->
                                    if (error!= null){
                                        Toast.makeText(
                                            context,
                                            "Errore durante la connessione al server",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else if (response!= null){
                                        val builder = android.app.AlertDialog.Builder(context)
                                        builder.setTitle("Congratulazioni")
                                        builder.setIcon(R.drawable.round_check_circle_24)
                                        builder.setMessage("Esame correttamente inserito nella carriera accademica")
                                        builder.setPositiveButton("OK") { _, _ ->
                                            Toast.makeText(
                                                context,
                                                "Si prega di aggiornare per visualizzare i cambiamenti",
                                                Toast.LENGTH_LONG).show()
                                        }
                                        builder.create().show()
                                    } else{
                                        Toast.makeText(
                                            context,
                                            "Errore durante la rimozione dell'esame dalla sessione",
                                            Toast.LENGTH_LONG).show()
                                    }
                                }

                            } else {
                                Log.e("CompletaEsame", "Errore")
                                try {
                                    Toast.makeText(
                                        context,
                                        "Inserire voto valido",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } catch (_: Exception) {
                                }
                            }
                        }
                }else {
                        Toast.makeText(context, "Inserire un voto valido", Toast.LENGTH_LONG).show()
                    }
            }
            builder.setNegativeButton("Annulla") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

            true
        }
    }

    override fun getItemCount(): Int {
        return examsList.size
    }
}