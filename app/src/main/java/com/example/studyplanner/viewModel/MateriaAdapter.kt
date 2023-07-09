package com.example.studyplanner.viewModel

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studyplanner.R
import com.example.studyplanner.databinding.CalendarCardViewBinding
import com.example.studyplanner.databinding.MateriaCardViewBinding
import com.example.studyplanner.model.ExamModel
import com.example.studyplanner.model.MateriaModel



class MateriaAdapter(private val materieList: List<MateriaModel>,  private val context: Context) : RecyclerView.Adapter<MateriaAdapter.ViewHolder>() {

    class ViewHolder(binding: MateriaCardViewBinding): RecyclerView.ViewHolder(binding.root) {
        val cardView=binding.cardViewMateria
        val cfu = binding.editCfu
        val nomeMateria = binding.textNomeMateria
        val voto= binding.editVoto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = MateriaCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = MateriaAdapter.ViewHolder(view)

        return MateriaAdapter.ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val examViewModel = materieList[position]
        //prendo i valori dalla lista e li passo al viewHolder che li inserisce nelle CardView
        holder.cfu.text = materieList[position].cfu.toString()
        holder.nomeMateria.text = materieList[position].nomeMateria.toString()
        holder.voto.text = materieList[position].voto.toString()

        // Assegna un ID alla CardView
        val generatedId = View.generateViewId()
        holder.cardView.id = generatedId

        val colorResId = R.color.grey
        val color = ContextCompat.getColor(context, colorResId)


        if(holder.voto.text.toString()=="-1"){
            holder.cardView.setCardBackgroundColor(color)
            holder.voto.setText("")

        }


    }

    override fun getItemCount(): Int {
        return materieList.size
    }





}