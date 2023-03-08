package com.ramaqq.storyapp_submission1.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ramaqq.storyapp_submission1.data.local.entity.UserModel
import com.ramaqq.storyapp_submission1.data.local.entity.UserPreference

class MainPageViewModel: ViewModel() {
    private lateinit var pref: UserPreference

    fun init(pref: UserPreference){
        this.pref = pref
    }

    fun getUser(): LiveData<UserModel>{
        return pref.getUser().asLiveData()
    }
}