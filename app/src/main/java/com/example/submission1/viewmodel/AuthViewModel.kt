package com.example.submission1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission1.api.ApiConfig
import com.example.submission1.api.LoginResponse
import com.example.submission1.api.LoginResult
import com.example.submission1.api.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(): ViewModel() {

    private val privateIsLoading = MutableLiveData<Boolean>()
    val isloading: LiveData<Boolean> = privateIsLoading
    val regResponse = MutableLiveData<Boolean>()
    val loginData = MutableLiveData<LoginResult>()
    val logResponse = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    fun getErrorMessageLogin(): LiveData<String> = errorMessage

    fun getRegResponse(): LiveData<Boolean> {
        return regResponse
    }

    fun getLogResponse(): LiveData<Boolean> {
        return logResponse
    }

    fun getLoginData(): LiveData<LoginResult> {
        return loginData
    }

    fun registerLauncher(name:String, email: String, password:String) {
        privateIsLoading.value = true
        Log.d(".RegisterActivity", "$name, $email, $password")
        val launchRegister = ApiConfig.getApiService().postRegister(name, email, password)

        launchRegister.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Log.d(".RegisterActivity","regis berhasil $responseBody")
                        regResponse.postValue(true)
                    }
                }else{
                    val errMess = when (response.code()) {
                        401 -> "${response.code()} : Bad Request"
                        403 -> "${response.code()} : Forbidden"
                        404 -> "${response.code()} : Not Found"
                        else -> "${response.code()} : $response"
                    }
                    errorMessage.postValue(errMess)
                    Log.e(".RegisterActivity","$errMess")
                    regResponse.postValue(false)
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(".RegisterActivity","retrofit register failure")
            }
        })
    }

    fun loginLauncher(email: String, password:String) {
        privateIsLoading.value = true
        val launchLogin = ApiConfig.getApiService().login(email, password)
        launchLogin.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Log.d(".RegisterActivity","login berhasil $responseBody")
                        loginData.postValue(responseBody.loginResult)
                        logResponse.postValue(true)
                    }
                }else{
                    Log.e(".RegisterActivity","login gagal ${response.message()}")
                    logResponse.postValue(false)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(".RegisterActivity","retrofit login failure")
            }

        })
    }
}