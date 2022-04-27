package com.example.submission1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission1.api.ApiConfig
import com.example.submission1.api.ListStory
import com.example.submission1.api.StoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {

    private val privateIsLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = privateIsLoading
    val listsStory = MutableLiveData<List<ListStory>>()

    fun setStories(token: String?) {
        privateIsLoading.value = true
        val stories = ApiConfig.getApiService().getStories("Bearer $token", null,null)
        stories.enqueue(object : Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Log.d(".HomeActivity","list story :\n $responseBody")
                        val storyFetch = ArrayList<ListStory>()
                        for (story in responseBody.listStory) {
                            val storys = ListStory(story.photoUrl, story.createdAt,
                            story.name,story.description,story.lon, story.id, story.lat)
                            storyFetch.add(storys)
                        }
                        listsStory.postValue(storyFetch)
                    }
                }else{
                    val errmess = when (response.code()) {
                        401 -> "${response.code()} : Bad Request"
                        403 -> "${response.code()} : Forbidden"
                        404 -> "${response.code()} : Not Found"
                        else -> "${response.code()} : errrr"
                    }
                    Log.e(".HomeActivity","$errmess")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getStories(): LiveData<List<ListStory>> {
        return listsStory
    }

}