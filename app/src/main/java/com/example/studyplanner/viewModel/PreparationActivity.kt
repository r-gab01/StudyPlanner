package com.example.studyplanner.viewModel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.studyplanner.R
import com.example.studyplanner.databinding.ActivityPreparationBinding

class PreparationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreparationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityPreparationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //MOSTRA FRAGMENT INFO GENERALI
        val chipInfoGenerali=binding.chipInfoGenerali
        chipInfoGenerali.setOnClickListener {
            val manager=supportFragmentManager
            val transaction=manager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView,PreparationFragmentInfo())
            transaction.commit()
        }

        //MOSTRA FRAGMENT MATERIALE
        val chipMateriale=binding.chipMateriale
        chipMateriale.setOnClickListener {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, PreparationFragmentMateriale())
            transaction.commit()
        }
    }
}