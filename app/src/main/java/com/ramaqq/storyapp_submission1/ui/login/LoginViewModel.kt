package com.ramaqq.storyapp_submission1.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramaqq.storyapp_submission1.data.api.ApiClient
import com.ramaqq.storyapp_submission1.data.response.LoginResponse
import com.ramaqq.storyapp_submission1.data.response.LoginResult
import com.ramaqq.storyapp_submission1.pojo.UserPreference
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel: ViewModel() {
    private val mResult = MutableLiveData<LoginResponse>()
    private val isLoading = MutableLiveData<Boolean>()
    private val isError = MutableLiveData<Boolean>()
    private lateinit var pref: UserPreference
    private lateinit var username: String
    private lateinit var password: String

    fun init(username: String, password: String, pref: UserPreference){
        this.username = username
        this.password = password
        this.pref = pref
        login()
    }

    fun saveDataLogin(user: LoginResult, email: String){
        viewModelScope.launch {
            pref.saveUser(user)
            pref.saveEmail(email)
        }
    }

    private fun login(){
        isLoading.value = true
        val client = ApiClient.getApiService().getLogin(username, password)
        client.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
                isError.value = false
                if (response.isSuccessful){
                   isLoading.value = false
                   mResult.postValue(response.body())
               }else{
                   isLoading.value = false
                   val data = response.errorBody()!!.string()

                   try {
                       val jObjError = JSONObject(data)
                       mResult.postValue(LoginResponse(null, response.raw().isSuccessful, jObjError.getString("message")))
                   } catch (e: Exception) {
                       // do nothing
                   }
               }
            }
            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                isLoading.value = false
                isError.value = true
            }
        })

    }

    val getLoginResult: LiveData<LoginResponse> = mResult

    val getLoading: LiveData<Boolean> = isLoading
    val getError: LiveData<Boolean> = isError

}