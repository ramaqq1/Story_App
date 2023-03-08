package com.ramaqq.storyapp_submission1.ui.addstory

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramaqq.storyapp_submission1.data.api.ApiClient
import com.ramaqq.storyapp_submission1.pojo.UploadResponse
import com.ramaqq.storyapp_submission1.data.local.entity.UserPreference
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel: ViewModel() {
    private lateinit var pref: UserPreference
    private lateinit var desc: String
    private lateinit var img: MultipartBody.Part

    private val isLoading = MutableLiveData<Boolean>()
    val getLoading: LiveData<Boolean> = isLoading

    private val isError = MutableLiveData<Boolean>()
    val getError: LiveData<Boolean> = isError

    private val status = MutableLiveData<String>()
    val getStatus: LiveData<String> = status

    private val location = MutableLiveData<Location>()
//    val getLocation: LiveData<Location> = location



    fun init(pref: UserPreference, desc: String, img: MultipartBody.Part){
        this.pref = pref
        this.desc = desc
        this.img = img
        uploadImage()
    }

    fun setLocation(location: Location){
        this.location.value = location
    }

    private fun uploadImage() {
        isLoading.value = true
        var token: String? = null
        viewModelScope.launch {
            pref.getUser().collect{
                token = it.token

                val client = ApiClient.getApiService().getUploadStory("Bearer $token", desc,
                    location.value?.latitude?.toFloat(), location.value?.longitude?.toFloat(), img)
                client.enqueue(object : Callback<UploadResponse?> {
                    override fun onResponse(call: Call<UploadResponse?>, response: Response<UploadResponse?>) {
                        isError.value = false
                        if (response.isSuccessful && response.body() != null){
                            isLoading.value = false
                            status.postValue(response.body()?.message)
                        }else{
                            isLoading.value = false
                            val data = response.errorBody()!!.string()

                            try {
                                val jObjError = JSONObject(data)
                                status.postValue( jObjError.getString("message"))
                            } catch (e: Exception) {
                                // do nothing
                            }
                        }
                    }

                    override fun onFailure(call: Call<UploadResponse?>, t: Throwable) {
                        isError.value = true
                        isLoading.value = false
                    }
                })
            }

        }
    }


}