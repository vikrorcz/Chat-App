package com.bura.chat.screens.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.bura.chat.data.UserPreferences
import com.bura.chat.net.requests.LoginUser
import com.bura.chat.net.RestClient
import com.bura.chat.net.requests.RegisterUser
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.screens.viewmodel.ui.UiState
import com.bura.chat.util.Screen
import com.bura.chat.util.isEmailValid
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel : ViewModel() {

    lateinit var userPreferences: UserPreferences

    var uiState by mutableStateOf(UiState())

    private val _uiResponse = MutableStateFlow(UiResponse.NULL)
    val uiResponse = _uiResponse.asStateFlow()

    fun setUiResponse(uiResponse: UiResponse) {
        _uiResponse.value = uiResponse
    }

    private fun loginAccount() {

        if (uiState.loginUsername.isEmpty()) {
            setUiResponse(uiResponse = UiResponse.USERNAME_ERROR)
            return
        }

        viewModelScope.launch {
            val response = try {
                val restClient = RestClient(userPreferences.getStringPref(UserPreferences.Prefs.token))
                restClient.api.loginUser(LoginUser(uiState.loginUsername, uiState.loginPassword))
            } catch (e: Exception) {
                setUiResponse(UiResponse.CONNECTION_FAIL)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {

                if (response.body()!!.message == "Logged in") {
                    if (uiState.rememberMe) {
                        userPreferences.setPref(UserPreferences.Prefs.rememberme, true)
                        userPreferences.setPref(UserPreferences.Prefs.token, response.body()!!.token)
                    } else {
                        userPreferences.setPref(UserPreferences.Prefs.rememberme, false)
                    }

                    setUiResponse(UiResponse.LOGIN_SUCCESS)
                }
            } else { setUiResponse(UiResponse.CONNECTION_FAIL) }
        }
    }

    fun autoLoginAccount(navController: NavController) {
        viewModelScope.launch {
            val response = try {
                val restClient = RestClient(userPreferences.getStringPref(UserPreferences.Prefs.token))
                restClient.api.autoLoginUser(userPreferences.getStringPref(UserPreferences.Prefs.token))
            } catch (e: Exception) {
                println(e.message)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                println(response.body()!!.username)
                userPreferences.setPref(UserPreferences.Prefs.username, response.body()!!.username)
                navController.navigate(Screen.ChatScreen.name)

            } else { setUiResponse(UiResponse.CONNECTION_FAIL) }
        }
    }

    fun registerAccount() {
        if (!uiState.registerEmail.isEmailValid()) {
            setUiResponse(UiResponse.EMAIL_ERROR)
            //Toast.makeText(context, context.getString(R.string.invalidemail), Toast.LENGTH_LONG).show()
            return
        }

        if (uiState.registerUsername.isEmpty()) {
            setUiResponse(UiResponse.USERNAME_ERROR)
            //Toast.makeText(context, context.getString(R.string.invalidusername), Toast.LENGTH_LONG).show()
            return
        }

        if (uiState.registerPassword.length < 5) {
            setUiResponse(UiResponse.PASSWORD_ERROR)
            //Toast.makeText(context, context.getString(R.string.invalidpassword), Toast.LENGTH_LONG).show()
            return
        }


        viewModelScope.launch {
            val response = try {
                val restClient = RestClient("")
                restClient.api.registerUser(RegisterUser(uiState.registerEmail, uiState.registerUsername, uiState.registerPassword))
            } catch (e: Exception) {
                setUiResponse(UiResponse.CONNECTION_FAIL)
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                setUiResponse(UiResponse.REGISTRATION_SUCCESS)
            }
        }
    }

    fun onEvent(event: UiEvent) {
        when(event) {
            is UiEvent.LoginUsernameChanged -> {
                uiState = uiState.copy(loginUsername = event.value)
            }
            is UiEvent.loginPasswordChanged -> {
                uiState = uiState.copy(loginPassword = event.value)
            }
            is UiEvent.login -> {
                loginAccount()
            }
            is UiEvent.RegisterEmailChanged -> {
                uiState = uiState.copy(registerEmail = event.value)
            }
            is UiEvent.registerUsernameChanged -> {
                uiState = uiState.copy(registerUsername = event.value)
            }
            is UiEvent.registerPasswordChanged -> {
                uiState = uiState.copy(registerPassword = event.value)
            }
            is UiEvent.RememberMeChanged -> {
                uiState = uiState.copy(rememberMe = event.value)
            }
            is UiEvent.register -> {
                registerAccount()
            }
        }
    }
}

