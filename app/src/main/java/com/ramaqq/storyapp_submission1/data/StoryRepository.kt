package com.ramaqq.storyapp_submission1.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.ramaqq.storyapp_submission1.data.api.ApiService
import com.ramaqq.storyapp_submission1.data.local.room.StoryDatabase
import com.ramaqq.storyapp_submission1.data.response.ListStoryItem

class StoryRepository(private val database: StoryDatabase, private val apiService: ApiService) {

    fun getListStories(token: String): LiveData<PagingData<ListStoryItem>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(database, apiService, token),
            pagingSourceFactory = {
//                StoriesPagingSource(apiService, token)
                database.storyDao().getAllStories()
            }
        ).liveData
    }


/*    // test class

    fun getListNStories(token: String): LiveData<Result<PagingData<ListStoryItem>>> = liveData{
        emit(Result.Loading)
        try {
            val data = Pager(
                config = PagingConfig(pageSize = 5),
                pagingSourceFactory = {StoriesPagingSource(apiService, token)
                }
            ).
            emit(Result.Success())
        }catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }


    fun getLoginResult(username: String, password:String): LiveData<Result<LoginResult>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getLoginData(username, password)
            when{
                !response.error && response.loginResult != null ->{
                    emit(Result.Success(response.loginResult))
                }
            }
        }catch (e: Throwable){
            val errorUiText: UIText = when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<LoginResponse>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<LoginResponse>() {}.type
                        )
                        UIText.DynamicString(response.message)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }
            emit(Result.Error(errorUiText.toString()))
        }
    }

   companion object {
       @Volatile
       private var instance: StoryRepository? = null
       fun getInstance(apiService: ApiService):
               StoryRepository = instance ?: synchronized(this) {
           instance ?: StoryRepository(apiService)
       }.also { instance = it }
   }*/

}

