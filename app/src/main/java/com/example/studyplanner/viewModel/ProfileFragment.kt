package com.example.studyplanner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ReplacementTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studyplanner.databinding.FragmentProfileBinding
import com.example.studyplanner.model.DataSingleton
import com.example.studyplanner.viewModel.LegendFragment
import com.example.studyplanner.viewModel.MainActivity


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences //ci serve per effettuare il logout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(inflater)

        val bottoneLogout= binding.logoutButton

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        //Implemento il Logout
        bottoneLogout.setOnClickListener{
            val editor = sharedPreferences.edit()
            editor.remove("Nome Utente")
            editor.remove("password")
            editor.remove("isLoggedIn")
            editor.apply()

            // Reindirizziamo l'utente alla schermata di accesso
            val i = Intent(requireContext(), MainActivity::class.java)
            startActivity(i)
            requireActivity().finish() //Per terminare l'attività ospitante e tornare alla schermata di accesso.
        }

        //Se l'utente ha checkato la checkbox allora sfrutto i dati salvati nelle sharedPreferences
        var loggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

        if(!loggedIn){
            //Mostro nel riquadro nome utente e password dell'utente che ha fatto login. Uso il singleton "DataSingleton". Questo se l'utente non ha checkato la checkboc
            val singleton= DataSingleton.ottieniIstanza()
            binding.editUsername.setText(singleton.nomeUtente)
            binding.textPass.setText(singleton.password)
        }else{
            val savedUsername = sharedPreferences.getString("Nome Utente", "")
            val savedPassword = sharedPreferences.getString("password", "")
            binding.editUsername.setText(savedUsername)
            binding.textPass.setText(savedPassword)
        }

      /*  val savedUsername = sharedPreferences.getString("Nome Utente", "")
        val savedPassword = sharedPreferences.getString("password", "")

        binding.editUsername.setText(savedUsername)
        binding.textPass.setText(savedPassword) */

        val editProfile= binding.editProfileIcon
        var isEditMode = true

        editProfile.setOnClickListener{
            if(isEditMode){ //Abilitiamo le modifiche
                binding.editName.isEnabled=true
                binding.editSurname.isEnabled=true
                binding.editCorso.isEnabled=true
                binding.editUniversity.isEnabled=true

                editProfile.setImageResource(R.drawable.baseline_check_24)
                isEditMode=false
            } else{
                binding.editName.isEnabled=false
                binding.editSurname.isEnabled=false
                binding.editCorso.isEnabled=false
                binding.editUniversity.isEnabled=false

                editProfile.setImageResource(R.drawable.baseline_edit_24)
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
                isEditAccount=true
            }
        }

        //val editTextPass= binding.textPass
        //convertToAsterisks(editTextPass)

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

        return binding.root
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