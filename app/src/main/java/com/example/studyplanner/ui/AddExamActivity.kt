package com.example.studyplanner.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.pm.ActivityInfo
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
import java.security.MessageDigest
import java.util.*

class AddExamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExamBinding
    private   var TextList: MutableList<View> = mutableListOf()

    private val elementList: MutableList<View> =
        mutableListOf() //Variabile di istanza per memorizzare lo stato degli elementi.

    // Ci serve per mantenere lo stato una volta girato il dispositivo.
    private var lastElementIndex: Int =
        0 //variabile di istanza per tenere traccia dell'indice dell'elemento aggiunto più recente

    private lateinit var containerLayout: LinearLayout
    private var editTextCounter = 0
    private val editTextValues: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityAddExamBinding.inflate(layoutInflater)
        requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)

        var idSessione: Int
        val editTextList = mutableListOf<EditText>()
        var posizioneMateria: Int = -1
        var newEditText: EditText
        var idArgument = 1
        val selettoreMateria = binding.selMateria
        var materiaSel: String? = ""
        var cfuSel: Int? = -1
        var dataSel: String? = null
        val nomeLibroSel = binding.textNameBook
        val pagineSel = binding.textPageBook
        val addButton = binding.buttonAdd
        val editTextDate = binding.insDate
        val nomiMaterie = ArrayList<String?>()
        val cfuMaterie = ArrayList<Int?>()


        val container = binding.linearLayout


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
                } catch (_: Exception) {

                }
            }
        }
        if (savedInstanceState != null) {
            editTextValues.addAll(savedInstanceState.getStringArrayList("edit_text_values") ?: emptyList())
            restoreEditTexts()
        }

        selettoreMateria.setOnItemClickListener { _, _, position, _ ->
            materiaSel = nomiMaterie[position].toString()
            cfuSel = cfuMaterie[position]
            posizioneMateria = position
            binding.textCfu.text = Editable.Factory.getInstance().newEditable(cfuSel.toString())
        }

        /*
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
                val selectedDateParts = dataSel!!.split("-") // la stringa dataSelezionata viene suddivisa utilizzando il separatore "/" tramite il metodo split("/"). Questo crea un array di stringhe selectedDateParts, contenente le parti della data (giorno, mese, anno) come elementi separati.
                year = selectedDateParts[0].toInt() //assegniamo il valore convertito dell'anno a year
                month = selectedDateParts[1].toInt() - 1 // Sottraiamo 1 al mese perché Calendar.MONTH parte da 0
                dayOfMonth = selectedDateParts[2].toInt()
            }
            //Mostriamo un date picker che inizialmente è settato alla data attuale
            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val newSelectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                    editTextDate.setText(newSelectedDate)
                    // Memorizziamo la nuova data selezionata
                    dataSel = newSelectedDate
                }, year, month, dayOfMonth)
            datePickerDialog.show()
        }
         */

        editTextDate.isFocusable = false
        editTextDate.setOnClickListener {
            val year: Int
            val month: Int
            val dayOfMonth: Int
            var calendar = Calendar.getInstance()
            var today = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                    val selected = Calendar.getInstance()
                    selected.set(selectedYear, selectedMonth, selectedDayOfMonth)

                    if (selected.before(today)) {
                        // La data selezionata è precedente a oggi
                        Toast.makeText(this, "Seleziona una data valida", Toast.LENGTH_SHORT).show()
                    } else {
                        // La data selezionata è valida
                        dataSel = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                        editTextDate.setText(dataSel)
                    }
                }, year, month, dayOfMonth)
            datePickerDialog.show()
        }


        //gestisco l'aggiunta di argomenti della materia
        addButton.setOnClickListener {

            editTextValues.add("") // Aggiungiamo un valore vuoto alla lista
            editTextCounter++

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

            container.addView(layout)


        }

        binding.buttonConferma.setOnClickListener {
            //prendo tutte le stringhe degli argomenti inseriti
            val allEditTextValues = mutableListOf<String>()
            for (i in editTextList) {
                if(!i.text.isNullOrEmpty()) {
                    val editTextValue = i.text.toString()
                    allEditTextValues.add(editTextValue)
                    Log.d("AddEXAM", allEditTextValues.toString())
                }
            }
            //controllo se tutti i campi sono stati inseriti
            if (cfuSel.toString()
                    .isEmpty() || materiaSel.isNullOrEmpty() || dataSel.isNullOrEmpty() ||
                nomeLibroSel.text.toString().isEmpty() || pagineSel.text.isNullOrEmpty()
            ) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Errore")
                builder.setIcon(R.drawable.baseline_error_24)
                builder.setMessage("Inserire correttamente tutti i campi")
                builder.setPositiveButton("OK") { _, _ -> }
                builder.create().show()
            }else {    // effettuo caricamento sul server
                idSessione = generateUniqueId(dataSel.toString(),posizioneMateria)
                ApiClient.insInSessione(
                    idSessione,
                    DataSingleton.ottieniIstanza().nomeUtente.toString(),
                    materiaSel, DataSingleton.ottieniIstanza().idCorso, dataSel,
                    nomeLibroSel.text.toString(), pagineSel.text.toString().toInt(), 0
                ) { res1, err1 ->
                    if (err1 != null) {
                        Log.e("AddEXAM", err1.toString())
                        try {
                            Toast.makeText( this,"Errore durante la connessione al server", Toast.LENGTH_LONG).show()
                        } catch (_: Exception) {
                        }
                    } else if (res1 != null) {
                        //se l'inserimento va a buon fine ottengo l'id della sessione
                        Log.d("AddEXAM", "Inserimento sessione riuscito")
                    } else {
                        Log.e("AddEXAM", "Errore nell'inserimento sessione")
                        try {
                            Toast.makeText( this,"Errore durante l'inserimento", Toast.LENGTH_LONG).show()
                        } catch (_: Exception) {
                        }
                    }
                }
                for (i in allEditTextValues) {
                    ApiClient.insArg(idSessione,i){ res2, err2->
                        if (err2 != null) {
                            Log.e("AddEXAM", err2.toString())
                            try {
                                Toast.makeText( this,"Errore durante la connessione al server", Toast.LENGTH_LONG).show()
                            } catch (_: Exception) {
                            }
                        } else if (res2 != null) {
                            Log.d("AddEXAM", "Inserimento argomento riuscito")
                        } else {
                            Log.e("AddEXAM", "Errore nell'inserimento sessione")
                            try {
                                Toast.makeText( this,"Errore durante l'inserimento", Toast.LENGTH_LONG).show()
                            } catch (_: Exception) {
                            }
                        }
                    }

                }
                finish()
            }
        }
    }

