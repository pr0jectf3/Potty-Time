package com.example.pottytime.ui.bathroom.reviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pottytime.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.review_card.view.*

class ReviewsAdapter(val reviews: List<Review>): RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {
    val db = FirebaseFirestore.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.review_card, parent, false)
        )
    }

    override fun getItemCount()= reviews.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        val user = db.collection("users").whereEqualTo("userID",review.id)

        var userDisplayName : String = ""

        user.get().addOnSuccessListener { document ->
            if(document != null){
                for(field in document){
                    userDisplayName = field.get("displayName").toString()
                    holder.view.reviewCardUsername.text = userDisplayName
                }

            }else {
                println("No document")
            }
        }

        holder.view.reviewCardDate.text = review.date
        holder.view.reviewCardRating.text = review.rating
        holder.view.reviewCardReview.text = review.review
    }

    class ReviewViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}