package com.bura.chat.screens.viewmodel.ui

sealed class UiEvent {

    //Login Screen
    data class LoginUsernameChanged(val value: String): UiEvent()
    data class LoginPasswordChanged(val value: String): UiEvent()
    data class RememberMeChanged(val value: Boolean): UiEvent()
    object Login: UiEvent()

    //Register Screen
    data class RegisterEmailChanged(val value: String): UiEvent()
    data class RegisterUsernameChanged(val value: String): UiEvent()
    data class RegisterPasswordChanged(val value: String): UiEvent()
    object Register: UiEvent()
    object AlreadyHaveAnAccount: UiEvent()

    //Settings Screen
    data class CurrentPasswordChanged(val value: String): UiEvent()
    data class NewPasswordChanged(val value: String): UiEvent()
    object ChangePassword: UiEvent()
}