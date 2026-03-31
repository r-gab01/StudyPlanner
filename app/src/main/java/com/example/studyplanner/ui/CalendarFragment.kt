package com.example.studyplanner.ui

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentCalendarBinding
import com.example.studyplanner.model.DataSingleton
import com.example.studyplanner.model.ExamModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
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

        val recyclerView = binding.recyclerViewMostraEsame
        val esami = ArrayList<ExamModel>()

        // Configura la RecyclerView che ci servirà per mostrare gli esami quando clicchiamo un giorno del calendario
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager


        calendar.dateTextAppearance = R.style.CustomCalendarViewStyle

        val nomiMaterie = ArrayList<String?>()
        //per ottenere il giorno attuale
        val Calendario = Calendar.getInstance()
        val currentDay = Calendario.get(Calendar.DAY_OF_MONTH)

        val infoSeEsame= binding.infoSeEsame

        val nomeU= DataSingleton.ottieniIstanza().nomeUtente
        var sharedPreferences= requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Ottiengo la data selezionata
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            // Ottiengo solo il giorno dalla data selezionata che ci serve dopo
            val day = selectedDate.get(Calendar.DAY_OF_MONTH)
            Log.d("CALENDAR", "Dati ricevuti: $day")

            //trasformiamo la data nel formato corretto
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            Log.d("CALENDAR", "Dati ricevuti: $formattedDate")
            Log.d("CALENDAR", "Dati ricevuti: $nomeU")
            val esami = ArrayList<ExamModel>()

            ApiClient.selectSessioneStudio(nomeU,formattedDate){ data, error ->
                if (error != null) {
                    // Gestisci l'errore
                    Log.e("CALENDAR", "Si è verificato un errore: $error")
                }else if (data != null) {
                    // Utilizza i dati restituiti
                    Log.d("CALENDAR", "Dati ricevuti: $data")

                   var giorniRimanenti:Int =day - currentDay
                    Log.d("prova", "${giorniRimanenti}")

                    binding.recyclerViewMostraEsame.visibility = View.VISIBLE

                    if(giorniRimanenti==0){
                        infoSeEsame.text= "Esame in data odierna:"
                        infoSeEsame.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    } else{  if(giorniRimanenti<0){
                        infoSeEsame.text= "Esami già sostenuti in data ${formattedDate}:"
                        infoSeEsame.setTextColor(ContextCompat.getColor(requireContext(), R.color.Red))
                    }else {
                        infoSeEsame.text = "Esami a cui sei prenotato:"
                        infoSeEsame.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    }
                        }

                    for (i in data){
                        nomiMaterie.add(i?.nomeMateria)
                        esami.add(ExamModel(i?.nomeMateria,formattedDate,giorniRimanenti,0))
                    }
                    val adapter = CalendarAdapter(esami)
                    recyclerView.adapter = adapter

                }else{
                    binding.recyclerViewMostraEsame.visibility = View.INVISIBLE
                    infoSeEsame.text= "Nessun esame fissato in questo giorno!"
                    infoSeEsame.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
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