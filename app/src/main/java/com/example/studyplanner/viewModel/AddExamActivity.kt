package com.example.studyplanner.viewModel

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.example.studyplanner.R
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.ActivityAddExamBinding
import com.example.studyplanner.model.DataSingleton
import java.util.*

class AddExamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExamBinding
    private val elementList: MutableList<View> = mutableListOf() //Variabile di istanza per memorizzare lo stato degli elementi.
    // Ci serve per mantenere lo stato una volta girato il dispositivo.
    private var lastElementIndex: Int = 0 //variabile di istanza per tenere traccia dell'indice dell'elemento aggiunto più recente

    override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            binding = ActivityAddExamBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val editTextList = mutableListOf<EditText>()
            var newEditText : EditText
            var idArgument = 1
            val selettoreMateria = binding.selMateria
            var materiaSel: String? = ""
            var cfuSel: Int? = -1
            var dataSel: String? = null
            var nomeLibroSel = binding.textNameBook
            var pagineSel = binding.textPageBook
            val nomiMaterie = ArrayList<String?>()
            val cfuMaterie = ArrayList<Int?>()

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

            ApiClient.selectMaterieCorso(DataSingleton.ottieniIstanza().idCorso) { data, error ->
                if (error != null) {
                    Log.e("ADDEXAM", "Si è verificato un errore: $error")
                    try {
                        Toast.makeText(
                            this,
                            "Errore durante la connessione al server",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (_: Exception) {

                    }
                } else if (data != null) {
                    for (i in data) {
                        nomiMaterie.add(i?.nomeMateria)
                        cfuMaterie.add(i?.cfu)
                    }
                    val arrayAdapterMaterie =
                        ArrayAdapter(this, R.layout.dropdown_item, nomiMaterie)
                    selettoreMateria.setAdapter(arrayAdapterMaterie)
                } else {
                    Log.e("ADDEXAM", "Errore")
                    try {
                        Toast.makeText(
                            this,
                            "Errore durante la connessione al server",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {

                    }
                }
            }

            binding.selMateria.setOnItemClickListener { _, _, position, _ ->
                materiaSel = nomiMaterie[position].toString()
                cfuSel = cfuMaterie[position]
                binding.textCfu.text = Editable.Factory.getInstance().newEditable(cfuSel.toString())
            }


            val addButton = binding.buttonAdd
            var editTextDate = binding.insDate
            editTextDate.isFocusable = false

            editTextDate.setOnClickListener {

                val year: Int
                val month: Int
                val dayOfMonth: Int

                val calendar = Calendar.getInstance()
                if (dataSel.isNullOrEmpty()) {
                    //Se la data salvata non esiste, utilizziamo la data attuale
                    year = calendar.get(Calendar.YEAR)
                    month = calendar.get(Calendar.MONTH)
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                } else {
                    // La data salvata esiste, la convertiamo in valori interi
                    val selectedDateParts =
                        dataSel!!.split("-") // la stringa dataSelezionata viene suddivisa utilizzando il separatore "/" tramite il metodo split("/"). Questo crea un array di stringhe selectedDateParts, contenente le parti della data (giorno, mese, anno) come elementi separati.
                    year =
                        selectedDateParts[0].toInt() //assegniamo il valore convertito dell'anno a year
                    month =
                        selectedDateParts[1].toInt() - 1 // Sottraiamo 1 al mese perché Calendar.MONTH parte da 0
                    dayOfMonth = selectedDateParts[2].toInt()
                }


                //Mostriamo un date picker che inizialmente è settato alla data attuale
                val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        val newSelectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"

                        editTextDate.setText(newSelectedDate)

                        // Memorizziamo la nuova data selezionata
                        dataSel = newSelectedDate
                    }, year, month, dayOfMonth)

                datePickerDialog.show()
            }


            val container = binding.linearLayout

            //gestisco l'aggiunta di argomenti della materia
            addButton.setOnClickListener {
                val layout = LinearLayout(this)
                layout.orientation = LinearLayout.HORIZONTAL

                // Scorrimento automatico verso il basso
                binding.scrollView.post {
                    binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                }

                newEditText = EditText(this)
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
                //gestisco bottone rimozione argomento
                val deleteButton = ImageButton(this)
                //deleteButton.setBackgroundResource(R.color.white)
                deleteButton.setImageResource(R.drawable.baseline_remove_24)
                deleteButton.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                deleteButton.setOnClickListener {
                    container.removeView(layout)
                    elementList.remove(layout)
                    editTextList.remove(newEditText)
                }
                newEditText.id = idArgument
                idArgument += 1
                Log.d("AddEXAM", newEditText.id.toString())
                deleteButton.id = View.generateViewId()
                layout.addView(newEditText)
                layout.addView(deleteButton)
                editTextList.add(newEditText)

                /*
                //Ogni volta che creiamo una nuova edit text e un nuovo bottone gli assegniamo un id che ci servirà nel ripristino dello stato
                newEditText.id = View.generateViewId()
                Log.d("AddEXAM", newEditText.id.toString())
                Log.d("AddEXAM", newEditText.text.toString())
                deleteButton.id = View.generateViewId()
                layout.addView(newEditText)
                layout.addView(deleteButton)

                 */

                container.addView(layout)
                //   elementList.add(layout)

            }

        binding.buttonConferma.setOnClickListener {

            val allEditTextValues = mutableListOf<String>()
            for (i in editTextList){
                val editTextValue = i.text.toString()
                allEditTextValues.add(editTextValue)
                Log.d("AddEXAM", allEditTextValues.toString())
            }
            if(cfuSel.toString().isNullOrEmpty() || materiaSel.isNullOrEmpty() || dataSel.isNullOrEmpty() ||
                nomeLibroSel.text.toString().isNullOrEmpty() || pagineSel.text.isNullOrEmpty()){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Errore")
                    builder.setIcon(R.drawable.baseline_error_24)
                    builder.setMessage("Inserire correttamente tutti i campi")
                    builder.setPositiveButton("OK"){_,_ -> }
                    builder.create().show()
            } else{

            }
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