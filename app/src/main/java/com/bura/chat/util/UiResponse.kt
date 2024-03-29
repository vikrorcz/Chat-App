package com.bura.chat.util

import com.bura.chat.data.SearchedUser
import com.bura.chat.data.room.contacts.Contact
import com.bura.chat.data.room.messages.Message

sealed class UiResponse {
    //responses from vm to ui

    //Add Contact Screen
    data class SearchUser(val user: SearchedUser): UiResponse()
    object UserNotFound: UiResponse()
    object ContactAlreadyAdded: UiResponse()
    object ContactSuccessfullyAdded: UiResponse()
    object CannotAddYourself: UiResponse()

    //Recent Chat Screen
    object NavigateContactScreen: UiResponse()

    //Contacts Screen
    object NavigateAddContactScreen: UiResponse()
    data class DeleteUserFromList(val contact: Contact): UiResponse()

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

    //Chat Screen
    data class AddMessageToList(val message: Message): UiResponse()

    //for initialization
    object Null: UiResponse()
}