package com.bura.chat.screens.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.bura.chat.data.UserPreferences
import com.bura.chat.data.room.contacts.Contact
import com.bura.chat.data.room.contacts.ContactDatabase
import com.bura.chat.data.room.messages.Message
import com.bura.chat.data.room.messages.MessageDatabase
import com.bura.chat.net.MyWebSocketListener
import com.bura.chat.net.RestClient
import com.bura.chat.net.requests.LoginUser
import com.bura.chat.net.requests.RegisterUser
import com.bura.chat.net.requests.SearchUser
import com.bura.chat.net.requests.UpdateUserPassword
import com.bura.chat.net.websocket.ChatMessage
import com.bura.chat.screens.viewmodel.ui.SearchedUser
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.screens.viewmodel.ui.UiState
import com.bura.chat.util.isEmailValid
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {

    val userPreferences = UserPreferences(getApplication())
    private val contactDb = Room.databaseBuilder(
        getApplication(),
        ContactDatabase::class.java, "contact_database"
    ).build()

    private val contactDao = contactDb.contactDao()

    private val messageDb = Room.databaseBuilder(
        getApplication(),
        MessageDatabase::class.java, "message_database"
    ).build()

    val messageDao = messageDb.messageDao()

    private val listener = MyWebSocketListener(this)

    val webSocket = listener.client.newWebSocket(listener.request, listener)

    var uiState by mutableStateOf(UiState())

    //If i use shared flow then auto login does not work
    val uiResponse = MutableStateFlow<UiResponse>(UiResponse.Null)

    init {
        //Connect to chat and start receiving websockets
        // TODO: https://stackoverflow.com/questions/71666792/viewmodel-instantiated-in-compose-with-hiltviewmodel-called-multiple-times
        // TODO: FIX -> create multiple viewmodels and call connectToChat() only once in LoginViewModel
        //connectToChat()

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
                val restClient = RestClient()
                restClient.api.loginUser(LoginUser(uiState.loginUsername, uiState.loginPassword))
            } catch (e: Exception) {
                println(e.message)
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
                    userPreferences.setPref(UserPreferences.Prefs.username, uiState.loginUsername)
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
            val contact = this.getContactByName(userPreferences.getStringPref(UserPreferences.Prefs.username), searchedUser.username)

            if (searchedUser.username == userPreferences.getStringPref(UserPreferences.Prefs.username)) {
                uiResponse.emit(UiResponse.CannotAddYourself)
                return
            }

            if (contact != null) {
                uiResponse.emit(UiResponse.ContactAlreadyAdded)
                return
            }

            this.insert(Contact(0, userPreferences.getStringPref(UserPreferences.Prefs.username), searchedUser.username, searchedUser.email))
            uiResponse.emit(UiResponse.ContactSuccessfullyAdded)
        }
    }

    suspend fun getContactList(): MutableList<Contact> {
        var list : MutableList<Contact>
        with (contactDao) {
            list = this.getContactList(userPreferences.getStringPref(UserPreferences.Prefs.username))
        }
        return list
    }

    suspend fun getMessageListFromSender(sendingUser: String): MutableList<Message> {
        var receivedMessagesList : MutableList<Message>//Messages sent by the other user
        var sentMessagesList: MutableList<Message>//Messages sent by the user
        with (messageDao) {
            receivedMessagesList = this.getMessageListFromSender(sendingUser, userPreferences.getStringPref(UserPreferences.Prefs.username))
            sentMessagesList = this.getMessageListFromSender(userPreferences.getStringPref(UserPreferences.Prefs.username), sendingUser)
        }

        receivedMessagesList.addAll(sentMessagesList)
        receivedMessagesList.sortBy { it.id }

        return receivedMessagesList
    }

    private suspend fun deleteContact(name: String) {
        with (contactDao) {
            val contact = this.getContactByName(userPreferences.getStringPref(UserPreferences.Prefs.username), name)
            this.delete(contact!!)
            uiResponse.emit(UiResponse.DeleteUserFromList(contact = contact))
        }
    }


    // TODO: bug jozin cant send message to viktor, but viktor can send to jozin
    //    java.io.EOFException
    //    at okio.RealBufferedSource.require(RealBufferedSource.kt:199)
    //    at okio.RealBufferedSource.readByte(RealBufferedSource.kt:209)
    //    at okhttp3.internal.ws.WebSocketReader.readHeader(WebSocketReader.kt:119)
    //    at okhttp3.internal.ws.WebSocketReader.processNextFrame(WebSocketReader.kt:102)
    //    at okhttp3.internal.ws.RealWebSocket.loopReader(RealWebSocket.kt:293)
    //    at okhttp3.internal.ws.RealWebSocket$connect$1.onResponse(RealWebSocket.kt:195)
    //    at okhttp3.internal.connection.RealCall$AsyncCall.run(RealCall.kt:519)
    //    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
    //    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
    private fun sendMessage(receivingUser: String) {
        try {
            val chatMessage = ChatMessage(
                userPreferences.getStringPref(UserPreferences.Prefs.username),
                receivingUser,
                uiState.message
            )
            val jsonObject = Gson().toJson(chatMessage)
            webSocket.send(jsonObject)

            viewModelScope.launch {
                val message = Message(0, chatMessage.message, chatMessage.username, chatMessage.receivingUsername)
                with (messageDao) {
                    this.insert(message)
                }
                uiResponse.emit(UiResponse.AddMessageToList(message = message))
            }
            println("Message successfully sent")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun disconnectFromChat() {
        val chatMessage = ChatMessage(
            userPreferences.getStringPref(UserPreferences.Prefs.username),
            "",
            "disconnect"
        )
        val jsonObject = Gson().toJson(chatMessage)
        webSocket.send(jsonObject)
        //webSocket.close(1000, "End of session")
    }

    fun connectToChat() {
        val message = ChatMessage(
            userPreferences.getStringPref(UserPreferences.Prefs.username),
            "",
            ""
        )
        val jsonObject = Gson().toJson(message)
        webSocket.send(jsonObject)
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

            //=================================RECENT CHAT SCREEN===================================
            UiEvent.Contacts -> {
                viewModelScope.launch {
                    uiResponse.emit(UiResponse.NavigateContactScreen)
                }
            }

            //================================== CHAT SCREEN========================================
            is UiEvent.MessageChanged -> {
                uiState = uiState.copy(message = event.value)
            }

            is UiEvent.SendMessage -> {
                sendMessage(event.value)
            }

            //==================================CONTACTS SCREEN=====================================
            UiEvent.AddContact -> {
                viewModelScope.launch {
                    uiResponse.emit(UiResponse.NavigateAddContactScreen)
                }
            }

            is UiEvent.DeleteUserContact -> {
                viewModelScope.launch {
                    deleteContact(event.name)
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

