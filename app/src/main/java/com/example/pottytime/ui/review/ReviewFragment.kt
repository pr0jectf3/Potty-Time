package com.example.pottytime.ui.review


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pottytime.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_reviews.*

class ReviewFragment : Fragment() {

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews, container, false)
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

        var reviews = listOf<Reviews>()

        val docRef = db.collection("bathrooms")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    for(k in document){
                        var i = 0
                        val location = k.data.get("building").toString()
                        val review = "review"
                        Log.d(location, review)

                        reviews += Reviews(i, location, review)

                    }

                    refreshReviewLayout.isRefreshing = false
                    recyclerViewReview.layoutManager = LinearLayoutManager(activity)
                    recyclerViewReview.adapter = ReviewsAdapter(reviews)


                } else {
                    Log.d("DOES NOT EXIST", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ERROR", "get failed with ", exception)
            }
    }


}
