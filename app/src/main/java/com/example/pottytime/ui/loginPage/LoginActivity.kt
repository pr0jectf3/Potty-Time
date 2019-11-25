package com.example.pottytime.ui.loginPage

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.pottytime.MainActivity
import com.example.pottytime.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    lateinit var providers : List<AuthUI.IdpConfig>
    private val MY_REQUEST_CODE: Int = 6969 // Any code
    //var googleSignInClient : GoogleSignInClient? = null
    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var checkIfUserLogOut = intent.getBooleanExtra("checkIfUserLogOut", false)
        auth = FirebaseAuth.getInstance()

        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        if(checkIfUserLogOut){
            AuthUI.getInstance().signOut(this@LoginActivity)
                .addOnCompleteListener {
                    sign_in_button.visibility = View.VISIBLE
                    //btn_sign_out.visibility = View.GONE
                }
                .addOnFailureListener {
                        e -> Toast.makeText(this@LoginActivity,e.message, Toast.LENGTH_SHORT).show()
                }
        }

        sign_in_button.setSize(SignInButton.SIZE_STANDARD)

        sign_in_button.setOnClickListener{
            showSignInOptions()
            //var signInIntent = googleSignInClient?.signInIntent
            //startActivityForResult(signInIntent,MY_REQUEST_CODE)
        }

//        val account = GoogleSignIn.getLastSignedInAccount(this)
//
//        if (account != null) {
//            sign_in_button.visibility = View.GONE
//        }

//        btn_sign_out.setOnClickListener {
//            AuthUI.getInstance().signOut(this@LoginActivity)
//                .addOnCompleteListener {
//                    sign_in_button.visibility = View.VISIBLE
//                    btn_sign_out.visibility = View.GONE
//                }
//                .addOnFailureListener {
//                        e -> Toast.makeText(this@LoginActivity,e.message, Toast.LENGTH_SHORT).show()
//                }
//        }

        // Need this part
//        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this,gso)

    }

    override fun onStart() {
        super.onStart()
        if(auth?.currentUser != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MY_REQUEST_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this,""+user!!.email,Toast.LENGTH_SHORT).show()

                sign_in_button.visibility = View.GONE

                //btn_sign_out.isEnabled = true
                //btn_sign_out.visibility = View.VISIBLE

                // This is to switch to main scence
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this,""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
            }
        }

        // If the request is success, move on
//        if(requestCode == MY_REQUEST_CODE){
//            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//            if(result.isSuccess){
//                var account = result.signInAccount
//                sign_in_button.visibility = View.GONE
//                firebaseAuthWithGoogle(account)
//            }
//        }
    }

//    private fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
//        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
//        auth?.signInWithCredential(credential)
//            ?.addOnCompleteListener {
//                    task ->
//                if(task.isSuccessful){
//                    // Check if user logged in
//                    if(task.result?.user != null){
//                        sign_in_button.visibility = View.GONE
//                        val intent = Intent(this,MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                }else{
//                    // error message
//                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
//                }
//            }
//    }

    private fun showSignInOptions(){
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.LogInTheme)
            .build(),MY_REQUEST_CODE)
    }
}
