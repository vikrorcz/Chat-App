package com.bura.chat.screens.screen.addcontact

import com.bura.chat.screens.viewmodel.ui.SearchedUser

sealed class AddContactEvent {
    data class AddContactChanged(val value: String): AddContactEvent()
    object SearchUser: AddContactEvent()
    data class AddUserContact(val user: SearchedUser): AddContactEvent()
}