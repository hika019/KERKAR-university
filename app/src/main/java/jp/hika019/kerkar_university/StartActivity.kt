package jp.hika019.kerkar_university

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import java.sql.Time
import java.util.*


class StartActivity: AppCompatActivity() {
    val TAG = "StartActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Splash)

        setContentView(R.layout.activity_start)

        start()
        

    }



    private fun start() {
        Log.d(TAG, "start() -> call")
        val dataStore = getSharedPreferences(UserData_SharedPreferences_name, Context.MODE_PRIVATE)

        //val local_uid = dataStore.getString("uid", null)
        val context = this
        //Log.d(TAG, "local_uid: "+ local_uid)


        val auth = Firebase.auth

        if (auth.uid != null){
            Log.d(TAG, "uid: "+auth.uid)
            uid = auth.uid

            firedb_register_login(context).cheak_user_data()
        }else{
            val uuid = UUID.randomUUID().toString()
            //create_uid(uuid)
            Log.d(TAG, "create_user() -> call")
            runBlocking {
                create_user()
            }
            Log.d("Main", "uid: "+ uid)
            Log.d(TAG, uid.toString())
            /*
            val editor: SharedPreferences.Editor = dataStore.edit()
            editor.putString("uid", uid)
            editor.commit()
            */
            firedb_register_login(this).get_university_list()
        }
    }
}