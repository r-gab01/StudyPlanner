package com.example.studyplanner

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ReplacementTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentProfileBinding
import com.example.studyplanner.model.DataSingleton
import com.example.studyplanner.viewModel.LegendFragment
import com.example.studyplanner.viewModel.LoginActivity
import com.example.studyplanner.viewModel.MainActivity
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences //ci serve per effettuare il logout e per la routine di studio
    private lateinit var imageViewProfile: ImageView
    private lateinit var imageButtonChangeImage: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottoneLogout = binding.logoutButton

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        //Implemento il Logout
        bottoneLogout.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.remove("Nome Utente")
            editor.remove("password")
            editor.remove("isLoggedIn")
            editor.apply()

            val singleton = DataSingleton.ottieniIstanza()
            singleton.reset()
            // Reindirizziamo l'utente alla schermata di accesso
            val i = Intent(requireContext(), LoginActivity::class.java)
            startActivity(i)
            requireActivity().finish() //Per terminare l'attività ospitante e tornare alla schermata di accesso.
        }

        //Se l'utente ha checkato la checkbox allora sfrutto i dati salvati nelle sharedPreferences
        var loggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
        val singleton = DataSingleton.ottieniIstanza()

        if (!loggedIn) {
            //Mostro nel riquadro nome utente e password dell'utente che ha fatto login. Uso il singleton "DataSingleton". Questo se l'utente non ha checkato la checkboc
            binding.editUsername.setText(singleton.nomeUtente)
            binding.textPass.setText(singleton.password)
        } else {
            val savedUsername = sharedPreferences.getString("Nome Utente", "")
            val savedPassword = sharedPreferences.getString("password", "")
            binding.editUsername.setText(savedUsername)
            binding.textPass.setText(savedPassword)
        }

        //Riempio grazie al singleton i riquadri relativi all'utente che ha fatto l'accesso
        binding.editName.setText(singleton.nome)
        binding.editSurname.setText(singleton.cognome)
        binding.editUniversity.setText(singleton.universita)
        binding.editCorso.setText(singleton.corsoStudi)

        val editProfile = binding.editProfileIcon
        var isEditMode = true

        val nomeCorsi = ArrayList<String?>()
        val idCorsi = ArrayList<Int?>()
        var idCorsoSelected: Int? = singleton.idCorso

        //Query per ottenere i corsi da DB e riempire l'elemento di selezione nella sezione "Corso di studio"
        ApiClient.selectCorsoStudio { data, error ->
            if (error != null) {
                Log.e("RECUPEROCORSI", "Si è verificato un errore: $error")
                Toast.makeText(
                    context,
                    "Errore durante la connessione al server",
                    Toast.LENGTH_LONG
                ).show()
            } else if (data != null) {
                for (i in data) {
                    nomeCorsi.add(i?.nomeCorso)
                    idCorsi.add(i?.idCorso)
                }
                val arrayAdapterCorso =
                    ArrayAdapter(requireContext(), R.layout.dropdown_item_profile, nomeCorsi)
                binding.editCorso.setAdapter(arrayAdapterCorso)
            } else {
                Log.e("RECUPEROCORSI", "Errore")
                Toast.makeText(
                    context,
                    "Errore durante la connessione al server",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //ottengo elemento selezionato nei 2 'spinner'
        binding.editCorso.setOnItemClickListener { _, _, position, _ ->
            idCorsoSelected = idCorsi[position]
        }

        editProfile.setOnClickListener {
            if (isEditMode) { //Abilitiamo le modifiche
                binding.editName.isEnabled = true
                binding.editSurname.isEnabled = true
                binding.editCorso.isEnabled = true
                binding.editUniversity.isEnabled = true
                binding.container.isEnabled = true

                editProfile.setImageResource(R.drawable.baseline_check_24)
                isEditMode = false
            } else {
                binding.editName.isEnabled = false
                binding.editSurname.isEnabled = false
                binding.editCorso.isEnabled = false
                binding.editUniversity.isEnabled = false
                binding.container.isEnabled = false
                editProfile.setImageResource(R.drawable.baseline_edit_24)

                //Prendiamo i nuovi dati scritti dall'utente
                val newNome = binding.editName.text.toString()
                val newCognome = binding.editSurname.text.toString()
                val newUni = binding.editUniversity.text.toString()
                //Prendiamo il nome utente che ci serve per fare la query
                val nomeU = binding.editUsername.text.toString()
                //Facciamo l'update dei dati anche nel DB
                Log.d("UPDATESTUD", "Utente ricevuto: $newNome")
                Log.d("UPDATESTUD", "Id ricevuto: $idCorsoSelected")
                ApiClient.updateStudente(
                    newNome,
                    newCognome,
                    newUni,
                    idCorsoSelected,
                    nomeU
                ) { boolean, error ->
                    if (error != null) {
                        // Gestisco l'errore
                        Log.e("UPDATESTUD", "Si è verificato un errore: $error")
                    } else if (boolean == true) {
                        // Utilizzo i dati restituiti e aggiorno il singleton
                        singleton.nome = newNome
                        singleton.cognome = newCognome
                        singleton.universita = newUni
                        //Eliminiamo la vecchia carriera dello studente
                        ApiClient.deletCarriera(nomeU, singleton.idCorso) { boolean, error ->
                            if (error != null) {
                                // Gestisco l'errore
                                Log.e("UPDATESTUD", "Si è verificato un errore: $error")
                            } else if (boolean == true) {
                                Log.d("UPDATESTUD", "Carriera eliminata correttamente!")

                                //Ricreiamo la nuova carriera con le nuove materie del nuovo corso di studi
                                ApiClient.selectMaterieCorso(idCorsoSelected) { data, error ->
                                    if (error != null) {
                                        Log.e(
                                            "REGISTRAZIONEFRAGMENT",
                                            "Si è verificato un errore: $error"
                                        )
                                        try {
                                            Toast.makeText(
                                                requireContext(),
                                                "Errore durante la connessione al server",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } catch (_: Exception) {
                                        }
                                    } else if (data != null) {
                                        for (i in data) {
                                            ApiClient.insCarriera(
                                                nomeU,
                                                i?.nomeMateria,
                                                idCorsoSelected,
                                                -1,
                                                0
                                            ) { response, error ->   //insert nella table Carriera
                                                if (error != null) {
                                                    Log.e(
                                                        "REGISTRAZIONEFRAGMENT",
                                                        "Si è verificato un errore: $error"
                                                    )
                                                    try {
                                                        Toast.makeText(
                                                            requireContext(),
                                                            "Errore durante la connessione al server",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    } catch (_: Exception) {
                                                    }
                                                } else if (response != null) {
                                                    Log.d(
                                                        "REGISTRAZONEFRAGMENT",
                                                        "Registrazione effettuata"
                                                    )
                                                } else {
                                                    Log.e(
                                                        "REGISTRAZIONEFRAGMENT",
                                                        "Errore in registra studente"
                                                    )
                                                    try {
                                                        Toast.makeText(
                                                            requireContext(),
                                                            "Errore durante la connessione al server",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    } catch (_: Exception) {
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        Log.e("REGISTRAZIONEFRAGMENT", "Errore")
                                        try {
                                            Toast.makeText(
                                                requireContext(),
                                                "Errore durante la connessione al server",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } catch (_: Exception) {

                                        }
                                    }
                                }

                            } else {
                                Log.d("UPDATESTUD", "Dati ricevuti: $boolean")
                            }
                        }
                        singleton.idCorso = idCorsoSelected
                        singleton.corsoStudi = binding.editCorso.text.toString()
                        Log.d("UPDATESTUD", "Utente ricevuto: $newNome")
                        Log.d("UPDATESTUD", "Id ricevuto: $idCorsoSelected")
                    } else {
                        Log.d("UPDATESTUD", "Dati ricevuti: $boolean")
                    }
                }
                isEditMode = true
            }
        }

        val editAccountButton = binding.modCred
        var isEditAccount = true

        editAccountButton.setOnClickListener {
            if (isEditAccount) {
                //  binding.editUsername.isEnabled=true
                binding.textPass.isEnabled = true

                editAccountButton.text = "Conferma modifiche"
                isEditAccount = false
            } else {
                // binding.editUsername.isEnabled=false
                binding.textPass.isEnabled = false
                editAccountButton.text = "Modifica credenziali"

                val newPassword = binding.textPass.text.toString()
                val nomeU = binding.editUsername.text.toString()
                //Facciamo l'update della pass anche nel DB
                ApiClient.updatePass(nomeU, newPassword) { boolean, error ->
                    if (error != null) {
                        // Gestisco l'errore
                        Log.e("UPDATEPASS", "Si è verificato un errore: $error")
                    } else if (boolean == true) {
                        // Utilizzo i dati restituiti e aggiorno le sharedPreferences
                        val editor = sharedPreferences.edit()
                        editor.putString("password", newPassword)
                        editor.apply()
                        //aggiorno anche il singleton
                        singleton.password = newPassword
                        Log.d("UPDATEPASS", "Boolean ricevuti: $boolean")
                    } else {
                        Log.d("UPDATEPASS", "Dati ricevuti: $boolean")
                    }
                }
                isEditAccount = true
            }
        }

        var buttonLegend = binding.legendButton

        val legendTag = "LegendFragment"

        buttonLegend.setOnClickListener {
            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            if (!fragmentExist(legendTag)) {  //verifico se già il fragment è stato aperto tramite questa funzione definita sotto
                transaction.replace(R.id.fragmentContainerView, LegendFragment(), legendTag)
                transaction.commit()
            }
        }

        //prendo i valori delle stringhe salvate sulle sharedPreferences utili per la routine di studio
        var lun = sharedPreferences.getString("isStudyLun", " ")
        var mar = sharedPreferences.getString("isStudyMar", " ")
        var mer = sharedPreferences.getString("isStudyMer", " ")
        var gio = sharedPreferences.getString("isStudyGio", " ")
        var ven = sharedPreferences.getString("isStudyVen", " ")
        var sab = sharedPreferences.getString("isStudySab", " ")
        var dom = sharedPreferences.getString("isStudyDom", " ")

        //Assegno una variabile per ogni icona relativo ad ogni giorno della settimana e chiamo la funzione per settare la routine di studio

        var giorni = ArrayList<String>()
        giorni.add(lun.toString())
        giorni.add(mar.toString())
        giorni.add(mer.toString())
        giorni.add(gio.toString())
        giorni.add(ven.toString())
        giorni.add(sab.toString())
        giorni.add(dom.toString())

        val editor = sharedPreferences.edit()
        //LUNEDI
        var monday = binding.iconMonday
        if(giorni[0] == "yes"){
            monday.setImageResource(R.drawable.baseline_menu_book_24)
        } else{
            monday.setImageResource(R.drawable.baseline_battery_charging_full_24)
        }
        monday.setOnClickListener {
            if (giorni[0] == "no") {
                monday.setImageResource(R.drawable.baseline_menu_book_24)
                giorni[0] = "yes"
                editor.putString("isStudyLun", "yes")
                editor.apply()
            } else {
                monday.setImageResource(R.drawable.baseline_battery_charging_full_24)
                giorni[0] = "no"
                editor.putString("isStudyLun", "no")
                editor.apply()
            }
        }
        //MARTEDI
        var tuesday = binding.iconTuesday
        if(giorni[1] == "yes"){
            tuesday.setImageResource(R.drawable.baseline_menu_book_24)
        } else{
            tuesday.setImageResource(R.drawable.baseline_battery_charging_full_24)
        }
        tuesday.setOnClickListener {
            if (giorni[1] == "no") {
                tuesday.setImageResource(R.drawable.baseline_menu_book_24)
                giorni[1] = "yes"
                editor.putString("isStudyMar", "yes")
                editor.apply()
            } else {
                tuesday.setImageResource(R.drawable.baseline_battery_charging_full_24)
                giorni[1] = "no"
                editor.putString("isStudyMar", "no")
                editor.apply()
            }
        }
        //MERCOLEDI
        var wednesday = binding.iconWednesday
        if(giorni[2] == "yes"){
            wednesday.setImageResource(R.drawable.baseline_menu_book_24)
        } else{
            wednesday.setImageResource(R.drawable.baseline_battery_charging_full_24)
        }
        wednesday.setOnClickListener {
            if (giorni[2] == "no") {
                wednesday.setImageResource(R.drawable.baseline_menu_book_24)
                giorni[2] = "yes"
                editor.putString("isStudyMer", "yes")
                editor.apply()
            } else {
                wednesday.setImageResource(R.drawable.baseline_battery_charging_full_24)
                giorni[2] = "no"
                editor.putString("isStudyMer", "no")
                editor.apply()
            }
        }
        //GIOVEDI
        var thursday = binding.iconThursday
        if(giorni[3] == "yes"){
            thursday.setImageResource(R.drawable.baseline_menu_book_24)
        } else{
            thursday.setImageResource(R.drawable.baseline_battery_charging_full_24)
        }
        thursday.setOnClickListener {
            if (giorni[3] == "no") {
                thursday.setImageResource(R.drawable.baseline_menu_book_24)
                giorni[3] = "yes"
                editor.putString("isStudyGio", "yes")
                editor.apply()
            } else {
                thursday.setImageResource(R.drawable.baseline_battery_charging_full_24)
                giorni[3] = "no"
                editor.putString("isStudyGio", "no")
                editor.apply()
            }
        }

        //VENERDI
        var friday = binding.iconFriday
        if(giorni[4] == "yes"){
            friday.setImageResource(R.drawable.baseline_menu_book_24)
        } else{
            friday.setImageResource(R.drawable.baseline_battery_charging_full_24)
        }
        friday.setOnClickListener {
            if (giorni[4] == "no") {
                friday.setImageResource(R.drawable.baseline_menu_book_24)
                giorni[4] = "yes"
                editor.putString("isStudyVen", "yes")
                editor.apply()
            } else {
                friday.setImageResource(R.drawable.baseline_battery_charging_full_24)
                giorni[4] = "no"
                editor.putString("isStudyVen", "no")
                editor.apply()
            }
        }
        //SABATO
        var saturday = binding.iconSaturday
        if(giorni[5] == "yes"){
            saturday.setImageResource(R.drawable.baseline_menu_book_24)
        } else{
            saturday.setImageResource(R.drawable.baseline_battery_charging_full_24)
        }
        saturday.setOnClickListener {
            if (giorni[5] == "no") {
                saturday.setImageResource(R.drawable.baseline_menu_book_24)
                giorni[5] = "yes"
                editor.putString("isStudySab", "yes")
                editor.apply()
            } else {
                saturday.setImageResource(R.drawable.baseline_battery_charging_full_24)
                giorni[5] = "no"
                editor.putString("isStudySab", "no")
                editor.apply()
            }
        }
        //DOMENICA
        var sunday = binding.iconSunday
        if(giorni[6] == "yes"){
            sunday.setImageResource(R.drawable.baseline_menu_book_24)
        } else{
            sunday.setImageResource(R.drawable.baseline_battery_charging_full_24)
        }
        sunday.setOnClickListener {
            if (giorni[6] == "no") {
                sunday.setImageResource(R.drawable.baseline_menu_book_24)
                giorni[6] = "yes"
                editor.putString("isStudyDom", "yes")
                editor.apply()
            } else {
                sunday.setImageResource(R.drawable.baseline_battery_charging_full_24)
                giorni[6] = "no"
                editor.putString("isStudyDom", "no")
                editor.apply()
            }
        }

        imageViewProfile = binding.imageProfile
        imageButtonChangeImage = binding.editIcon

        imageButtonChangeImage.setOnClickListener {

            // Verifica se il permesso di accesso alla galleria è stato già concesso
            if (isGalleryPermissionGranted()) {
                openGallery()
            } else {
                // Richiedi il permesso di accesso alla galleria
                requestGalleryPermission()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            if (selectedImageUri != null) {
                // Carica l'immagine selezionata nell'ImageView
                Glide.with(this)
                    .load(selectedImageUri)
                    .into(imageViewProfile)
            }
        }
    }

    companion object {
        private const val REQUEST_PERMISSION_GALLERY = 1
        private const val REQUEST_IMAGE_PICK = 2

    }

    private fun isGalleryPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestGalleryPermission() {
        Locale.setDefault(Locale("it"))
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_GALLERY)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_GALLERY -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    // Il permesso è stato negato dall'utente
                    Toast.makeText(requireContext(), "Permesso negato", Toast.LENGTH_SHORT).show()
                    requireFragmentManager().popBackStack()
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    fun fragmentExist(tag: String): Boolean {       //funzione che mi permette di trovare se un fragment è presente tramite il uso tag
        val fragmentManager = parentFragmentManager
        return (fragmentManager.findFragmentByTag(tag) != null)         //scrittura compatta che restituisce true o false se quella condizione si verifica o meno
    }
}

