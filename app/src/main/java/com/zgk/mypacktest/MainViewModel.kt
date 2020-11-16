package com.zgk.mypacktest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class MainViewModel(countReserved: Int) : ViewModel(){
    var counter = MutableLiveData<Int>()
    init {
        counter.value=countReserved
    }
    fun plusOne(){
        val count=counter.value ?: 0
        counter.value=count+1
    }
    fun clear(){
        counter.value=0
    }
    private val userIdLiveData = MutableLiveData<String>()
    val user: LiveData<User> = Transformations.switchMap(userIdLiveData){
        userId->Repository.getUser(userId)
    }
    fun getUser(userId:String){
        userIdLiveData.value=userId
    }
}