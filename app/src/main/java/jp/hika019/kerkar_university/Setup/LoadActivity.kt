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

        if (Intent.ACTION_PACKAGE_FULLY_REMOVED.equals(intent.action)){
            val uid = Firebase.auth.uid

            if (uid != null) {
                firedb.collection("user")
                    .document(uid)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "user data delete -> success")
                    }
                    .addOnFailureListener {e ->
                        Log.w(TAG, "user data delete -> failure", e)
                    }
            }
        }


        val setup_class = setup()
        setup_class.start(this)

            }
}