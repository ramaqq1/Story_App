package com.ramaqq.storyapp_submission1.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ramaqq.storyapp_submission1.data.StoryRepository
import com.ramaqq.storyapp_submission1.ui.login.LoginViewModelN
import com.ramaqq.storyapp_submission1.ui.stories.StoriesViewModel

class ViewModelFactory private constructor(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModelN::class.java)) {
            return LoginViewModelN(storyRepository) as T
        }else if (modelClass.isAssignableFrom(StoriesViewModel::class.java)){
            return StoriesViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}