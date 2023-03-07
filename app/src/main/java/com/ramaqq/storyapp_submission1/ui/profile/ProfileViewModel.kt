package com.ramaqq.storyapp_submission1.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ramaqq.storyapp_submission1.pojo.UserModel
import com.ramaqq.storyapp_submission1.pojo.UserPreference
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private lateinit var pref: UserPreference

    fun init(pref: UserPreference){
        this.pref = pref
    }

    fun getUser(): LiveData<UserModel>{
        return pref.getUser().asLiveData()
    }

    fun getEmail(): LiveData<String>{
        return pref.getEmail().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }
}