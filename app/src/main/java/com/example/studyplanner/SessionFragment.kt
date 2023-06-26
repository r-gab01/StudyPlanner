package com.example.studyplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studyplanner.databinding.FragmentSessionBinding

class SessionFragment : Fragment() {
    lateinit var binding: FragmentSessionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSessionBinding.inflate(inflater)

        binding.confirmPageButton.setOnClickListener {
            val numPages = binding.editTextNumber.text
            // TODO: Salvare pagine fatte in memoria o su db
        }


        return binding.root
    }

}