package com.example.studyplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studyplanner.databinding.FragmentCalendarBinding
import com.example.studyplanner.databinding.FragmentEventBinding

class EventFragment : Fragment() {

    private lateinit var binding: FragmentEventBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater)

        //gestisco arguments
        val tagArgument = requireContext().getString(R.string.tagArgumentsEventFragment)        //tag che uso nel bundle per individuare dati passati
        val receivedBundle = arguments
        val receivedString = receivedBundle?.getString(tagArgument)
        if (receivedString != null){
            binding.EventTextView.text = receivedString
        }

        return binding.root
    }

}