package com.example.studyplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.iwgang.countdownview.CountdownView
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

        val countdownView: CountdownView = binding.countdownView
        var durata: Int = 45
        countdownView.updateShow(durata*60*1000.toLong())

        var tempo: Long = (45 * 60 * 1000).toLong()
        binding.playTimerButton.setOnClickListener {
            if (!play && !continua) {
                countdownView.start(tempo)
                play = true
                continua = true
            }
            else if (play && continua) {
                countdownView.stop()
                play = false
                tempo = countdownView.remainTime
            }
            else if (!play && continua) {
                countdownView.start(tempo)
                play = true
                countdownView.updateShow(countdownView.remainTime)
            }
        }
        binding.stopTimerButton.setOnClickListener {
            countdownView.pause()
            countdownView.updateShow(durata*60*1000.toLong())
        }

        binding.
        countdownView.pause()

        return binding.root
    }


}