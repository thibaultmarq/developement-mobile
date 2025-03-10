package com.tibomrq.todotibomrq.user

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tibomrq.todotibomrq.data.Api
import com.tibomrq.todotibomrq.data.UserUpdate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserViewModel : ViewModel() {

    private val webService = Api.userWebService


    public val usernameStateFlow = MutableStateFlow<String>("")


    fun updateAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            val response = webService.updateAvatar(avatar)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

        }
    }


    fun updateName(uuid : String, newName : String) {
        viewModelScope.launch {
            val userUpdate = UserUpdate.userUpdate(uuid =  uuid, fullName = newName)
            val response = webService.updateName(userUpdate)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }
        }
    }

    fun getUsername() {
        viewModelScope.launch {
            val response = webService.fetchUser()
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }
            val fetchedUsername = response.body()!!.name
            usernameStateFlow.value = fetchedUsername
            Log.e("username", usernameStateFlow.value)
        }
    }


}