package jp.hika019.kerkar_university.Register_and_Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import jp.hika019.kerkar_university.R
import jp.hika019.kerkar_university.tagHoge
import jp.hika019.kerkar_university.uid
import kotlinx.android.synthetic.main.activity_register.*

class Register: AppCompatActivity() {

    private val auth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "Register" + tagHoge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setToolbar()

        googleSighin.setOnClickListener {
            google_sign()
        }


    }

    private fun setToolbar(){
        val toolbar: Toolbar = findViewById(R.id.toolbar7)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    fun google_sign(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

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
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    fun hogeee(){
        Log.d(TAG, "dasdasda")
        Firebase.auth.currentUser!!.unlink(GoogleAuthProvider.PROVIDER_ID)
            .addOnSuccessListener {
                Log.d(TAG, "success")
            }
            .addOnFailureListener {
                Log.w(TAG, "failure", it)
            }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.currentUser!!.linkWithCredential(credential)
            .addOnSuccessListener{
                Log.d(TAG, "引継ぎに成功しました")
                uid = it.user?.uid
            }
            .addOnFailureListener {
                Log.w(TAG, "linkWithCredential:failure", it)
                Toast.makeText(this, "引継ぎに失敗しました",
                    Toast.LENGTH_SHORT).show()
            }
    }



    companion object {
        private const val RC_SIGN_IN = 9001
    }
}