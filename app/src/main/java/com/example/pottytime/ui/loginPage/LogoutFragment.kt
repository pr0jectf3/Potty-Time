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

/**
 * A simple [Fragment] subclass.
 */
class LogoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_logout, container, false)

        root.btn_sign_out_temp.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent (getActivity(), MainActivity::class.java)
            startActivity(intent)

        }

        return root
    }


}
