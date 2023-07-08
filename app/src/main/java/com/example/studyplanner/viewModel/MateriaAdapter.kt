package com.example.studyplanner.viewModel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studyplanner.databinding.CalendarCardViewBinding
import com.example.studyplanner.databinding.MateriaCardViewBinding
import com.example.studyplanner.model.ExamModel
import com.example.studyplanner.model.MateriaModel

class MateriaAdapter(private val materieList: List<MateriaModel>) : RecyclerView.Adapter<MateriaAdapter.ViewHolder>() {

    class ViewHolder(binding: MateriaCardViewBinding): RecyclerView.ViewHolder(binding.root) {
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

    }

    override fun getItemCount(): Int {
        return materieList.size
    }





}