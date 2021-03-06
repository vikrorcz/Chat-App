package com.bura.chat.screens.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.bura.chat.screens.net.RestClient
import com.bura.chat.screens.net.LoginUser
import com.bura.chat.screens.util.Screen
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {
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

/*
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    fun setMessage(message: String) {
        _message.value = message
    }
*/
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()


    fun loginAccount(navController: NavController) {
        viewModelScope.launch {
            val response = try {
                RestClient.api.loginUser(LoginUser(username.value, password.value))
            } catch (e: Exception) {
                _message.emit("Connection to server failed")
                return@launch
            }


            if (response.isSuccessful && response.body() != null) {

                _message.emit(response.body()!!.message)

                if (response.body()!!.message == "Logged in") {
                    navController.navigate(Screen.ChatScreen.name)
                }
            } else _message.emit("Connection to server failed")
        }
    }
}