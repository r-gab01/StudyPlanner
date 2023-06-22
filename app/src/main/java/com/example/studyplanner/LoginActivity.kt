package com.example.studyplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import androidx.core.content.ContextCompat
import com.example.studyplanner.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(R.id.containerView_login, LoginFragment())
        transaction.commit()




   /*     val eyeVisibleDrawable = ContextCompat.getDrawable(this, R.drawable.baseline_remove_red_eye_24)
        val eyeHiddenDrawable = ContextCompat.getDrawable(this, R.drawable.icons8_eye_24)


       passwordEditText.setOnLongClickListener{
           val isPasswordVisible = passwordEditText.inputType != InputType.TYPE_TEXT_VARIATION_PASSWORD
           //verifichiamo se il tipo di input della password è TYPE_TEXT_VARIATION_PASSWORD. Se lo è, significa che la password è nascosta. In tal caso, impostiamo il tipo di input sulla combinazione di TYPE_CLASS_TEXT e TYPE_TEXT_VARIATION_PASSWORD per nascondere la password.
           // Inoltre, impostiamo l'icona dell'occhio nascosto come drawable destro dell'EditText.
           if (isPasswordVisible) {
               // Nascondi il testo della password
               passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
               passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, eyeHiddenDrawable, null)
           } else {
               //Se il tipo di input della password non è TYPE_TEXT_VARIATION_PASSWORD, significa che la password è già visibile.
               // In tal caso, impostiamo il tipo di input su TYPE_TEXT_VARIATION_VISIBLE_PASSWORD per mostrare la password. Inoltre, impostiamo l'icona dell'occhio visibile come drawable destro dell'EditText
               // Mostra il testo della password
               passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
               passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, eyeVisibleDrawable, null)
           }
//restituiamo true per indicare che l'evento è stato gestito correttamente.
           true
       } */
    }

}