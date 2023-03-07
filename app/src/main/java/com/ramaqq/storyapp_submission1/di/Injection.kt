package com.ramaqq.storyapp_submission1.di

import android.content.Context
import com.ramaqq.storyapp_submission1.data.api.ApiClient
import com.ramaqq.storyapp_submission1.data.StoryRepository
import com.ramaqq.storyapp_submission1.data.local.room.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository{
        val database= StoryDatabase.getDatabase(context)
        val apiService =  ApiClient.getApiService()
        return StoryRepository(database, apiService)
    }
}