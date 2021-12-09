package jp.hika019.kerkar_university.setup

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jp.hika019.kerkar_university.*


class LoadActivity: AppCompatActivity() {
    val TAG = "LoadActivity"+tagHoge
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "show LoadActivity")
//        setTheme(R.style.AppThem)
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