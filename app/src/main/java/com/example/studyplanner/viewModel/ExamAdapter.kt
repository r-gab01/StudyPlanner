package com.example.studyplanner.viewModel

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studyplanner.PreparationActivity
import com.example.studyplanner.model.ExamModel
import com.example.studyplanner.R
import com.example.studyplanner.databinding.ExamsCardViewBinding

class ExamAdapter(private val examsList: List<ExamModel>) : RecyclerView.Adapter<ExamAdapter.ViewHolder>() {

    class ViewHolder(binding: ExamsCardViewBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.titleCardView
        val date = binding.dataEsameTextView
        val remainingDays = binding.GiorniRimTextView
        val image = binding.imageviewCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ExamsCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = ViewHolder(view)
        view.cardView.setOnClickListener {
            //viene ottenuto il contesto (context) dalla View su cui è stato effettuato il clic
            val context = it.context
            val intent = Intent(context, PreparationActivity::class.java)
            context.startActivity(intent)
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val examViewModel = examsList[position]     //variabile contenente una examList ( ossia lista di examViewModel)
        //prendo i valori dalla lista e li passo al viewHolder che li inserisce nelle CardView
        if (examsList[position].tipo==0)        //se il tipo è 0 allora è un esame, altrimenti sono esercizi
            holder.image.setImageResource(R.drawable.baseline_library_books_24)
        else
            holder.image.setImageResource(R.drawable.baseline_edit_note_24)
        holder.date.text = examsList[position].date
        holder.title.text = examsList[position].title
        holder.remainingDays.text = examsList[position].remainingDays.toString()

    }

    override fun getItemCount(): Int {
        return examsList.size
    }
}