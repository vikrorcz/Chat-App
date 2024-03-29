package com.bura.chat.screens.login

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.data.UserPreferences
import com.bura.chat.net.RestClient
import com.bura.chat.net.requests.LoginUser
import com.bura.chat.repository.MessageRepository
import com.bura.chat.repository.ServerRepository
import com.bura.chat.repository.UserPrefsRepository
import com.bura.chat.util.UiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class LoginViewModel(
    private val userPrefsRepository: UserPrefsRepository,
    private val serverRepository: ServerRepository
): ViewModel() {

    var state by mutableStateOf(LoginState())

    val uiResponse = MutableStateFlow<UiResponse>(UiResponse.Null)

    init {
        if (userPrefsRepository.getBooleanPref(UserPreferences.Prefs.rememberme)) {
            autoLoginAccount()
        }
    }

    private fun loginAccount() {
        viewModelScope.launch {
            if (state.loginUsername.isEmpty()) {
                uiResponse.emit(UiResponse.UsernameError)
                return@launch
            }

            val response = try {
                serverRepository.loginUser(LoginUser(state.loginUsername, state.loginPassword))
            } catch (e: Exception) {
                println(e.message)
                uiResponse.emit(UiResponse.ConnectionFail)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {

                if (response.body()!!.message == "Logged in") {
                    if (state.rememberMe) {
                        userPrefsRepository.setPref(UserPreferences.Prefs.rememberme, true)
                        userPrefsRepository.setPref(UserPreferences.Prefs.token, response.body()!!.token)
                    } else {
                        userPrefsRepository.setPref(UserPreferences.Prefs.rememberme, false)
                    }
                    userPrefsRepository.setPref(UserPreferences.Prefs.username, state.loginUsername)
                    uiResponse.emit(UiResponse.LoginSuccess)
                }

                if (response.body()!!.message == "Invalid credentials") {
                    uiResponse.emit(UiResponse.InvalidCredentials)
                }
            } else {
                uiResponse.emit(UiResponse.ConnectionFail)
            }
        }
    }

    private fun autoLoginAccount() {
        viewModelScope.launch {
            val response = try {
                serverRepository.autoLoginUser(userPrefsRepository.getStringPref(UserPreferences.Prefs.token))
            } catch (e: Exception) {
                println(e.message)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                userPrefsRepository.setPref(UserPreferences.Prefs.username, response.body()!!.username)
                uiResponse.emit(UiResponse.LoginSuccess)

            } else {
                uiResponse.emit(UiResponse.TokenExpired)
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginUsernameChanged -> {
                state = state.copy(loginUsername = event.value)
            }
            is LoginEvent.LoginPasswordChanged -> {
                state = state.copy(loginPassword = event.value)
            }
            LoginEvent.Login -> {
                loginAccount()
            }
            is LoginEvent.RememberMeChanged -> {
                state = state.copy(rememberMe = event.value)
            }
        }
    }
}