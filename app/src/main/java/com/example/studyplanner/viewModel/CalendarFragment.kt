package com.example.studyplanner.viewModel

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentCalendarBinding
import com.example.studyplanner.model.DataSingleton
import java.util.*


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


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar= binding.calendarView

        val nomeU= DataSingleton.ottieniIstanza().nomeUtente
        var sharedPreferences= requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        //   val nomeU: String? = sharedPreferences.getString("Nome Utente", "")

        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Ottiengo la data selezionata
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            //trasformiamo la data nel formato corretto
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            Log.d("CALENDAR", "Dati ricevuti: $formattedDate")
            Log.d("CALENDAR", "Dati ricevuti: $nomeU")

            ApiClient.selectSessioneStudio(nomeU,formattedDate){ data, error ->
                if (error != null) {
                    // Gestisci l'errore
                    Log.e("CALENDAR", "Si è verificato un errore: $error")
                }else if (data != null) {
                    // Utilizza i dati restituiti
                    Log.d("CALENDAR", "Dati ricevuti: $data")
                    //salvo il corso nel Singleton
                    val singleton= DataSingleton.ottieniIstanza()

                }else{
                    Log.d("CALENDAR", "Dati ricevuti: $data")
                }
            }
        }



        val ExamButton=binding.addExamButton

        ExamButton.setOnClickListener{
            val i= Intent(requireContext(), AddExamActivity:: class.java)
            startActivity(i)
        }
    }

}