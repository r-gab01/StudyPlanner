package com.example.studyplanner.viewModel

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studyplanner.PreparationActivity
import com.example.studyplanner.R
import com.example.studyplanner.databinding.CalendarCardViewBinding
import com.example.studyplanner.databinding.ExamsCardViewBinding
import com.example.studyplanner.model.ExamModel

class CalendarAdapter(private val examsList: List<ExamModel>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(binding: CalendarCardViewBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.titleEsame
        val remainingDays = binding.GiorniRim
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarAdapter.ViewHolder {
        val view = CalendarCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = CalendarAdapter.ViewHolder(view)

        return CalendarAdapter.ViewHolder(view)

    }

    override fun onBindViewHolder(holder: CalendarAdapter.ViewHolder, position: Int) {
        val examViewModel = examsList[position]     //variabile contenente una examList ( ossia lista di examViewModel)
        //prendo i valori dalla lista e li passo al viewHolder che li inserisce nelle CardView
        holder.title.text = examsList[position].title
        holder.remainingDays.text = examsList[position].remainingDays.toString()
    }

    override fun getItemCount(): Int {
        return examsList.size
    }



}