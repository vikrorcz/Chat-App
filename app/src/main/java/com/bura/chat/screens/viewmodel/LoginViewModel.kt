package com.bura.chat.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.bura.chat.data.UserPreferences
import com.bura.chat.net.requests.LoginUser
import com.bura.chat.net.RestClient
import com.bura.chat.util.Screen
import com.bura.chat.util.TokenInterceptor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


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
                val restClient = RestClient(userPreferences.getStringPref(UserPreferences.Prefs.token))
                restClient.api.loginUser(LoginUser(username.value, password.value))
                //RestClient.api.loginUser(LoginUser(username.value, password.value))
            } catch (e: Exception) {
                _message.emit("Connection to server failed")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                _message.emit(response.body()!!.message)

                if (response.body()!!.message == "Logged in") {
                    if (rememberMe.value) {
                        userPreferences.setPref(UserPreferences.Prefs.rememberme, true)
                        userPreferences.setPref(UserPreferences.Prefs.token, response.body()!!.token)
                    } else {
                        userPreferences.setPref(UserPreferences.Prefs.rememberme, false)
                    }

                    navController.navigate(Screen.ChatScreen.name)
                }
            } else _message.emit("Connection to server failed")
        }
    }

    fun autoLoginAccount(navController: NavController) {
        viewModelScope.launch {
            val response = try {
                //RestClient.tokenInterceptor = TokenInterceptor(userPreferences.getStringPref(UserPreferences.Prefs.token))
                //RestClient.api.autoLoginUser(userPreferences.getStringPref(UserPreferences.Prefs.token))
                val restClient = RestClient(userPreferences.getStringPref(UserPreferences.Prefs.token))
                restClient.api.autoLoginUser(userPreferences.getStringPref(UserPreferences.Prefs.token))
            } catch (e: Exception) {
                println(e.message)
                _message.emit(e.message.toString())
                return@launch
            }

            println("auto logging")

            if (response.isSuccessful && response.body() != null) {
                _message.emit(response.body()!!.username)
                println(response.body()!!.username)
                userPreferences.setPref(UserPreferences.Prefs.username, response.body()!!.username)
                navController.navigate(Screen.ChatScreen.name)

            } else _message.emit("Connection to server failed")
        }
    }

    //init {
    //    autoLoginAccount(navController = )
    //}
}

