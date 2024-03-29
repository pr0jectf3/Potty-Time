package com.example.pottytime.ui.loginPage

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.pottytime.MainActivity
import com.example.pottytime.R
import com.example.pottytime.ui.models.User
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LoginActivity : AppCompatActivity() {
    lateinit var providers : List<AuthUI.IdpConfig>
    private val MY_REQUEST_CODE: Int = 6969 // Any code
    val db = FirebaseFirestore.getInstance()
    //var googleSignInClient : GoogleSignInClient? = null
    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var checkIfUserLogOut = intent.getBooleanExtra("checkIfUserLogOut", false)

        supportActionBar?.hide();

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

                saveUserToFirebaseDataBase()

                // This is to switch to main scence
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
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

    private fun saveUserToFirebaseDataBase(){
        val userEmail = FirebaseAuth.getInstance().currentUser!!.email ?: ""
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val refHome = FirebaseDatabase.getInstance().getReference()

        val user = User(uid, "", "", userEmail, "")

        val userID = getUserID(userEmail)

        val tempUser = hashMapOf(
            "uid" to uid,
            "userID" to userID,
            "firstName" to user.firstName,
            "lastName" to user.lastName,
            "gender" to user.gender,
            "displayName" to user.firstName,
            "email" to userEmail
        )

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(data in dataSnapshot.children){
                    if(data.child(uid).exists()){

                    } else {
                        ref.setValue(user)
                            .addOnSuccessListener {
                                Log.d("LoginActivity", "Saved user to firebase database")
                            }
                        db.collection("users").document(uid).set(tempUser)
                    }
                }

                // User first time signing in
                if(!dataSnapshot.exists()){
                    ref.setValue(user)
                        .addOnSuccessListener {
                            Log.d("LoginActivity", "Saved user to firebase database")
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        refHome.addListenerForSingleValueEvent(userListener)
    }

    // To get the userID, making our accounts from 1-5
    private fun getUserID(userEmail : String) : Int {
        if(userEmail == "michaelkourandom@gmail.com"){
            return 1;
        } else if (userEmail == "pasindu2siri@gmail.com"){
            return 2;
        } else if (userEmail == "vanitysubscribers@gmail.com"){
            return 3;
        } else if (userEmail == "ivanyu626@gmail.com" || userEmail == "ivanscreativity@gmail.com"){
            return 4;
        } else if (userEmail == "carloslariossolis@gmail.com"){
            return 5;
        } else {
            return 0;
        }

    }



}

