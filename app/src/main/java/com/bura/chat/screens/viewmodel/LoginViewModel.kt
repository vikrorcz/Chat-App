package com.bura.chat.screens.viewmodel

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.bura.chat.data.UserPreferences
import com.bura.chat.net.LoginUser
import com.bura.chat.net.RestClient
import com.bura.chat.util.Screen
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect


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

    private val _rememberMe = MutableStateFlow(false)
    val rememberMe = _rememberMe.asStateFlow()

    fun setRememberMe(boolean: Boolean) {
        _rememberMe.value = boolean
    }

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    lateinit var userPreferences: UserPreferences

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

                    if (rememberMe.value) {
                        userPreferences.setPref(UserPreferences.Prefs.rememberme, true)
                    }

                    navController.navigate(Screen.ChatScreen.name)
                }
            } else _message.emit("Connection to server failed")
        }
    }
}

