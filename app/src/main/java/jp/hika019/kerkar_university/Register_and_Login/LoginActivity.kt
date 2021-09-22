package jp.hika019.kerkar_university.Register_and_Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import jp.hika019.kerkar_university.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import jp.hika019.kerkar_university.Setup.setup
import kotlinx.android.synthetic.main.activity_login.*
class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        create_account_textview.setOnClickListener{
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//            finish()
        }

        login_button.setOnClickListener{
            login()
        }

    }

    private fun login(){
        val mail = EmaileditTextText.text.toString()
        val password = PasswordeditTextText.text.toString()

        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        val uid = auth.currentUser!!.uid
                        val setup_class = setup()
                        setup_class.check_user_data(this)

                    }else{
                        Toast.makeText(this, "ログインに失敗しました", Toast.LENGTH_SHORT).show()
                    }
                }

    }
}