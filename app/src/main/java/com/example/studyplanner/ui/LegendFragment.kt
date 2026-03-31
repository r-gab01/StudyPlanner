package com.example.studyplanner.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studyplanner.ProfileFragment
import com.example.studyplanner.R
import com.example.studyplanner.databinding.FragmentLegendBinding


class LegendFragment : Fragment() {

    private lateinit var binding: FragmentLegendBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLegendBinding.inflate(inflater)

        val profileTag = "ProfileFragment"

        binding.buttonReturn.setOnClickListener{
            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            if (!fragmentExist(profileTag)) {  //verifico se già il fragment è stato aperto tramite questa funzione definita sotto
                transaction.replace(R.id.fragmentContainerView, ProfileFragment(), profileTag)
                transaction.commit()
            }
        }

        return binding.root
    }

    fun fragmentExist(tag: String): Boolean {       //funzione che mi permette di trovare se un fragment è presente tramite il uso tag
        val fragmentManager = parentFragmentManager
        return (fragmentManager.findFragmentByTag(tag) != null)         //scrittura compatta che restituisce true o false se quella condizione si verifica o meno
    }


    }
