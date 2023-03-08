package com.ramaqq.storyapp_submission1.ui.stories

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ramaqq.storyapp_submission1.data.StoryRepository
import com.ramaqq.storyapp_submission1.data.local.entity.UserModel
import com.ramaqq.storyapp_submission1.data.local.entity.UserPreference
import com.ramaqq.storyapp_submission1.data.response.ListStoryItem
import kotlinx.coroutines.launch


class StoriesViewModel(private val repository: StoryRepository) : ViewModel() {
    private lateinit var pref: UserPreference

    fun init(pref: UserPreference){
        this.pref = pref
    }

    fun getData(token: String): LiveData<PagingData<ListStoryItem>>{
        return repository.getListStories("Bearer $token").cachedIn(viewModelScope)
        }

    fun getUser(): LiveData<UserModel>{
        return pref.getUser().asLiveData()
    }


/*    // test code
    private val dataN = MutableLiveData<PagingData<ListStoryItem>>()
    val getDataN: LiveData<PagingData<ListStoryItem>> = dataN
//    var getDataN: LiveData<PagingData<ListStoryItem>> = MutableLiveData()

    private fun testMethod(){
        viewModelScope.launch {
            pref.getUser().collect {
                dataN.postValue()
                    repository.getListStories("Bearer ${it.token}").cachedIn(viewModelScope).
            }
        }
    }*/

/*
    val getDataN: LiveData<PagingData<ListStoryItem>> = pref.getUser().asLiveData().switchMap {
        repository.getListStories("Bearer ${it.token}").cachedIn(viewModelScope)
    }*/
}