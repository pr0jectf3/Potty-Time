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
        holder.view.reviewFloor.text = review.reviewFloor
        holder.view.reviewId.text = review.id.toString()

        var j = review.reviewFloor.toInt() % 10
        var k = review.reviewFloor.toInt() % 100

        if (j == 1 && k != 11)
            holder.view.reviewFloorTitle.text = "st"
        else if (j == 2 && k != 12)
            holder.view.reviewFloorTitle.text = "nd"
        else if (j == 3 && k != 13)
            holder.view.reviewFloorTitle.text = "rd"
        else
            holder.view.reviewFloorTitle.text = "th"


    }

    class ReviewViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}