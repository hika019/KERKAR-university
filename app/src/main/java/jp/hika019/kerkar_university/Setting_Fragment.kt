package jp.hika019.kerkar_university

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_setting.view.*

class Setting_Fragment: Fragment() {
    private val TAG = "Setting_Fragment"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_setting, container, false)

        view.setting_uid_textview.text = uid
        view.setting_uid_textview.setOnClickListener {
            uid_dialog()
        }

        get_university(view)

        view.setting_data_transfer_button.setOnClickListener {
            enter_uid(view)
        }

//        view.setting_destroy_user.setOnClickListener {
//            destroy_user()
//        }

        return view
    }

    fun uid_dialog(){
        val dialog = AlertDialog.Builder(context)
                .setTitle("ユーザID")
                .setMessage(uid)
                .setPositiveButton("ok"){ dialog, which ->

                }
        dialog.create().show()

    }

    fun get_university(view: View){
        firedb.collection("user")
                .document(uid!!)
                .get()
                .addOnSuccessListener {
                    val university = it.getString("university")
                    view.setting_university_textview.text = university
                }
                .addOnFailureListener{
                    Toast.makeText(context, "大学情報が取得できませんでした", Toast.LENGTH_SHORT).show()
                }
    }

    private fun destroy_user(){
        Log.d(TAG, "destroy_user -> call")
        /*
        firedb.collection("user")
            .document(uid!!)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "delete data -> success")

            }
            .addOnFailureListener {
                Log.w(TAG, "delete data -> failure", it)
                Toast.makeText(context, "データ消去に失敗しました", Toast.LENGTH_SHORT).show()
            }
         */

        Firebase.auth.signOut()
        activity?.finish()

    }

    fun enter_uid(view: View){
        val layout = LayoutInflater.from(context).inflate(R.layout.dialog_enter_uid, null)

        val dialog = AlertDialog.Builder(context)
                .setTitle("未実装機能")
                .setMessage("しばらくしたら実装されます")

        /*
        val dialog = AlertDialog.Builder(context)
                .setTitle("データを引き継ぐ")
                .setView(layout)
                .setPositiveButton("引き継ぐ"){dialog, which ->
                    val enter_uid0 = layout.dialog_enter_uid_edittxt0.text.toString()
                    val enter_uid1 = layout.dialog_enter_uid_edittxt1.text.toString()
                    val enter_uid2 = layout.dialog_enter_uid_edittxt2.text.toString()
                    val enter_uid3 = layout.dialog_enter_uid_edittxt3.text.toString()
                    val enter_uid4 = layout.dialog_enter_uid_edittxt4.text.toString()

                    if(!(enter_uid0.isEmpty() || enter_uid1.isEmpty() || enter_uid2.isEmpty() ||
                            enter_uid3.isEmpty() || enter_uid4.isEmpty())){
                        if(cheack_uid(enter_uid0, enter_uid1, enter_uid2, enter_uid3, enter_uid4)){
                            Log.d(TAG, "enter_uid -> success")

//                            val dataStore = getSharedPreferences(UserData_SharedPreferences_name, Context.MODE_PRIVATE)
//                            val editor: SharedPreferences.Editor = dataStore.edit()
//                            editor.putString("uid", uid)
//                            editor.commit()
                        }
                    }


                }*/

        dialog.create().show()
    }
}