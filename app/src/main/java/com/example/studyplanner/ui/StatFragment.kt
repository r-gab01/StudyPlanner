package com.example.studyplanner.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentStatBinding
import com.example.studyplanner.model.DataSingleton
import com.example.studyplanner.model.ExamModel
import com.example.studyplanner.model.MateriaModel
import java.util.ArrayList

class StatFragment : Fragment() {

    private lateinit var binding: FragmentStatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentStatBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


                    val recyclerMaterie= binding.recyclerMateria
                    val materie = ArrayList<MateriaModel>()

                    val layoutManager = LinearLayoutManager(requireContext())
                    recyclerMaterie.layoutManager = layoutManager
                    var cfuTotali:Int=0
                    var totVoti=0.00
                    var totMaterie=0 //Numero totali di materie presenti nel libretto
                    var numMaterie=0 //Numero di materie date in quel momento
                    var mediaAr: Double=0.00
                    var prodotto=0 //prodotto tra voto e cfu che mi serve per calcolare la media ponderata
                    var temp=0
                    var mediaPo: Double=0.00
                    var percentualeM:Float=0f //Percentuale mancante alla laurea
                    var percentualeF:Int=0


                    //faccio la query che mi serve per riempire la recycler view
                    ApiClient.selectCarriera(DataSingleton.ottieniIstanza().nomeUtente){ data, error ->
                        if (error != null) {
                            Log.e("STATFRAGMENT", "Si è verificato un errore: $error")
                            try {
                                Toast.makeText(requireContext(),"Errore durante la connessione al server",Toast.LENGTH_LONG).show()
                            } catch (_: Exception) {

                            }
                        } else if (data != null) {
                            for (i in data) {
                                Log.d("STATFRAGMENT", i.toString())
                                materie.add(MateriaModel(i?.cfu,i?.nomeMateria,i?.voto,i?.lode))
                                totMaterie++
                                if(i?.voto != -1 ){
                                    cfuTotali+= i!!.cfu
                                    totVoti+= i!!.voto
                                    numMaterie++
                                    //I prossimi due calcoli mi servono nel calcolo della media ponderata
                                    prodotto = if (i?.voto != null) i.voto * i.cfu else 0
                                     temp+= prodotto
                                }
                                binding.cfuTotali.setText("${cfuTotali}")

                            }

                            //mediaAritmetica
                            mediaAr= (totVoti)/numMaterie
                            val primaDueCifre = String.format("%.2f", mediaAr)//Stampo solo le prime due cifre decimali
                            if(primaDueCifre != "NaN") {
                                binding.numMediaAritmetica.setText("${primaDueCifre}")
                            }else{
                                binding.numMediaAritmetica.setText("-")
                            }

                            //Media Ponderata
                            mediaPo=((temp)/cfuTotali.toDouble())
                            val mediaFinale = String.format("%.2f", mediaPo)
                            if(mediaFinale != "NaN") {
                                binding.numMediaPonderata.setText("${mediaFinale}")
                            }else{
                                binding.numMediaPonderata.setText("-")
                            }

                            //CALCOLO LA PERCENTUALE
                            percentualeM = ((numMaterie.toFloat()/totMaterie.toFloat())) * 100
                            percentualeF = percentualeM.toInt()
                            binding.numPercentuale.setText("${percentualeF} %")
                            binding.barPercentualeLaurea.progress=percentualeF.toInt()

                            val adapter = MateriaAdapter(materie,requireContext())
                            recyclerMaterie.adapter = adapter
                        } else {
                            Log.e("STATFRAGMENT", "Errore")
                            try {
                                Toast.makeText(requireContext(),"Errore durante la connessione al server",Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {

                            }
                        }
                    }


                }




    }

