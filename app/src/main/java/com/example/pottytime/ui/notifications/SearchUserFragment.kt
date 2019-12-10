package com.example.pottytime.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pottytime.R

class SearchUserFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var toiletListView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)

        toiletListView = root.findViewById(R.id.toilet_list)
        val toiletArray = arrayOf("Salazar Hall", "King Hall", "ECST")
        toiletListView.adapter = ArrayAdapter<String>(root.context, android.R.layout.simple_expandable_list_item_1, toiletArray)

        toiletListView.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(root.context, "tapped", Toast.LENGTH_SHORT)
        }

        return root
    }
}