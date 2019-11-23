package com.example.pottytime.ui.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pottytime.R
import kotlinx.android.synthetic.main.review_card.view.*

class ReviewsAdapter(val reviews: List<Reviews>): RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.review_card, parent, false)
        )
    }

    override fun getItemCount() = reviews.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        holder.view.reviewLocationName.text = review.reviewLocation
        holder.view.reviewReview.text = review.reviewReview

    }

    class ReviewViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}