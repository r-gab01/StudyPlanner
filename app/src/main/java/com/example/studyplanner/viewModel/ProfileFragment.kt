package com.example.studyplanner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ReplacementTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studyplanner.database.ApiClient
import com.example.studyplanner.databinding.FragmentProfileBinding
import com.example.studyplanner.model.DataSingleton
import com.example.studyplanner.viewModel.LegendFragment
import com.example.studyplanner.viewModel.LoginActivity
import com.example.studyplanner.viewModel.MainActivity


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences //ci serve per effettuare il logout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(inflater)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottoneLogout= binding.logoutButton

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        //Implemento il Logout
        bottoneLogout.setOnClickListener{
            val editor = sharedPreferences.edit()
            editor.remove("Nome Utente")
            editor.remove("password")
            editor.remove("isLoggedIn")
            editor.apply()

            val singleton= DataSingleton.ottieniIstanza()
            singleton.reset()
            // Reindirizziamo l'utente alla schermata di accesso
            val i = Intent(requireContext(), LoginActivity::class.java)
            startActivity(i)
            requireActivity().finish() //Per terminare l'attività ospitante e tornare alla schermata di accesso.
        }


        //Se l'utente ha checkato la checkbox allora sfrutto i dati salvati nelle sharedPreferences
        var loggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
        val singleton= DataSingleton.ottieniIstanza()

        if(!loggedIn){
            //Mostro nel riquadro nome utente e password dell'utente che ha fatto login. Uso il singleton "DataSingleton". Questo se l'utente non ha checkato la checkboc
            binding.editUsername.setText(singleton.nomeUtente)
            binding.textPass.setText(singleton.password)
        }else{
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

        val editProfile= binding.editProfileIcon
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
                val arrayAdapterCorso = ArrayAdapter(requireContext(), R.layout.dropdown_item_profile, nomeCorsi)
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


        editProfile.setOnClickListener{
            if(isEditMode){ //Abilitiamo le modifiche
                binding.editName.isEnabled=true
                binding.editSurname.isEnabled=true
                binding.editCorso.isEnabled=true
                binding.editUniversity.isEnabled=true
                binding.container.isEnabled=true

                editProfile.setImageResource(R.drawable.baseline_check_24)
                isEditMode=false
            } else{
                binding.editName.isEnabled=false
                binding.editSurname.isEnabled=false
                binding.editCorso.isEnabled=false
                binding.editUniversity.isEnabled=false
                binding.container.isEnabled=false
                editProfile.setImageResource(R.drawable.baseline_edit_24)


                //ottengo elemento selezionato nei 2 'spinner'
         /*       binding.editCorso.setOnItemClickListener { _, _, position, _ ->
                    idCorsoSelected = idCorsi[position]
                } */

                //Prendiamo i nuovi dati scritti dall'utente
                val newNome= binding.editName.text.toString()
                val newCognome= binding.editSurname.text.toString()
                val newUni=binding.editUniversity.text.toString()
                //Prendiamo il nome utente che ci serve per fare la query
                val nomeU= binding.editUsername.text.toString()
                //Facciamo l'update dei dati anche nel DB
                Log.d("UPDATESTUD", "Utente ricevuto: $newNome")
                Log.d("UPDATESTUD", "Id ricevuto: $idCorsoSelected")
                ApiClient.updateStudente(newNome,newCognome,newUni,idCorsoSelected,nomeU){boolean,error ->
                    if (error != null) {
                        // Gestisco l'errore
                        Log.e("UPDATESTUD", "Si è verificato un errore: $error")
                    }else if (boolean==true) {
                        // Utilizzo i dati restituiti e aggiorno il singleton
                        singleton.nome= newNome
                        singleton.cognome= newCognome
                        singleton.universita= newUni
                        singleton.idCorso=idCorsoSelected
                        singleton.corsoStudi=binding.editCorso.text.toString()
                        Log.d("UPDATESTUD", "Utente ricevuto: $newNome")
                        Log.d("UPDATESTUD", "Id ricevuto: $idCorsoSelected")
                    }else{
                        Log.d("UPDATESTUD", "Dati ricevuti: $boolean")
                    }
                }
                isEditMode=true
            }
        }

        val editAccountButton= binding.modCred
        var isEditAccount = true

        editAccountButton.setOnClickListener{
            if(isEditAccount){
                //  binding.editUsername.isEnabled=true
                binding.textPass.isEnabled=true

                editAccountButton.text= "Conferma modifiche"
                isEditAccount=false
            }else{
                // binding.editUsername.isEnabled=false
                binding.textPass.isEnabled=false
                editAccountButton.text= "Modifica credenziali"

                val newPassword= binding.textPass.text.toString()
                val nomeU= binding.editUsername.text.toString()
                //Facciamo l'update della pass anche nel DB
                ApiClient.updatePass(nomeU,newPassword){boolean,error ->
                    if (error != null) {
                        // Gestisco l'errore
                        Log.e("UPDATEPASS", "Si è verificato un errore: $error")
                    }else if (boolean==true) {
                        // Utilizzo i dati restituiti e aggiorno le sharedPreferences
                        val editor=sharedPreferences.edit()
                        editor.putString("password", newPassword)
                        editor.apply()
                        //aggiorno anche il singleton
                        singleton.password= newPassword
                        Log.d("UPDATEPASS", "Boolean ricevuti: $boolean")
                    }else{
                        Log.d("UPDATEPASS", "Dati ricevuti: $boolean")
                    }
                }
                isEditAccount=true
            }
        }

        var buttonLegend= binding.legendButton

        val legendTag = "LegendFragment"

        buttonLegend.setOnClickListener{
            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            if (!fragmentExist(legendTag)) {  //verifico se già il fragment è stato aperto tramite questa funzione definita sotto
                transaction.replace(R.id.fragmentContainerView, LegendFragment(), legendTag)
                transaction.commit()
            }
        }

        var monday= binding.iconMonday
        setOnClickImageChange(monday)

        var tuesday= binding.iconTuesday
        setOnClickImageChange(tuesday)

        var wednesday= binding.iconWednesday
        setOnClickImageChange(wednesday)

        var thursday= binding.iconThursday
        setOnClickImageChange(thursday)

        var friday= binding.iconFriday
        setOnClickImageChange(friday)

        var saturday= binding.iconSaturday
        setOnClickImageChange(saturday)

        var sunday= binding.iconSunday
        setOnClickImageChange(sunday)

    }

    fun convertToAsterisks(editText: EditText) {
        val text = editText.text.toString()
        val asterisks = StringBuilder()

        for (i in 0 until text.length) {
            asterisks.append("*")
        }

        editText.setText(asterisks)
        editText.setSelection(asterisks.length)
    }

    fun setOnClickImageChange(imageView: ImageView) {
        var isStudyMode = true

        imageView.setOnClickListener {
            if (isStudyMode) {
                imageView.setImageResource(R.drawable.baseline_battery_charging_full_24)
                isStudyMode= false
            } else {
                imageView.setImageResource(R.drawable.baseline_menu_book_24)
                isStudyMode=true
            }
        }
    }

    fun fragmentExist(tag: String): Boolean {       //funzione che mi permette di trovare se un fragment è presente tramite il uso tag
        val fragmentManager = parentFragmentManager
        return (fragmentManager.findFragmentByTag(tag) != null)         //scrittura compatta che restituisce true o false se quella condizione si verifica o meno
    }


}