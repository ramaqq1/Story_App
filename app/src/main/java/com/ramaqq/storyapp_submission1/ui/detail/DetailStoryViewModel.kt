package com.ramaqq.storyapp_submission1.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramaqq.storyapp_submission1.data.api.ApiClient
import com.ramaqq.storyapp_submission1.pojo.DetailResponse
import com.ramaqq.storyapp_submission1.pojo.Story
import com.ramaqq.storyapp_submission1.pojo.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel: ViewModel() {
    private lateinit var id: String
    private lateinit var pref: UserPreference
    private val isLoading = MutableLiveData<Boolean>()
    private val isError = MutableLiveData<Boolean>()
    private val data = MutableLiveData<Story>()

    fun init(pref: UserPreference, id: String){
        this.pref = pref
        this.id = id
        detailStory()
    }

    private fun detailStory() {
        isLoading.value = true
        var token: String?
        viewModelScope.launch {
            pref.getUser().collect{
                token = it.token

                val client = ApiClient.getApiService().getDetailStory("Bearer $token", id)
                client.enqueue(object : Callback<DetailResponse?> {
                    override fun onResponse(call: Call<DetailResponse?>, response: Response<DetailResponse?>) {
                        isError.value = false
                        if (response.isSuccessful && response.body() != null){
                            isLoading.value = false
                            data.postValue(response.body()?.story)
                        }
                    }

                    override fun onFailure(call: Call<DetailResponse?>, t: Throwable) {
                        isError.value = true
                        isLoading.value = false
                    }
                })
            }
        }
    }

    val getDetailStory: LiveData<Story> = data
    val getLoading: LiveData<Boolean> = isLoading
    val getError: LiveData<Boolean> = isError
}