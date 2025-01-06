package com.tibomrq.todotibomrq.user

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tibomrq.todotibomrq.data.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserViewModel : ViewModel() {

    private val webService = Api.userWebService


    fun updateAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            val response = webService.updateAvatar(avatar)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }
        }
    }




}