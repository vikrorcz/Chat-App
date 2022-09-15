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

    //Recent Chat Screen
    object Contacts: UiEvent()

    //Chat Screen
    data class MessageChanged(val value: String): UiEvent()
    data class SendMessage(val value: String): UiEvent()

    //Contacts Screen
    object AddContact: UiEvent()
    data class DeleteUserContact(val name: String): UiEvent()

    //Profile Screen
    object Logout: UiEvent()

    //Add Contact Screen
    data class AddContactChanged(val value: String): UiEvent()
    object SearchUser: UiEvent()
    data class AddUserContact(val user: SearchedUser): UiEvent()
}