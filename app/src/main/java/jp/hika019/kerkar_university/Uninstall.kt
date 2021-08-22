package jp.hika019.kerkar_university

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Uninstall : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val TAG = "uninstall"

        Log.d(TAG, "Uninstall -> call")

        val uid = Firebase.auth.uid

        if (Intent.ACTION_PACKAGE_FULLY_REMOVED == intent.action) {

            if (uid != null) {
                firedb.collection("user")
                    .document(uid)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "delete user_data -> success")
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "delete user_data -> failure", it)
                    }
            }


        }
    }


}