package com.example.pottytime.ui.bathroom.reviews


import android.app.AlertDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pottytime.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dialog_addreview.view.*
import kotlinx.android.synthetic.main.fragment_bathrooms.*
import kotlinx.android.synthetic.main.fragment_review.*
import kotlinx.android.synthetic.main.fragment_review.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.databinding.DataBindingUtil
import com.example.pottytime.databinding.FragmentReviewBinding

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
        val binding = DataBindingUtil.inflate<FragmentReviewBinding>(
            inflater,
            R.layout.fragment_review,
            container,
            false
        )

        binding.addReview.setOnClickListener{
           //inflate
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_addreview, null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView).setTitle("Add a Review")
            //show
            val mAlertDialog = mBuilder.show()

            mDialogView.addReviewConfirm.setOnClickListener{
                val uid = FirebaseAuth.getInstance().uid ?: ""
                val user = db.collection("users").whereEqualTo("uid",uid)
                val ratingStar = mDialogView.ratingBar.rating.toString().toDouble()



                        user.get().addOnSuccessListener { document ->
                            if (document != null) {

                                var userID = -1;

                                for (field in document) {
                                    userID = field.get("userID").toString().toInt()
                                }

                                val current = LocalDateTime.now()
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                val formatted = current.format(formatter)

                                val review = hashMapOf(
                                    "bathroomID" to arguments!!.get("ID").toString().toInt(),
                                    "date" to formatted.toString(),
                                    "dislikes" to 0,
                                    "likes" to 0,
                                    "rating" to ratingStar,
                                    "review" to mDialogView.addReviewText.text.toString(),
                                    "userID" to userID
                                )

                                if (userID != -1) {
                                    db.collection("reviews").add(review)
                                        .addOnSuccessListener {
                                            Log.d(
                                                "reviewAdd",
                                                "DocumentSnapshot successfully written!"
                                            )
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(
                                                "reviewAdd",
                                                "Error writing document",
                                                e
                                            )
                                        }
                                }

                            }
                        }

                        mAlertDialog.dismiss()
                        showReviews()

            }

            mDialogView.addReviewCancel.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }

        binding.reviewLocation.text = arguments!!.getString("building")
        binding.reviewFloor.text = arguments!!.getString("floor")

        var j = arguments!!.getString("floor").toString().toInt() % 10
        var k = arguments!!.getString("floor").toString().toInt() % 100

        if (j == 1 && k != 11)
            binding.reviewFloorsub.text = "st"
        else if (j == 2 && k != 12)
            binding.reviewFloorsub.text = "nd"
        else if (j == 3 && k != 13)
            binding.reviewFloorsub.text = "rd"
        else
            binding.reviewFloorsub.text = "th"

        when(arguments!!.getString("gender").toString()){
            "Male" -> binding.bathroomGender.setImageResource(R.drawable.male_on)
            "Female" -> binding.bathroomGender.setImageResource(R.drawable.female_on)
            else -> binding.bathroomGender.setImageResource(R.drawable.neutral_on)
        }

        if(arguments!!.getBoolean("isHandicap")) {
            binding.bathroomHandicap.setImageResource(R.drawable.handicap_on)
        } else {
            binding.bathroomHandicap.setImageResource(R.drawable.handicap_off)
        }

        if(arguments!!.getBoolean("isFamily")) {
            binding.bathroomFamily.setImageResource(R.drawable.family_on)
        } else {
            binding.bathroomFamily.setImageResource(R.drawable.family_off)
        }

        showReviews()

        return binding.root
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
                    val resId = R.anim.layout_animation_fall_down
                    val animation = AnimationUtils.loadLayoutAnimation(activity, resId)
                    recyclerViewReview2.setLayoutAnimation(animation)
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

    fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.isEmpty())
            return false
        return true
    }

}
