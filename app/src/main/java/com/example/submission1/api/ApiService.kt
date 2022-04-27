package com.example.submission1.api

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

data class RegisterResponse(
    @field:SerializedName("error")
    @Expose
    val error: Boolean,
    @field:SerializedName("message")
    @Expose
    val message: String
)

data class LoginResponse(
    @field:SerializedName("error")
    @Expose
    val error: Boolean,
    @field:SerializedName("message")
    @Expose
    val message: String,
    @field:SerializedName("loginResult")
    @Expose
    val loginResult: LoginResult,
)

data class LoginResult(
    @field:SerializedName("userId")
    @Expose
    val userId: String,
    @field:SerializedName("name")
    @Expose
    val name: String,
    @field:SerializedName("token")
    @Expose
    val token: String
)

data class StoriesResponse(
    @field:SerializedName("error")
    @Expose
    val error: Boolean,
    @field:SerializedName("message")
    @Expose
    val message: String,
    @field:SerializedName("listStory")
    @Expose
    val listStory: List<ListStory>,
)

@Parcelize
data class ListStory(
    @field:SerializedName("photoUrl")
    @Expose
    val photoUrl: String,
    @field:SerializedName("createdAt")
    @Expose
    val createdAt: String,
    @field:SerializedName("name")
    @Expose
    val name: String,
    @field:SerializedName("description")
    @Expose
    val description: String,
    @field:SerializedName("lon")
    @Expose
    val lon: Double,
    @field:SerializedName("id")
    @Expose
    val id: String,
    @field:SerializedName("lat")
    @Expose
    val lat: Double
): Parcelable

data class FileUploadResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)

interface ApiService {

    @FormUrlEncoded
    @POST("/v1/register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/v1/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("/v1/stories")
//    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVNDWHVNZEY3aWZlSkVzTE0iLCJpYXQiOjE2NTAwMTM1MTN9.64uDLECje2qiagVw27nVxfYwUToWWRolUI3BRKsCBzU")
    fun getStories(
        @Header("Authorization") Authorization:String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Boolean? = false,
    ): Call<StoriesResponse>

    @Multipart
    @POST("/v1/stories")
    fun upload(
        @Header("Authorization") Authorization:String,
        @Part("description") description: String,
        @Part file: MultipartBody.Part
    ): Call<FileUploadResponse>
}