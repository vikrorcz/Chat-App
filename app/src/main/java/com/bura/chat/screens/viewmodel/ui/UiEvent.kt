package com.bura.chat.screens.viewmodel.ui

sealed class UiEvent {

    //Login Screen
    data class LoginUsernameChanged(val value: String): UiEvent()
    data class loginPasswordChanged(val value: String): UiEvent()
    data class RememberMeChanged(val value: Boolean): UiEvent()
    object login: UiEvent()

    //Register Screen
    data class RegisterEmailChanged(val value: String): UiEvent()
    data class registerUsernameChanged(val value: String): UiEvent()
    data class registerPasswordChanged(val value: String): UiEvent()
    object register: UiEvent()
}