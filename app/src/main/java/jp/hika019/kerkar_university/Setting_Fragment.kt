package jp.hika019.kerkar_university

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import jp.hika019.kerkar_university.Register_and_Login.Register
import kotlinx.android.synthetic.main.fragment_setting.view.*

class Setting_Fragment: Fragment() {
    private val googleIdToken: String? = null
    private val TAG = "Setting_Fragment"
    private val auth = Firebase.auth

    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        view.setting_uid_textview.text = uid
        view.setting_uid_textview.setOnClickListener {
            uid_dialog()
        }

        get_university(view)

        view.setting_data_transfer_button.setOnClickListener {
            enter_uid()
//            val i = Intent(context, Register::class.java)
//            requireContext().startActivity(i)
        }

        view.button6.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context?.startActivity(intent)
        }

//        view.setting_destroy_user.setOnClickListener {
//            destroy_user()
//        }
//        view.googleSighin.setOnClickListener {
//            google_sign()
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


    fun enter_uid(){
        val dialog = AlertDialog.Builder(context)
            .setTitle("未実装機能")
            .setMessage("しばらくしたら実装されます")
        dialog.create().show()
    }

    fun google_sign(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)

        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)!!
            try {
                // Google Sign In was successful, authenticate with Firebase


                Log.d(TAG, "asdadasda")
//                auth.currentUser!!.linkWithCredential(credential)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            Log.d(TAG, "linkWithCredential:success")
//                            val user = task.result?.user
//                            updateUI(user)
//                        } else {
//                            Log.w(TAG, "linkWithCredential:failure", task.exception)
//                            Toast.makeText(context, "Authentication failed.",
//                                Toast.LENGTH_SHORT).show()
//                            updateUI(null)
//                        }
//                    }

                Log.d(TAG, "asdadasda")

                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                Log.d(TAG, "signInWithCredential:success")
                val user = auth.currentUser

                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.currentUser!!.linkWithCredential(credential)
                    .addOnSuccessListener{
                            Log.d(TAG, "linkWithCredential:success")
                            val user = it.user
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "linkWithCredential:failure", it)
                        Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }

            }
            .addOnFailureListener {
                Log.w(TAG, "signInWithCredential:failure", it)
            }
    }



    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

}