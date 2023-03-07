package com.ramaqq.storyapp_submission1.ui.stories

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ramaqq.storyapp_submission1.data.StoryRepository
import com.ramaqq.storyapp_submission1.data.response.ListStoryItem
import com.ramaqq.storyapp_submission1.pojo.*


class StoriesViewModel(private val repository: StoryRepository) : ViewModel() {
    private lateinit var pref: UserPreference

    fun init(pref: UserPreference){
        this.pref = pref
    }

//    val getCompleteStories: LiveData<PagingData<ListStoryItem>> =
//        pref.getUser().asLiveData().switchMap{
//            repository.getListStories("Bearer ${it.token}").cachedIn(viewModelScope)
//        }

    fun getData(token: String): LiveData<PagingData<ListStoryItem>>{
        return repository.getListStories("Bearer $token").cachedIn(viewModelScope)
        }



    fun getUser(): LiveData<UserModel>{
        return pref.getUser().asLiveData()
    }

//    val getStories: LiveData<List<ListStoryItem>> = data
//    val getLoading: LiveData<Boolean> = isLoading
//    val getError: LiveData<Boolean> = isError


/*    private fun stories() {
        isLoading.value = true
        var token: String? = null
        viewModelScope.launch {
            pref.getUser().collect {
                token = it.token

                val client = ApiClient.getApiService().getAllStories("Bearer $token", null)
                client.enqueue(object : Callback<StoriesResponse?> {
                    override fun onResponse(call: Call<StoriesResponse?>, response: Response<StoriesResponse?>) {
                        if (response.isSuccessful && response.body() != null) {
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
    val user = pref.getUser()
    private val userModel = user.asLiveData()

    fun init(pref: UserPreference) {
        this.pref = pref
//        stories()
    }
    val quote: LiveData<PagingData<QuoteResponseItem>> = quoteRepository.getQuote().cachedIn(viewModelScope)*/

}