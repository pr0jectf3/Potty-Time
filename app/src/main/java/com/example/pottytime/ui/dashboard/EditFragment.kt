package com.example.pottytime.ui.dashboard

import android.os.Bundle
import android.os.Message
import android.renderscript.Sampler
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.pottytime.R
import com.example.pottytime.ui.dashboard.DashboardFragment.Companion.user
import com.example.pottytime.ui.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_edit.view.*

class EditFragment : Fragment() {

    private var user: FirebaseUser? = null
    var auth : FirebaseAuth? = null
    var currentUserUid : String? = null
    var fbAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        currentUserUid = auth?.currentUser?.uid

        val root = inflater.inflate(R.layout.fragment_edit, container, false)

        setHasOptionsMenu(true)
        return root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save, menu)
        menu.findItem(R.id.save).setVisible(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.save -> {
            val userEmail = FirebaseAuth.getInstance().currentUser!!.email ?: ""
            val user = db.collection("users").document(uid)
            val tempUser = hashMapOf(
                "uid" to uid,
                "userID" to 0,
                "name" to firstName.text.toString(),
                "displayName" to firstName.text.toString(),
                "email" to userEmail
            )
            user.update(tempUser)

            replaceFragment(DashboardFragment())
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}