package com.example.studyplanner.viewModel

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studyplanner.PreparationActivity
import com.example.studyplanner.R
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
        holder.image.setImageResource(R.drawable.baseline_library_books_24)
        holder.date.text = examViewModel.dataAppello.toString()
        holder.title.text = examViewModel.nomeMateria
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
    }

    override fun getItemCount(): Int {
        return examsList.size
    }
}