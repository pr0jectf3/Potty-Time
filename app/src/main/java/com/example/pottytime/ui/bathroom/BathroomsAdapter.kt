package com.example.pottytime.ui.bathroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pottytime.R
import kotlinx.android.synthetic.main.bathroom_card.view.*

class BathroomsAdapter(val bathrooms: List<Bathroom>): RecyclerView.Adapter<BathroomsAdapter.BathroomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BathroomViewHolder {
        return BathroomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.bathroom_card, parent, false)
        )
    }

    override fun getItemCount() = bathrooms.size

    override fun onBindViewHolder(holder: BathroomViewHolder, position: Int) {
        val bathroom = bathrooms[position]

        holder.view.bathroomLocationName.text = bathroom.bathroomLocation
        holder.view.bathroomFloor.text = bathroom.bathroomFloor
        holder.view.bathroomId.text = bathroom.id.toString()

        var j = bathroom.bathroomFloor.toInt() % 10
        var k = bathroom.bathroomFloor.toInt() % 100

        if (j == 1 && k != 11)
            holder.view.bathroomFloorTitle.text = "st"
        else if (j == 2 && k != 12)
            holder.view.bathroomFloorTitle.text = "nd"
        else if (j == 3 && k != 13)
            holder.view.bathroomFloorTitle.text = "rd"
        else
            holder.view.bathroomFloorTitle.text = "th"

        when(bathroom.bathroomGender){
            "Male" -> holder.view.bathroomGender.setImageResource(R.drawable.male_on)
            "Female" -> holder.view.bathroomGender.setImageResource(R.drawable.female_on)
            else -> holder.view.bathroomGender.setImageResource(R.drawable.neutral_on)
        }

        if(bathroom.bathroomIsHandicap) {
            holder.view.bathroomHandicap.setImageResource(R.drawable.handicap_on)
        } else {
            holder.view.bathroomHandicap.setImageResource(R.drawable.handicap_off)
        }

        if(bathroom.bathroomIsFamily) {
            holder.view.bathroomFamily.setImageResource(R.drawable.family_on)
        } else {
            holder.view.bathroomFamily.setImageResource(R.drawable.family_off)
        }

        holder.view.setOnClickListener{
            Toast.makeText(holder.view.context, bathroom.bathroomLocation, Toast.LENGTH_SHORT).show()
        }


    }

    class BathroomViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}