override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putStringArrayList("edit_text_values", ArrayList(editTextValues))
}

    //Il metodo onRestoreInstanceState viene chiamato dopo che l'activity è stata ricreata a seguito di un cambio di configurazione o di un ripristino dello stato.
    // Questo metodo ti consente di ripristinare lo stato salvato in precedenza.
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val editTextValues = savedInstanceState.getStringArrayList("editTextValues")
        var container=binding.linearLayout
        if (editTextValues != null) {
            for (i in editTextValues.indices) {
                val editText = EditText(this)
                editText.setText(editTextValues[i])
                // ... Aggiungi le impostazioni necessarie per l'editText
                container.addView(editText)
                TextList.add(editText)
            }
        }
    }




    fun generateUniqueId(date: String, position: Int): Int { //funzione hash che sfrutta SHA-256 per ottenere un id univoco per la materia
                                                            // della sessione tramite la posizione della materia nell'array e data esame
        // Concatenazione della data e del valore intero in una stringa
        val inputString = "$date$position"
        // Creazione dell'oggetto MessageDigest utilizzando SHA-256
        val digest = MessageDigest.getInstance("SHA-256")
        // Calcolo dell'hash
        val hashBytes = digest.digest(inputString.toByteArray())
        // Conversione dell'hash in un valore intero
        val uniqueId = hashBytes.fold(0) { acc, byte -> (acc shl 8) + (byte.toInt() and 0xFF) }
        return uniqueId
    }

    private fun restoreEditTexts() {
        val container = binding.linearLayout
        for (textValue in editTextValues) {
            val editText = EditText(this)
            editText.hint = "Aggiungi un argomento"
            editText.setText(textValue)
            container.addView(editText)
        }
    }


}