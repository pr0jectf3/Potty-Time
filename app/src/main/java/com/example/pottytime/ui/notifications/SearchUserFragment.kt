package com.example.pottytime.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.pottytime.R
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_search_user.*
import kotlinx.android.synthetic.main.fragment_search_user.view.*
import kotlinx.android.synthetic.main.user_list_layout.view.*

class SearchUserFragment : Fragment() {

    private lateinit var searchUserViewModel: SearchUserViewModel

    //private lateinit var toiletListView: ListView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_search_user, container, false)

        root.search_userbutton.setOnClickListener{
            val whatUserTyped : String = root.search_userinfo.text.toString()
            fetchUsers(whatUserTyped)
        }

        return root
    }

    private fun fetchUsers(whatUserTyped : String){
        val user = db.collection("users").whereEqualTo("displayName",whatUserTyped)


        user.get().addOnSuccessListener { document ->
            val adapter = GroupAdapter<ViewHolder>()

            if(document.isEmpty){
                Toast.makeText(activity, "This user name does not exist!", Toast.LENGTH_SHORT).show()
            }
            if(document != null){
                for(field in document){
                    println("HERE")
                    val userInformation = UserInfo(field.get("displayName").toString(), field.get("firstName").toString(), field.get("lastName").toString(), field.get("gender").toString(), field.get("email").toString())

                    if(userInformation != null){
                        adapter.add(UserItem(userInformation))
                    } else {
                        println("Failed")
                    }
                }

                recyclerview_list_of_users.adapter = adapter
            }else {
                Toast.makeText(activity, "This user name does not exist!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class UserItem(val user: UserInfo) : Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.search_displayname.text = "Display Name: " + user.displayName
            viewHolder.itemView.search_email.text = "Email: " + user.email
            viewHolder.itemView.search_fullname.text = "Full Name: " +user.firstName + user.lastName
            viewHolder.itemView.search_gender.text = "Gender: " + user.gender
        }

        override fun getLayout(): Int {
            return R.layout.user_list_layout
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Search"
    }

    // This does not work somehow
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            searchUserViewModel = ViewModelProviders.of(this).get(SearchUserViewModel::class.java)
        } ?: throw Throwable("Can't not change title")
        searchUserViewModel.changeActionBarTitle("Search for a user");
    }
}