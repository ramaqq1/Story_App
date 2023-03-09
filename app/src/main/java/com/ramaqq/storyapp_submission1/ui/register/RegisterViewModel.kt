package com.ramaqq.storyapp_submission1.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ramaqq.storyapp_submission1.data.api.ApiClient
import com.ramaqq.storyapp_submission1.data.response.SignUpResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String
    private val isLoading = MutableLiveData<Boolean>()
    private val isError = MutableLiveData<Boolean>()
    private val mResult = MutableLiveData<SignUpResponse>()

    fun init(username: String, email: String, password: String){
        this.username = username
        this.email = email
        this.password = password
        register()
    }

    private fun register(){
        isLoading.value = true
        val client = ApiClient.getApiService().getRegister(username, email, password)
        client.enqueue(object : Callback<SignUpResponse?> {
            override fun onResponse(call: Call<SignUpResponse?>, response: Response<SignUpResponse?>) {
               if (response.isSuccessful && response.body() != null){
                   isLoading.value = false
                   mResult.postValue(response.body())
               }else{
                   isLoading.value = false
                   val data = response.errorBody()!!.string()

                   try {
                       val jObjError = JSONObject(data)
                       mResult.postValue(SignUpResponse(jObjError.getBoolean("error"), jObjError.getString("message")))
                   } catch (e: Exception) {
                       // do nothing
                   }
               }
            }

            override fun onFailure(call: Call<SignUpResponse?>, t: Throwable) {
//                Log.e(LoginViewModel.TAG, "onResponse: ${response.message()}")
                isLoading.value = false
                isError.value = true
            }
        })
    }

    val getRegisterResult: LiveData<SignUpResponse> = mResult
    val getLoading: LiveData<Boolean> = isLoading
    val getError: LiveData<Boolean> = isError

}