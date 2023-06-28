package com.example.studyplanner

import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.OneShotPreDrawListener.add
import com.example.studyplanner.databinding.FragmentProfileBinding



class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(inflater)

        val editProfile= binding.editProfileIcon

        var isEditMode = true

        editProfile.setOnClickListener{
            if(isEditMode){ //Abilitiamo le modifiche
                binding.editName.isEnabled=true
                binding.editSurname.isEnabled=true
                binding.editCorso.isEnabled=true
                binding.editUniversity.isEnabled=true

                editProfile.setImageResource(R.drawable.baseline_check_24)
                isEditMode=false
            } else{
                binding.editName.isEnabled=false
                binding.editSurname.isEnabled=false
                binding.editCorso.isEnabled=false
                binding.editUniversity.isEnabled=false

                editProfile.setImageResource(R.drawable.baseline_edit_24)
                isEditMode=true
            }
        }







        return binding.root
    }


}