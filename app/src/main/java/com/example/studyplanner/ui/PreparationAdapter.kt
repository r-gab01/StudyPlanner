package com.example.studyplanner.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studyplanner.databinding.MaterialCardViewBinding

class PreparationAdapter(private val argumentList: ArrayList<String>): RecyclerView.Adapter<PreparationAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=MaterialCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return argumentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Prendo un’oggetto della lista alla posizione position
        val argomentoDaCercare = argumentList[position]

        holder.argument.text=argomentoDaCercare
        //ottendo il contesto dalla vista dell'elemento argument tramite il campo context di holder.argument
        val context=holder.argument.context

        holder.imageButton.setOnClickListener {
            val myUri: Uri = Uri.parse("${PreparationFragmentMateriale.SEARCH_PREFIX}${argomentoDaCercare}")
            val intent = Intent(Intent.ACTION_VIEW, myUri)
            context.startActivity(intent)
        }
    }

    class ViewHolder(binding:MaterialCardViewBinding): RecyclerView.ViewHolder(binding.root){
        val argument=binding.textArgomenti
        val imageButton=binding.imageviewCard
        }
}