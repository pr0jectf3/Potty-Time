package com.example.pottytime.ui.bathroom.reviews


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pottytime.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_bathrooms.*
import kotlinx.android.synthetic.main.fragment_review.*

/**
 * A simple [Fragment] subclass.
 */
class ReviewFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        Log.d("DEBUG", arguments!!.getString("ID"))
        val view = inflater.inflate(R.layout.fragment_review, container, false)

        view.findViewById<TextView>(R.id.reviewLocation).text = arguments!!.getString("building")
        view.findViewById<TextView>(R.id.reviewFloor).text = arguments!!.getString("floor")

        var j = arguments!!.getString("floor").toString().toInt() % 10
        var k = arguments!!.getString("floor").toString().toInt() % 100

        if (j == 1 && k != 11)
            view.findViewById<TextView>(R.id.reviewFloorsub).text = "st"
        else if (j == 2 && k != 12)
            view.findViewById<TextView>(R.id.reviewFloorsub).text = "nd"
        else if (j == 3 && k != 13)
            view.findViewById<TextView>(R.id.reviewFloorsub).text = "rd"
        else
            view.findViewById<TextView>(R.id.reviewFloorsub).text = "th"

        when(arguments!!.getString("gender").toString()){
            "Male" -> view.findViewById<ImageView>(R.id.bathroomGender).setImageResource(R.drawable.male_on)
            "Female" -> view.findViewById<ImageView>(R.id.bathroomGender).setImageResource(R.drawable.female_on)
            else -> view.findViewById<ImageView>(R.id.bathroomGender).setImageResource(R.drawable.neutral_on)
        }

        if(arguments!!.getBoolean("isHandicap")) {
            view.findViewById<ImageView>(R.id.bathroomHandicap).setImageResource(R.drawable.handicap_on)
        } else {
            view.findViewById<ImageView>(R.id.bathroomHandicap).setImageResource(R.drawable.handicap_off)
        }

        if(arguments!!.getBoolean("isFamily")) {
            view.findViewById<ImageView>(R.id.bathroomFamily).setImageResource(R.drawable.family_on)
        } else {
            view.findViewById<ImageView>(R.id.bathroomFamily).setImageResource(R.drawable.family_off)
        }

        showReviews()

        return view
    }

    private fun showReviews(){

        //reviewRefresher.isRefreshing = true

        var reviews = listOf<Review>()

        val docRef = db.collection("reviews")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    //var i = 1 ??????????????????????????????????????????????????????
                    for(k in document){

                        if(k.data.get("bathroomID").toString() == arguments!!.getString("ID")){
                            val date = k.data.get("date").toString()
                            val rating = k.data.get("rating").toString()
                            val reviewText = k.data.get("review").toString()
                            val id = k.data.get("userID").toString().toInt()
                            reviews += Review(id, date, rating, reviewText)
                            //i += 1 ???????????????????????????
                        }


                    }

                    //reviewRefresher.isRefreshing = false
                    recyclerViewReview2.layoutManager = LinearLayoutManager(activity)
                    recyclerViewReview2.adapter = ReviewsAdapter(reviews)


                } else {
                    Log.d("DOES NOT EXIST", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ERROR", "get failed with ", exception)
            }
    }


}
