package com.example.studyplanner

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.example.studyplanner.databinding.ActivityAddExamBinding
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

            val addButton = binding.buttonAdd

            var editTextDate = binding.insDate
            editTextDate.isFocusable = false

            editTextDate.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                //Mostriamo un date picker che inizialmente è settato alla data attuale
                val datePickerDialog =
                    DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                        editTextDate.setText(selectedDate)
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