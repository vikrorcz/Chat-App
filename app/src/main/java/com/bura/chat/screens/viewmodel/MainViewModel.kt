package com.bura.chat.screens.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.bura.chat.data.UserPreferences
import com.bura.chat.data.room.Contact
import com.bura.chat.data.room.ContactDatabase
import com.bura.chat.net.RestClient
import com.bura.chat.net.requests.LoginUser
import com.bura.chat.net.requests.RegisterUser
import com.bura.chat.net.requests.SearchUser
import com.bura.chat.net.requests.UpdateUserPassword
import com.bura.chat.screens.viewmodel.ui.SearchedUser
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.screens.viewmodel.ui.UiState
import com.bura.chat.util.isEmailValid
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(getApplication())
    private val roomDb = Room.databaseBuilder(
        getApplication(),
        ContactDatabase::class.java, "contact_database"
    ).build()

    private val contactDao = roomDb.contactDao()

    var uiState by mutableStateOf(UiState())

    //If i use shared flow then auto login does not work
    val uiResponse = MutableStateFlow<UiResponse>(UiResponse.Null)

    init {
        if (userPreferences.getBooleanPref(UserPreferences.Prefs.rememberme)) {
            autoLoginAccount()
        }
    }

    private fun loginAccount() {
        viewModelScope.launch {
            if (uiState.loginUsername.isEmpty()) {
                uiResponse.emit(UiResponse.UsernameError)
                return@launch
            }

            val response = try {
                val restClient = RestClient(userPreferences.getStringPref(UserPreferences.Prefs.token))
                restClient.api.loginUser(LoginUser(uiState.loginUsername, uiState.loginPassword))
            } catch (e: Exception) {
                uiResponse.emit(UiResponse.ConnectionFail)
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
                val restClient = RestClient(userPreferences.getStringPref(UserPreferences.Prefs.token))
                restClient.api.autoLoginUser(userPreferences.getStringPref(UserPreferences.Prefs.token))
            } catch (e: Exception) {
                println(e.message)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                userPreferences.setPref(UserPreferences.Prefs.username, response.body()!!.username)
                uiResponse.emit(UiResponse.LoginSuccess)

            } else {
                uiResponse.emit(UiResponse.TokenExpired)
            }
        }
    }

    private fun registerAccount() {
        viewModelScope.launch {
            if (!uiState.registerEmail.isEmailValid()) {
                uiResponse.emit(UiResponse.EmailError)
                return@launch
            }

            if (uiState.registerUsername.isEmpty()) {
                uiResponse.emit(UiResponse.UsernameError)
                return@launch
            }

            if (uiState.registerPassword.length < 5) {
                uiResponse.emit(UiResponse.PasswordError)
                return@launch
            }

            val response = try {
                val restClient = RestClient()
                restClient.api.registerUser(RegisterUser(uiState.registerEmail, uiState.registerUsername, uiState.registerPassword))
            } catch (e: Exception) {
                uiResponse.emit(UiResponse.ConnectionFail)
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                uiResponse.emit(UiResponse.RegistrationSuccess)
            }
        }
    }

    private fun changePassword() {
        viewModelScope.launch {
            val response = try {
                val restClient = RestClient()
                restClient.api.updatePassword(
                    UpdateUserPassword(userPreferences.getStringPref(UserPreferences.Prefs.username),
                        uiState.settingsCurrentPassword,
                        uiState.settingsNewPassword))

            } catch (e: Exception) {
                uiResponse.emit(UiResponse.ConnectionFail)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.message == "Current password is invalid") {
                    uiResponse.emit(UiResponse.ChangePasswordFail)
                    return@launch
                }
                uiResponse.emit(UiResponse.ChangePasswordSuccess)
            }
        }
    }

    private fun searchUser() {
        viewModelScope.launch {
            val response = try {
                val restClient = RestClient()
                restClient.api.searchUser(SearchUser(uiState.addNewContact))
            } catch (e: Exception) {
                uiResponse.emit(UiResponse.ConnectionFail)
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.message == "User found") {
                    uiResponse.emit(UiResponse.SearchUser(SearchedUser(response.body()!!.username, response.body()!!.email)))
                    println("Username= ${response.body()!!.username} email= ${response.body()!!.email}")
                }

                if (response.body()!!.message == "User not found") {
                    uiResponse.emit(UiResponse.UserNotFound)
                }
            }
        }
    }

    private fun logout() {
        userPreferences.setPref(UserPreferences.Prefs.rememberme, false)
        viewModelScope.launch {
            uiResponse.emit(UiResponse.NavigateLoginScreen)
        }
    }

    private suspend fun addContact(searchedUser: SearchedUser) {
        with (contactDao) {
            val contact = this.getContactByName(searchedUser.username)
            if (contact != null) {
                uiResponse.emit(UiResponse.ContactAlreadyAdded)
                return
            }

            this.insert(Contact(0, searchedUser.username, searchedUser.email))
        }
    }

    suspend fun getContactList(): MutableList<Contact> {
        var list : MutableList<Contact>
        with (contactDao) {
            list = this.getContactList()
        }
        return list
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            //=================================LOGIN SCREEN=========================================
            is UiEvent.LoginUsernameChanged -> {
                uiState = uiState.copy(loginUsername = event.value)
            }
            is UiEvent.LoginPasswordChanged -> {
                uiState = uiState.copy(loginPassword = event.value)
            }
            UiEvent.Login -> {
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
            UiEvent.Register -> {
                registerAccount()
            }
            UiEvent.AlreadyHaveAnAccount -> {
                viewModelScope.launch {
                    uiResponse.emit(UiResponse.NavigateLoginScreen)
                }
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

            //===================================CHAT SCREEN========================================
            UiEvent.Contacts -> {
                viewModelScope.launch {
                    uiResponse.emit(UiResponse.NavigateContactScreen)
                }
            }

            //==================================CONTACTS SCREEN=====================================
            UiEvent.AddContact -> {
                viewModelScope.launch {
                    uiResponse.emit(UiResponse.NavigateAddContactScreen)
                }
            }

            //===================================PROFILE SCREEN=====================================
            UiEvent.Logout -> {
                logout()
            }

            //=================================ADD CONTACT SCREEN====================================
            is UiEvent.AddContactChanged -> {
                uiState = uiState.copy(addNewContact = event.value)
            }

            UiEvent.SearchUser -> {
                searchUser()
            }

            is UiEvent.AddUserContact -> {
                viewModelScope.launch {
                    addContact(event.user)
                }
            }
        }
    }
}

