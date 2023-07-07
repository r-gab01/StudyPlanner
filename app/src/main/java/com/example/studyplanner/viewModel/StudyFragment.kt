package com.example.studyplanner.viewModel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.iwgang.countdownview.CountdownView
import com.example.studyplanner.R
import com.example.studyplanner.databinding.FragmentStudyBinding


class StudyFragment : Fragment() {
    private var play: Boolean = false
    private var continua: Boolean = false
    private lateinit var binding: FragmentStudyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyBinding.inflate(inflater)


        //GESTISCO IL TIMER DELLA LIBRERIA COUNTDOWN VIEW
        val countdownView: CountdownView = binding.countdownView
        var durata: Int = 45                //questa variabile indica i minuti che l'utente vorrà impostare
        var tempo: Long = (durata * 60 * 1000).toLong()
        countdownView.updateShow(tempo)
        binding.playTimerButton.setOnClickListener {        //Da qua Gestisco la logica di quando clicca play e pausa, stoppando il timer e aggiornando la view
            if (!play && !continua) {
                countdownView.start(tempo)
                binding.playTimerButton.setImageResource(R.drawable.baseline_pause_24)
                play = true
                continua = true
            }
            else if (play && continua) {
                countdownView.stop()
                binding.playTimerButton.setImageResource(R.drawable.baseline_play_arrow_24)
                play = false
                tempo = countdownView.remainTime
            }
            else if (!play && continua) {
                countdownView.start(tempo)
                binding.playTimerButton.setImageResource(R.drawable.baseline_pause_24)
                play = true
                countdownView.updateShow(countdownView.remainTime)
            }
        }
        binding.stopTimerButton.setOnClickListener {    //Qua gestico la logica del tasto Stop che riavvia il timer
            countdownView.pause()
            countdownView.updateShow(durata*60*1000.toLong())
            tempo = countdownView.remainTime
        }

        //GESTISCO IL BOTTONE TERMINA SESSIONE
        binding.buttonTermina.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val transaction = fragmentManager.beginTransaction()
            val sessionTag = "sessionFragment"
            if (!fragmentExist(sessionTag)) {  //verifico se già il fragment è stato aperto tramite questa funzione definita sotto
                transaction.replace(R.id.sessionContainerView, SessionFragment(), sessionTag)
                transaction.commit()
            }else{
                transaction.remove(fragmentManager.findFragmentByTag(sessionTag)!!)
                transaction.commit()
            }
        }


        return binding.root
    }

    fun fragmentExist(tag: String): Boolean {       //funzione che mi permette di trovare se un fragment è presente tramite il uso tag
        val fragmentManager = parentFragmentManager
        return (fragmentManager.findFragmentByTag(tag) != null)         //scrittura compatta che restituisce true o false se quella condizione si verifica o meno
    }

}