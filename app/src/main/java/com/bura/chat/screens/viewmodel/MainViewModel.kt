package com.bura.chat.screens.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.bura.chat.data.UserPreferences
import com.bura.chat.data.room.ContactDao
import com.bura.chat.data.room.ContactDatabase
import com.bura.chat.net.RestClient
import com.bura.chat.net.requests.LoginUser
import com.bura.chat.net.requests.RegisterUser
import com.bura.chat.net.requests.UpdateUserPassword
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.screens.viewmodel.ui.UiState
import com.bura.chat.util.isEmailValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences: UserPreferences = UserPreferences(getApplication())
    private val roomDb: ContactDatabase = Room.databaseBuilder(
        getApplication(),
        ContactDatabase::class.java, "contact_database"
    ).build()
    private val contactDao: ContactDao = roomDb.contactDao()

    var uiState by mutableStateOf(UiState())

    // TODO: how to do this better?
    private val _uiResponse = MutableStateFlow(UiResponse.NULL)
    val uiResponse = _uiResponse.asStateFlow()

    fun setUiResponse(uiResponse: UiResponse) {
        _uiResponse.value = uiResponse
    }

    init {
        if (userPreferences.getBooleanPref(UserPreferences.Prefs.rememberme)) {
            autoLoginAccount()
        }
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

    private fun autoLoginAccount() {
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
                setUiResponse(UiResponse.LOGIN_SUCCESS)

            } else { setUiResponse(UiResponse.CONNECTION_FAIL) }
        }
    }

    private fun registerAccount() {
        if (!uiState.registerEmail.isEmailValid()) {
            setUiResponse(UiResponse.EMAIL_ERROR)
            return
        }

        if (uiState.registerUsername.isEmpty()) {
            setUiResponse(UiResponse.USERNAME_ERROR)
            return
        }

        if (uiState.registerPassword.length < 5) {
            setUiResponse(UiResponse.PASSWORD_ERROR)
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

    private fun changePassword() {
        viewModelScope.launch {
            val response = try {
                val restClient = RestClient("")
                restClient.api.updatePassword(
                    UpdateUserPassword(userPreferences.getStringPref(UserPreferences.Prefs.username),
                        uiState.settingsCurrentPassword,
                        uiState.settingsNewPassword))

            } catch (e: Exception) {
                setUiResponse(UiResponse.CONNECTION_FAIL)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.message == "Current password is invalid") {
                    setUiResponse(UiResponse.CHANGE_PASSWORD_FAIL)
                    return@launch
                }
                setUiResponse(UiResponse.CHANGE_PASSWORD_SUCCESS)
            }
        }
    }

    fun onEvent(event: UiEvent) {
        when(event) {
            //=================================LOGIN SCREEN=========================================
            is UiEvent.LoginUsernameChanged -> {
                uiState = uiState.copy(loginUsername = event.value)
            }
            is UiEvent.LoginPasswordChanged -> {
                uiState = uiState.copy(loginPassword = event.value)
            }
            is UiEvent.Login -> {
                loginAccount()
            }

            //=================================REGISTER SCREEN======================================
            is UiEvent.RegisterEmailChanged -> {
                uiState = uiState.copy(registerEmail = event.value)
            }
            is UiEvent.RegisterUsernameChanged -> {
                uiState = uiState.copy(registerUsername = event.value)
            }
            is UiEvent.RegisterPasswordChanged -> {
                uiState = uiState.copy(registerPassword = event.value)
            }
            is UiEvent.RememberMeChanged -> {
                uiState = uiState.copy(rememberMe = event.value)
            }
            is UiEvent.Register -> {
                registerAccount()
            }
            is  UiEvent.AlreadyHaveAnAccount -> {
                setUiResponse(UiResponse.LOGIN_SCREEN)
            }

            //=================================SETTINGS SCREEN======================================
            UiEvent.ChangePassword -> {
                changePassword()
            }
            is UiEvent.CurrentPasswordChanged -> {
                uiState = uiState.copy(settingsCurrentPassword = event.value)
            }
            is UiEvent.NewPasswordChanged -> {
                uiState = uiState.copy(settingsNewPassword = event.value)
            }
        }
    }
}

