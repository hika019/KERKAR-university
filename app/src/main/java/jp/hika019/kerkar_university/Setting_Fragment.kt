package jp.hika019.kerkar_university

import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_setting.view.*

class Setting_Fragment: Fragment() {
    val TAG = "Setting_Fragment"

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_setting, container, false)

        view.setting_uid_textview.text = uid
        view.setting_uid_textview.setOnClickListener {
            uid_dialog()
        }

        get_university(view)


        return view
    }

    fun uid_dialog(){

        val uid_message = uid!!.substring(0, 6) + "-" +uid!!.substring(6, 12) + "-"+
                uid!!.substring(12, 18) + "-" +uid!!.substring(18, 24) + "-"+uid!!.substring(24, 30)

        val dialog = AlertDialog.Builder(context)
                .setTitle("ユーザID")
                .setMessage(uid_message)
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
}