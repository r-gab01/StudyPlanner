package com.example.studyplanner.viewModel

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.ResourcesCompat
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.ActivityAddExamBinding
import java.util.*

class AddExamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExamBinding
    private val elementList: MutableList<View> = mutableListOf() //Variabile di istanza per memorizzare lo stato degli elementi.
    // Ci serve per mantenere lo stato una volta girato il dispositivo.
    private var lastElementIndex: Int = 0 //variabile di istanza per tenere traccia dell'indice dell'elemento aggiunto più recente
    val selettoreMateria = binding.selMateria
    val materiaSel: String? = ""
    val nomiMaterie = ArrayList<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verifico se esiste uno stato salvato
        if (savedInstanceState != null) {
            // Ripristina lo stato degli elementi
            val savedElementList = savedInstanceState.getSparseParcelableArray<Parcelable>("elementList")
            savedElementList?.let {
                for (i in 0 until it.size()) {
                    val view = it.valueAt(i) as View
                    elementList.add(view)
                }
            }
            // Ricostruisco gli elementi nell'interfaccia utente
            recreateElements()
        }

        ApiClient.selectMaterieCorso(){ data, error ->
            if (error != null){
                Log.e("REGISTRAZIONEFRAGMENT", "Si è verificato un errore: $error")
                try {
                    Toast.makeText(requireContext(),"Errore durante la connessione al server", Toast.LENGTH_LONG).show()
                } catch (e: Exception){

                }
            }else if (data != null){
                for (i in data){
                    nomiMaterie.add(i?.nomeMateria)
                }
                val arrayAdapterMaterie = ArrayAdapter(this, R.layout.dropdown_item, nomiMaterie)
                selettoreMateria.setAdapter(arrayAdapterMaterie)
            }
            else{
                Log.e("REGISTRAZIONEFRAGMENT", "Errore")
                try {
                    Toast.makeText(this,"Errore durante la connessione al server", Toast.LENGTH_LONG).show()
                } catch (e: Exception){

                }
            }
        }
            val addButton = binding.buttonAdd

            var editTextDate = binding.insDate
            editTextDate.isFocusable = false

            // Dichiarazione della variabile globale per memorizzare la data selezionata
             var dataSelezionata: String? = null

            editTextDate.setOnClickListener {
                // utilizziamo le SharedPreferences per memorizzare e recuperare successivamente la data selezionata.
                // val sharedPreferences = getSharedPreferences("DatePicker", Context.MODE_PRIVATE)
                // val dataSelezionata = sharedPreferences.getString("selectedDate", "")

                val year: Int
                val month: Int
                val dayOfMonth: Int

                val calendar = Calendar.getInstance()

                if (dataSelezionata.isNullOrEmpty()) {
                    //Se la data salvata non esiste, utilizziamo la data attuale
                    year = calendar.get(Calendar.YEAR)
                    month = calendar.get(Calendar.MONTH)
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                } else {
                    // La data salvata esiste, la convertiamo in valori interi
                    val selectedDateParts =
                        dataSelezionata!!.split("/") // la stringa dataSelezionata viene suddivisa utilizzando il separatore "/" tramite il metodo split("/"). Questo crea un array di stringhe selectedDateParts, contenente le parti della data (giorno, mese, anno) come elementi separati.
                    year =
                        selectedDateParts[2].toInt() //assegniamo il valore convertito dell'anno a year
                    month =
                        selectedDateParts[1].toInt() - 1 // Sottraiamo 1 al mese perché Calendar.MONTH parte da 0
                    dayOfMonth = selectedDateParts[0].toInt()
                }


                //Mostriamo un date picker che inizialmente è settato alla data attuale
                val datePickerDialog =
                    DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        val newSelectedDate =
                            "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                        editTextDate.setText(newSelectedDate)

                        // Salviamo la data selezionata
                        //val editor = sharedPreferences.edit()
                        //editor.putString("selectedDate", selectedDate)
                        //editor.apply()

                        // Memorizziamo la nuova data selezionata
                        dataSelezionata = newSelectedDate
                    }, year, month, dayOfMonth)

                datePickerDialog.show()
            }


            val container = binding.linearLayout


            addButton.setOnClickListener {
                val layout = LinearLayout(this)
                layout.orientation = LinearLayout.HORIZONTAL

                // Scorrimento automatico verso il basso
                binding.scrollView.post {
                    binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                }

                val newEditText = EditText(this)
                val typeface = ResourcesCompat.getFont(this, R.font.montserrat_light)
                newEditText.typeface = typeface
                newEditText.hint = "Aggiungi un argomento"
                val textSize = 18.0f
                newEditText.textSize = textSize
                newEditText.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )

                val deleteButton = ImageButton(this)
                //deleteButton.setBackgroundResource(R.color.white)
                deleteButton.setImageResource(R.drawable.baseline_remove_24) // Imposta l'immagine desiderata
                deleteButton.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                deleteButton.setOnClickListener {
                    container.removeView(layout)
                    elementList.remove(layout)
                }

                //Ogni volta che creiamo una nuova edit text e un nuovo bottone gli assegniamo un id che ci servirà nel ripristino dello stato
                newEditText.id = View.generateViewId()
                deleteButton.id = View.generateViewId()


                layout.addView(newEditText)
                layout.addView(deleteButton)

                container.addView(layout)
             //   elementList.add(layout)

            }
        }

        //Sovrascrivo il metodo onSaveInstanceState() per salvare solo l'indice dell'ultimo elemento aggiunto
        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putInt("lastElementIndex", lastElementIndex)
        }

        //Sovrascrivo il metodo onRestoreInstanceState() per ripristinare lo stato dell'elementList utilizzando l'indice salvato
        override fun onRestoreInstanceState(savedInstanceState: Bundle) {
            super.onRestoreInstanceState(savedInstanceState)
            lastElementIndex = savedInstanceState.getInt("lastElementIndex")
            recreateElements()
        }

        // Nel metodo recreateElements(), itero sull'elementList e ricreo gli elementi nell'interfaccia utente a partire dall'indice salvato
        fun recreateElements() {
            binding.linearLayout.removeAllViews()
            for (i in 0..lastElementIndex) {
                val element = elementList[i]
                binding.linearLayout.addView(element)
            }
        }



}