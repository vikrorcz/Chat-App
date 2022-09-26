package com.bura.chat.net.websocket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.data.room.messages.Message
import com.bura.chat.repository.MessageRepository
import com.bura.chat.screens.chat.ChatViewModel
import com.bura.chat.util.UiResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.*
import java.util.concurrent.TimeUnit

class MyWebSocketListener(
    val viewModel: ViewModel?,
    private val messageRepository: MessageRepository

): WebSocketListener() {

    private val statusCode = 1000

    //ws implementation, possibly migrate to ktor client, because retrofit does not support ws
    val client  = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    val request = Request.Builder()
        .url("ws://192.168.254.38:8080/chat")
        .build()

    //init {
        // Follows advice from https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/WebSocketEcho.java
        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        // client.dispatcher.executorService.shutdown()
    //}

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        println("CLOSED: $code $reason");
        super.onClosed(webSocket, code, reason)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(statusCode, null)
        println("CLOSING: $code $reason");
        super.onClosing(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        println("onFailure: ${t.message}")
        println("onFailure: ${t.printStackTrace()}")
        super.onFailure(webSocket, t, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("Received message: $text");

        val messageDeserialized = Gson().fromJson(text, ChatMessage::class.java)

        val message = Message(
            0,
            sendingUser = messageDeserialized.username,
            receivingUser = messageDeserialized.receivingUsername,
            message = messageDeserialized.message
        )

        viewModel?.viewModelScope?.launch {
            if (viewModel is ChatViewModel) {
                viewModel.messageRepository.insert(message)
                viewModel.uiResponse.emit(UiResponse.AddMessageToList(message = message))
            } else {
                messageRepository.insert(message)
            }
        }
        super.onMessage(webSocket, text)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        //webSocket.send("Hello message")
        //webSocket.close(statusCode, "End of chat")

        super.onOpen(webSocket, response)
    }
}