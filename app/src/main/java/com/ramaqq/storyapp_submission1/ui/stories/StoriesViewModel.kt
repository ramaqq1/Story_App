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

    val getCompleteStories: LiveData<PagingData<ListStoryItem>> =
        pref.getUser().asLiveData().switchMap{
            repository.getListStories("Bearer ${it.token}").cachedIn(viewModelScope)
        }

    fun getData(token: String): LiveData<PagingData<ListStoryItem>>{
        return repository.getListStories("Bearer $token").cachedIn(viewModelScope)
        }



    fun getUser(): LiveData<UserModel>{
        return pref.getUser().asLiveData()
    }


}