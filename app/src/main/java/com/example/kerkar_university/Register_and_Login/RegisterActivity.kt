package com.example.kerkar_university.Register_and_Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kerkar_university.R
import com.example.kerkar_university.register_dialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity: AppCompatActivity() {
    private val TAG = "Register Activity"

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        already_hav_account_textview.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        create_account_button.setOnClickListener{
//            firedb_register_login(this).get_university_list()
            create_acount()
        }



    }

    private fun create_acount(){
        val mail = EmaileditTextText.text.toString()
        val password = PasswordeditTextText.text.toString()

        val auth = Firebase.auth

        if(mail.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "e-mai/password is empty", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "e-mai/password is empty")
        }else{
            auth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener {
                        if(!it.isSuccessful) return@addOnCompleteListener
                        //else if successful
                        Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")

                        //dialog 大学選択
                        val register_dialog_class = register_dialog(this, it.result?.user?.uid!!)
                        register_dialog_class.select_univarsity_rapper()

//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)

                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Failed to create user: ${it.message}")
                        Toast.makeText(this,"Failed to create user: ${it.message}", Toast.LENGTH_LONG).show()

                    }
        }

    }

}