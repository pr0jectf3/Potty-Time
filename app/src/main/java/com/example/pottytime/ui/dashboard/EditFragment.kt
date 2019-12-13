package com.example.pottytime.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import com.example.pottytime.MainActivity
import com.example.pottytime.R
import com.example.pottytime.databinding.FragmentEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_edit.view.*

class EditFragment : Fragment() {

    //private var user: FirebaseUser? = null
    var auth : FirebaseAuth? = null
    var currentUserUid : String? = null
    //var fbAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        currentUserUid = auth?.currentUser?.uid

        val binding: FragmentEditBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit, container, false)

//        val root = inflater.inflate(R.layout.fragment_edit, container, false)
        //val userEmail = FirebaseAuth.getInstance().currentUser!!.email ?: ""
        val user = db.collection("users").document(uid)

        user.get().addOnSuccessListener { document ->
            if(document != null){
                val firstName = document.get("firstName").toString()
                val lastName = document.get("lastName").toString()
                val gender = document.get("gender").toString()
                val displayName = document.get("displayName").toString()

                binding.editFirstName.setText(firstName);
                binding.editLastName.setText(lastName);
                binding.editGender.setText(gender);
                binding.editDisplayName.setText(displayName);
            }else {
                println("No document")
            }
        }

        setHasOptionsMenu(true)
        return binding.root
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
            val firstName = editFirstName.text.toString()
            val lastName = editLastName.text.toString()
            val gender = editGender.text.toString()
            val displayName = editDisplayName.text.toString()

//            val tempUser = hashMapOf(
//                "uid" to uid,
//                "userID" to 0,
//                "name" to firstName.text.toString(),
//                "displayName" to firstName.text.toString(),
//                "email" to userEmail
//            )

            user.get().addOnSuccessListener { document ->
                if(document != null){
                    val userID = document.get("userID").toString().toInt()
                    val tempUser = hashMapOf(
                        "uid" to uid,
                        "userID" to userID,
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "gender" to gender,
                        "displayName" to displayName,
                        "email" to userEmail
                    )

                    user.update(tempUser)
                    replaceFragment(DashboardFragment())
                    //Navigation.createNavigateOnClickListener(R.id.action_editFragment_to_navigation_dashboard2)
                }else {
                    println("No document")
                }
            }



            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun replaceFragment(fragment: Fragment) {
//        val fragmentTransaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
          //Navigation.createNavigateOnClickListener(R.id.action_editFragment2_to_navigation_dashboard)
//        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
//        //fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit()
        val manager = fragmentManager

        val frag_trans = manager!!.beginTransaction()

        frag_trans!!.replace(R.id.nav_host_fragment, fragment)
        frag_trans.addToBackStack(null);
        frag_trans!!.commit()
    }
}