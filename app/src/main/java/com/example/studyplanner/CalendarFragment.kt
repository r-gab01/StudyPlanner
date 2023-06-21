package com.example.studyplanner

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
        binding = FragmentCalendarBinding.inflate(inflater)
        binding.calendarView.setOnDateChangeListener { calendarView, anno, mese, giorno ->
            val dateSelected : String = "$giorno/$mese/$anno"

            val tagArgument = requireContext().getString(R.string.tagArgumentsEventFragment)        //tag che uso nel bundle per individuare dati passati
            val messageObj = Bundle()           //L'oggetto bundle viene utilizzato per passare dati tra diversi componenti in questo caso fragment.
            messageObj.putString(tagArgument , dateSelected)        //questo metodo mi permette di inserire i dati nel bundle
            //in questo caso la data selezionata dalla calendar view

            val tagFragment = "EventFragment"
            val fragmentManager = parentFragmentManager

            if (!fragmentExist(tagFragment)){
                val transaction = fragmentManager.beginTransaction()
                val fragmentDestinazione = EventFragment()              //Qui è dove passo realmente il bundle. Innanzitutto creo istanza del fragment di destinazione
                fragmentDestinazione.arguments = messageObj         //Alla nuova istanza setto gli ARGUMENTS, ossia il mio bundle, a questo punto posso aggiungerlo con una add

                transaction.add(R.id.fragment_container_CalendarEvent, fragmentDestinazione, tagFragment)             //qua nella ADD devo passare il fragmentDestianzione e non una nuova ulteriore istanza
                transaction.addToBackStack(tagFragment)
                transaction.commit()
            }else{                                              //ELSE (il fragment è gia caricato allora devo sostituirlo con una nuova istanza che ha gli Arguments
                val transaction = fragmentManager.beginTransaction()
                val fragmentDestinazione = EventFragment()              //Come sopra creo istanza e setto arguments
                fragmentDestinazione.arguments = messageObj
                transaction.replace(R.id.fragment_container_CalendarEvent, fragmentDestinazione, tagFragment)       //A differenza di prima faccio una replace
                transaction.commit()
            }
        }

        return binding.root
    }

    private fun fragmentExist(tag: String): Boolean {       //funzione che mi permette di trovare se un fragment è presente tramite il uso tag
        val fragmentManager = parentFragmentManager
        return (fragmentManager.findFragmentByTag(tag) != null)         //scrittura compatta che restituisce true o false se quella condizione si verifica o meno
    }

}