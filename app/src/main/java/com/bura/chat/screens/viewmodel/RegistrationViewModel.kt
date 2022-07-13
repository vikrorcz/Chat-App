package com.bura.chat.screens.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.screens.data.RestClient
import com.bura.chat.screens.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    fun setEmail(email: String) {
        _email.value = email
    }

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    fun setUsername(username: String) {
        _username.value = username
    }

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun setPassword(password: String) {
        _password.value = password
    }

    fun registerAccount() {
        Log.e("Registered","account")
        viewModelScope.launch {
            val call = try {//
                RestClient.api.registerUser(User(email.value, password.value))
                //RestClient.api.getUser()

            } catch (e: Exception) {
                Log.e("Exception", e.message.toString())
                return@launch
            }


            //if (response.isSuccessful) {// && response.body() != null) {
            //   println(response.message())
            //}
        }
    }
}