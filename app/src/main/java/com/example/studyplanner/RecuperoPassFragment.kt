package com.example.studyplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studyplanner.databinding.FragmentRecuperoPassBinding


class RecuperoPassFragment : Fragment() {

    private lateinit var binding: FragmentRecuperoPassBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRecuperoPassBinding.inflate(inflater)

        return binding.root
    }
    }
