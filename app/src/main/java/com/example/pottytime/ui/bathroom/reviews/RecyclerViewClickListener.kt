package com.example.pottytime.ui.bathroom.reviews

import android.view.View

interface RecyclerViewClickListener {

    fun onRecyclerViewItemClick(id: String, building: String, floor: String, gender: String, isFamily: Boolean, isHandicap: Boolean, nearbyRoom: String){

    }

}