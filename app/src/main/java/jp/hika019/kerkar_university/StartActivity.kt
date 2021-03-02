package jp.hika019.kerkar_university

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class StartActivity: AppCompatActivity() {
    val TAG = "StartActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val dataStore = getSharedPreferences(UserData_SharedPreferences_name, Context.MODE_PRIVATE)

        val local_uid = dataStore.getString("uid", null)

        if (local_uid != null){
            uid = local_uid
            Log.d("hoge", "uid: $uid")
            firedb_register_login(this).cheak_user_data()
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }else{
            val uuid = UUID.randomUUID().toString()
            uid = sha256(uuid).substring(0, 32)
            Log.d("hoge", uid!!)

            val editor: SharedPreferences.Editor = dataStore.edit()
            editor.putString("uid", uid)
            editor.commit()

            val register_dialog_class = register_dialog(this)
            register_dialog_class.select_univarsity_rapper()
        }





    }
}