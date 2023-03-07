package com.ramaqq.storyapp_submission1.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramaqq.storyapp_submission1.data.StoryRepository
import com.ramaqq.storyapp_submission1.data.response.LoginResult
import com.ramaqq.storyapp_submission1.pojo.UserPreference
import kotlinx.coroutines.launch

class LoginViewModelN(private val storyRepository: StoryRepository): ViewModel() {
    private lateinit var pref: UserPreference
    private lateinit var username: String
    private lateinit var password: String

    fun init(username: String, password: String, pref: UserPreference){
        this.username = username
        this.password = password
        this.pref = pref

    }

    fun saveDataLogin(user: LoginResult){
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

//    fun getLoginData() = storyRepository.getLoginResult(username, password)
}