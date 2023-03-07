package com.ramaqq.storyapp_submission1.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ramaqq.storyapp_submission1.data.api.ApiService
import com.ramaqq.storyapp_submission1.data.response.ListStoryItem

class StoriesPagingSource(private val apiService: ApiService, private val token: String): PagingSource<Int, ListStoryItem>() {

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllCompleteStories(params.loadSize, position, token = token).listStory
            for (item in responseData){
                Log.d("USER",item.name+" "+item.photoUrl)
            }
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        }catch (exception: Exception){
            return LoadResult.Error(exception)
        }
    }

}