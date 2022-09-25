package com.bura.chat.screens.screen.login

import com.bura.chat.screens.viewmodel.ui.UiEvent

sealed  class LoginEvent {
    data class LoginUsernameChanged(val value: String): LoginEvent()
    data class LoginPasswordChanged(val value: String): LoginEvent()
    data class RememberMeChanged(val value: Boolean): LoginEvent()
    object Login: LoginEvent()
}