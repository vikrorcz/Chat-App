package com.bura.chat.net.websocket

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val username: String,
    val receivingUsername: String,
    val message: String
)