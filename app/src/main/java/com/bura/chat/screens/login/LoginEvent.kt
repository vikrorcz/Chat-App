package com.bura.chat.screens.login

sealed  class LoginEvent {
    data class LoginUsernameChanged(val value: String): LoginEvent()
    data class LoginPasswordChanged(val value: String): LoginEvent()
    data class RememberMeChanged(val value: Boolean): LoginEvent()
    object Login: LoginEvent()
}