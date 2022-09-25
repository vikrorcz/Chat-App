package com.bura.chat.screens.chat

sealed class ChatEvent {
    data class MessageChanged(val value: String): ChatEvent()
    data class SendMessage(val value: String): ChatEvent()
}