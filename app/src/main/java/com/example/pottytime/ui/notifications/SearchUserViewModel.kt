package com.example.pottytime.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchUserViewModel : ViewModel() {

    private val search_title = MutableLiveData<String>()

    val text: LiveData<String>

    get() = search_title

    fun changeActionBarTitle(title : String) = search_title.postValue(title)

}