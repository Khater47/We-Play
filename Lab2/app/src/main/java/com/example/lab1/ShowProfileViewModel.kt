package com.example.lab1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowProfileViewModel: ViewModel() {

    private val _users = MutableLiveData<User>();

    val users:LiveData<User> = _users

    fun addUser(u:User){
        _users.value=u
    }

}