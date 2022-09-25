package com.bura.chat.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.net.RestClient
import com.bura.chat.net.requests.RegisterUser
import com.bura.chat.util.UiResponse
import com.bura.chat.util.isEmailValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {

    var state by mutableStateOf(RegisterState())

    val uiResponse = MutableStateFlow<UiResponse>(UiResponse.Null)

    private fun registerAccount() {
        viewModelScope.launch {
            if (!state.registerEmail.isEmailValid()) {
                uiResponse.emit(UiResponse.EmailError)
                return@launch
            }

            if (state.registerUsername.isEmpty()) {
                uiResponse.emit(UiResponse.UsernameError)
                return@launch
            }

            if (state.registerPassword.length < 5) {
                uiResponse.emit(UiResponse.PasswordError)
                return@launch
            }

            val response = try {
                val restClient = RestClient()
                restClient.api.registerUser(RegisterUser(state.registerEmail, state.registerUsername, state.registerPassword))
            } catch (e: Exception) {
                uiResponse.emit(UiResponse.ConnectionFail)
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                uiResponse.emit(UiResponse.RegistrationSuccess)
            }
        }
    }

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.RegisterEmailChanged -> {
                state = state.copy(registerEmail = event.value)
            }
            is RegisterEvent.RegisterUsernameChanged -> {
                state = state.copy(registerUsername = event.value)
            }
            is RegisterEvent.RegisterPasswordChanged -> {
                state = state.copy(registerPassword = event.value)
            }
            RegisterEvent.Register -> {
                registerAccount()
            }
            RegisterEvent.AlreadyHaveAnAccount -> {
                viewModelScope.launch {
                    uiResponse.emit(UiResponse.NavigateLoginScreen)
                }
            }
        }
    }
}