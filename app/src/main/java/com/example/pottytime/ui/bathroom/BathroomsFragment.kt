package com.example.pottytime.ui.bathroom


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pottytime.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_bathrooms.*

class BathroomsFragment : Fragment() {

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bathrooms, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        refreshReviewLayout.setOnRefreshListener {
            showReviews()
        }

        showReviews()

    }

    fun showReviews(){

        refreshReviewLayout.isRefreshing = true

        var bathrooms = listOf<Bathroom>()

        val docRef = db.collection("bathrooms")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var i = 1
                    for(k in document){

                        val location = k.data.get("building").toString()
                        val floor = k.data.get("floor").toString()
                        val gender = k.data.get("gender").toString()
                        val isFamily = k.data.get("isFamily").toString().toBoolean()
                        val isHandicap = k.data.get("isHandicap").toString().toBoolean()
                        val nearby = k.data.get("nearbyRoom").toString()
                        Log.d(location, floor)

                        bathrooms += Bathroom(i, location, floor, gender, isFamily, isHandicap, nearby)
                        i += 1

                    }

                    refreshReviewLayout.isRefreshing = false
                    recyclerViewReview.layoutManager = LinearLayoutManager(activity)
                    recyclerViewReview.adapter = BathroomsAdapter(bathrooms)


                } else {
                    Log.d("DOES NOT EXIST", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ERROR", "get failed with ", exception)
            }
    }


}
