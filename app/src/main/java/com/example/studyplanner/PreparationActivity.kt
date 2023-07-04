package com.example.studyplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.studyplanner.databinding.ActivityPreparationBinding

class PreparationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreparationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityPreparationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}