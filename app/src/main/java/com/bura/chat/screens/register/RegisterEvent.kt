package com.bura.chat.screens.register

sealed class RegisterEvent {
    data class RegisterEmailChanged(val value: String): RegisterEvent()
    data class RegisterUsernameChanged(val value: String): RegisterEvent()
    data class RegisterPasswordChanged(val value: String): RegisterEvent()
    object Register: RegisterEvent()
    object AlreadyHaveAnAccount: RegisterEvent()
}