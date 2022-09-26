package com.bura.chat.screens.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.data.UserPreferences
import com.bura.chat.data.room.messages.Message
import com.bura.chat.net.websocket.MyWebSocketListener
import com.bura.chat.net.websocket.ChatMessage
import com.bura.chat.repository.MessageRepository
import com.bura.chat.repository.UserPrefsRepository
import com.bura.chat.util.UiResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val userPrefsRepository: UserPrefsRepository,
    val messageRepository: MessageRepository
): ViewModel() {

    var state by mutableStateOf(ChatState())
    val uiResponse = MutableStateFlow<UiResponse>(UiResponse.Null)

    private val listener = MyWebSocketListener(this, messageRepository)
    private val webSocket = listener.client.newWebSocket(listener.request, listener)

    init {
        connectToChat()
    }

    suspend fun getMessageListFromSender(sendingUser: String): MutableList<Message> {
        var receivedMessagesList : MutableList<Message>//Messages sent by the other user
        var sentMessagesList: MutableList<Message>//Messages sent by the user
        with (messageRepository) {
            receivedMessagesList = this.getMessageListFromSender(sendingUser, userPrefsRepository.getStringPref(
                UserPreferences.Prefs.username))
            sentMessagesList = this.getMessageListFromSender(userPrefsRepository.getStringPref(
                UserPreferences.Prefs.username), sendingUser)
        }

        receivedMessagesList.addAll(sentMessagesList)
        receivedMessagesList.sortBy { it.id }

        return receivedMessagesList
    }

    private fun sendMessage(receivingUser: String) {
        try {
            val chatMessage = ChatMessage(
                userPrefsRepository.getStringPref(UserPreferences.Prefs.username),
                receivingUser,
                state.message
            )
            val jsonObject = Gson().toJson(chatMessage)
            webSocket.send(jsonObject)

            viewModelScope.launch {
                val message = Message(0, chatMessage.message, chatMessage.username, chatMessage.receivingUsername)
                with (messageRepository) {
                    this.insert(message)
                }
                uiResponse.emit(UiResponse.AddMessageToList(message = message))
            }
            println("Message successfully sent")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun connectToChat() {
        val message = ChatMessage(
            userPrefsRepository.getStringPref(UserPreferences.Prefs.username),
            "",
            ""
        )
        val jsonObject = Gson().toJson(message)
        webSocket.send(jsonObject)
    }

    fun getCurrentUser(): String {
        return userPrefsRepository.getStringPref(UserPreferences.Prefs.username)
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.MessageChanged -> {
                state = state.copy(message = event.value)
            }

            is ChatEvent.SendMessage -> {
                sendMessage(event.value)
            }
        }
    }
}