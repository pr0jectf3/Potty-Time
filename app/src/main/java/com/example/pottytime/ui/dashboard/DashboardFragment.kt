package com.example.pottytime.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.pottytime.R
import com.example.pottytime.ui.loginPage.LoginActivity
import com.example.pottytime.ui.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.btn_sign_out_temp
import kotlinx.android.synthetic.main.fragment_logout.view.*

class DashboardFragment : Fragment() {
    //var uid : String? = null
    var auth : FirebaseAuth? = null
    var currentUserUid : String? = null
    var fbAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().uid ?: ""

    var checkIfUserLogOut : Boolean = false
    private lateinit var dashboardViewModel: DashboardViewModel

//    companion object {
//        var user: User? = null
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View?{
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        auth = FirebaseAuth.getInstance()

        currentUserUid = auth?.currentUser?.uid

        // Set the email for the user
//        val userEmail = FirebaseAuth.getInstance().currentUser!!.email
//        root.emailAddress.setText(userEmail);

//        val uid = FirebaseAuth.getInstance().uid
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//
//        ref.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                user = p0.getValue(User::class.java)
//                root.emailAddress.setText(user?.email)
//
//                root.gender.setText(user?.gender)
//
//                root.firstName.setText(user?.firstName)
//
//                root.lastName.setText(user?.lastName)
//            }
//        })

        val userEmail = FirebaseAuth.getInstance().currentUser!!.email ?: ""
        val user = db.collection("users").document(uid)

        user.get().addOnSuccessListener { document ->
            if(document != null){
                val firstName = document.get("firstName").toString()
                val lastName = document.get("lastName").toString()
                val gender = document.get("gender").toString()
                val displayName = document.get("displayName").toString()

                root.firstName.setText(firstName)
                root.lastName.setText(lastName)
                root.gender.setText(gender)
                root.displayName.setText(displayName)
            }else {
                println("No document")
            }
        }

        root.btn_sign_out_temp.setOnClickListener {
            activity?.finish()
            fbAuth.signOut()
            auth?.signOut()
            FirebaseAuth.getInstance().signOut()
            checkIfUserLogOut = true

            val intent = Intent (activity, LoginActivity::class.java)
            intent.putExtra("checkIfUserLogOut", checkIfUserLogOut)
            startActivity(intent)
        }
        setHasOptionsMenu(true)
        return root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit, menu)
        menu.findItem(R.id.edit).setVisible(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
       R.id.edit -> {
           replaceFragment(EditFragment())
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