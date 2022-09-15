package com.bura.chat.net

import com.bura.chat.net.websocket.ChatMessage
import com.google.gson.Gson
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class MyWebSocketListener: WebSocketListener() {

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
        t.printStackTrace()
        super.onFailure(webSocket, t, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("MESSAGE: $text");
        super.onMessage(webSocket, text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        println("MESSAGE: ${bytes.hex()}");
        super.onMessage(webSocket, bytes)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        //webSocket.send("Hello message")
        //webSocket.close(STATUS_CODE, "End of chat")

        super.onOpen(webSocket, response)
    }
}