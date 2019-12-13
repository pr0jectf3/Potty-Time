package com.example.pottytime.ui.bathroom


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pottytime.R
import com.example.pottytime.ui.bathroom.reviews.RecyclerViewClickListener
import com.example.pottytime.ui.bathroom.reviews.ReviewFragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_bathrooms.*
import androidx.databinding.DataBindingUtil
import com.example.pottytime.databinding.FragmentBathroomsBinding
import android.view.animation.AnimationUtils.loadLayoutAnimation
import android.view.animation.LayoutAnimationController
import android.view.animation.AnimationUtils


class BathroomsFragment : Fragment(), RecyclerViewClickListener {

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentBathroomsBinding>(
            inflater,
            R.layout.fragment_bathrooms,
            container,
            false
        )



        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        refreshReviewLayout.setOnRefreshListener {
            showReviews()
        }

        showReviews()

    }

    fun showReviews(){

        if(refreshReviewLayout != null) {
            refreshReviewLayout.isRefreshing = true
        }

        var bathrooms = listOf<Bathroom>()

        val docRef = db.collection("bathrooms").orderBy("building")
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
                        val id = k.data.get("bathroomID").toString()
                        Log.d(location, floor)

                        bathrooms += Bathroom(i, location, floor, gender, isFamily, isHandicap, nearby, id)
                        i += 1

                    }
                    if(refreshReviewLayout != null) {
                        refreshReviewLayout.isRefreshing = false

                        val resId = R.anim.layout_animation_fall_down
                        val animation = loadLayoutAnimation(activity, resId)
                        recyclerViewReview.setLayoutAnimation(animation)

                        recyclerViewReview.layoutManager = LinearLayoutManager(activity)
                        recyclerViewReview.adapter = BathroomsAdapter(bathrooms, this)
                    }


                } else {
                    Log.d("DOES NOT EXIST", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ERROR", "get failed with ", exception)
            }
    }

    override fun onRecyclerViewItemClick(
        id: String,
        building: String,
        floor: String,
        gender: String,
        isFamily: Boolean,
        isHandicap: Boolean,
        nearbyRoom: String
    ) {
        //Toast.makeText(activity, id, Toast.LENGTH_SHORT).show()

        val frag = ReviewFragment()

        val bundle = Bundle()
        bundle.putString("ID", id)
        bundle.putString("building", building)
        bundle.putString("floor", floor)
        bundle.putString("gender", gender)
        bundle.putBoolean("isFamily", isFamily)
        bundle.putBoolean("isHandicap", isHandicap)
        bundle.putString("nearbyRoom", nearbyRoom)

        frag.arguments = bundle

        val manager = fragmentManager

        val frag_trans = manager!!.beginTransaction()

        frag_trans!!.replace(R.id.nav_host_fragment, frag)
        frag_trans.addToBackStack(null);
        frag_trans!!.commit()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Review"
    }
}
