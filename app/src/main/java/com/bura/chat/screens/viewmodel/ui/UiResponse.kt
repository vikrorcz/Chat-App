package com.bura.chat.screens.viewmodel.ui

import com.bura.chat.data.room.Contact

sealed class UiResponse {
    //responses from vm to ui

    //Add Contact Screen
    data class SearchUser(val user: SearchedUser): UiResponse()
    object UserNotFound: UiResponse()
    object ContactAlreadyAdded: UiResponse()

    //Chat Screen
    object NavigateContactScreen: UiResponse()

    //Contacts Screen
    object NavigateAddContactScreen: UiResponse()

    //Login Screen
    object UsernameError: UiResponse()
    object ConnectionFail: UiResponse()
    object TokenExpired: UiResponse()
    object LoginSuccess: UiResponse()
    object InvalidCredentials: UiResponse()

    //Profile Screen
    object NavigateLoginScreen: UiResponse()

    //Registration Screen
    object PasswordError: UiResponse()
    object EmailError: UiResponse()
    object RegistrationSuccess: UiResponse()

    //Settings Screen
    object ChangePasswordSuccess: UiResponse()
    object ChangePasswordFail: UiResponse()

    //for initialization
    object Null: UiResponse()
}