package com.example.pottytime.ui.notifications

import android.os.Bundle
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
import com.example.pottytime.R

class SearchUserFragment : Fragment() {

    private lateinit var searchUserViewModel: SearchUserViewModel

    private lateinit var toiletListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_search_user, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)

        toiletListView = root.findViewById(R.id.toilet_list)
        val toiletArray = arrayOf("Salazar Hall", "King Hall", "ECST")
        toiletListView.adapter = ArrayAdapter<String>(root.context, android.R.layout.simple_expandable_list_item_1, toiletArray)

        toiletListView.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(root.context, "tapped", Toast.LENGTH_SHORT)
        }

        return root
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
        searchUserViewModel.changeActionBarTitle("Search for user");
    }
}