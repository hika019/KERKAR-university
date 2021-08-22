package jp.hika019.kerkar_university.Setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import jp.hika019.kerkar_university.*
import kotlinx.coroutines.runBlocking
import java.util.*


class LoadActivity: AppCompatActivity() {
    val TAG = "StartActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Splash)
        Thread.sleep(200)
        setContentView(R.layout.activity_load)


        val setup_class = setup()
        setup_class.start(this)

    }
}