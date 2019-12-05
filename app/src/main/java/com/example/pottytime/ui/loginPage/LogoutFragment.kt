package com.example.pottytime.ui.loginPage


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pottytime.MainActivity

import com.example.pottytime.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_logout.view.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.Auth


/**
 * A simple [Fragment] subclass.
 */
class LogoutFragment : Fragment() {
    var uid : String? = null
    var auth : FirebaseAuth? = null
    var currentUserUid : String? = null
    var fbAuth = FirebaseAuth.getInstance()

    var checkIfUserLogOut : Boolean = false

    //private var mGoogleSignInClient = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_logout, container, false)

        uid = arguments?.getString("destinationUid")
        auth = FirebaseAuth.getInstance()

        currentUserUid = auth?.currentUser?.uid


        root.btn_sign_out_temp.setOnClickListener {
            activity?.finish()
            fbAuth.signOut()
            auth?.signOut()
            FirebaseAuth.getInstance().signOut()
            checkIfUserLogOut = true;

            val intent = Intent (activity, LoginActivity::class.java)
            intent.putExtra("checkIfUserLogOut", checkIfUserLogOut)
            startActivity(intent)
        }

        return root
    }
}
