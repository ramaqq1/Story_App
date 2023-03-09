package com.ramaqq.storyapp_submission1.data.api

import com.ramaqq.storyapp_submission1.data.response.*
import com.ramaqq.storyapp_submission1.pojo.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("login")
    fun getLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // login ver 2
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("login")
    fun getLoginData(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    fun getRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignUpResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String,
        @Query("location") loc: String? = null,
        @Query("size") size: Int? = null,
        @Query("page") page: Int? = null
    ): Call<StoriesResponse>

    @GET("stories")
    suspend fun getAllCompleteStories(
        @Query("size") size: Int,
        @Query("page") page : Int,
        @Query("location") location: String? = null,
        @Header("Authorization") token: String
    ): StoriesResponse

    @GET("stories/{id}")
    fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailResponse>

    @Multipart
    @POST("stories")
    fun getUploadStory(
        @Header("Authorization") token: String,
        @Part("description") description: String,
        @Part("lat") lat: Float? = null,
        @Part("lon") lon: Float? = null,
        @Part file: MultipartBody.Part
    ): Call<UploadResponse>
}