package com.example.studyplanner.viewModel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studyplanner.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentCalendarBinding.inflate(inflater)

        val ExamButton=binding.addExamButton

        ExamButton.setOnClickListener{
            val i= Intent(requireContext(), AddExamActivity:: class.java)
            startActivity(i)
        }

        return binding.root
    }



}