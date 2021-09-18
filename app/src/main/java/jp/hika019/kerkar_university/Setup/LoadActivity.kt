package jp.hika019.kerkar_university.Setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import jp.hika019.kerkar_university.*
import kotlinx.coroutines.runBlocking
import java.util.*


class LoadActivity: AppCompatActivity() {
    val TAG = "LoadActivity"+TAG_hoge
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "show LoadActivity")
        setTheme(R.style.AppTheme_Splash)
        Thread.sleep(200)
        setContentView(R.layout.activity_load)

        /*
        val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))
        */

        val setup_class = setup()
        setup_class.start(this)

    }
}