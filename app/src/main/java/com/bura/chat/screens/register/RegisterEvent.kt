package com.bura.chat.screens.screen.register

import com.bura.chat.screens.viewmodel.ui.UiEvent

sealed class RegisterEvent {
    data class RegisterEmailChanged(val value: String): RegisterEvent()
    data class RegisterUsernameChanged(val value: String): RegisterEvent()
    data class RegisterPasswordChanged(val value: String): RegisterEvent()
    object Register: RegisterEvent()
    object AlreadyHaveAnAccount: RegisterEvent()
}