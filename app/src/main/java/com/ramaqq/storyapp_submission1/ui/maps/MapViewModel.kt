package com.ramaqq.storyapp_submission1.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramaqq.storyapp_submission1.data.api.ApiClient
import com.ramaqq.storyapp_submission1.data.response.ListStoryItem
import com.ramaqq.storyapp_submission1.data.response.StoriesResponse
import com.ramaqq.storyapp_submission1.data.local.entity.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel: ViewModel() {
    private lateinit var pref: UserPreference

    private val isLoading = MutableLiveData<Boolean>()
    val getLoading: LiveData<Boolean> = isLoading

    private val isError = MutableLiveData<Boolean>()
    val getError: LiveData<Boolean> = isError

    private val data = MutableLiveData<List<ListStoryItem>>()
    val getLocation: LiveData<List<ListStoryItem>> = data

    fun init(pref: UserPreference){
        this.pref = pref
        location()
    }

    private fun location() {
        isLoading.value = true
        var token: String? = null
        viewModelScope.launch {
            pref.getUser().collect{
                token = it.token

                val client = ApiClient.getApiService().getAllStories("Bearer $token", "1", 20, 1 )
                client.enqueue(object : Callback<StoriesResponse?> {
                    override fun onResponse(call: Call<StoriesResponse?>, response: Response<StoriesResponse?>) {
                        if (response.isSuccessful && response.body() != null){
                            isLoading.value = false
                            isError.value = false
                            data.postValue(response.body()?.listStory)
                        }
                    }
                    override fun onFailure(call: Call<StoriesResponse?>, t: Throwable) {
                        isLoading.value = false
                        isError.value = true
                    }
                })
            }
        }
    }
}