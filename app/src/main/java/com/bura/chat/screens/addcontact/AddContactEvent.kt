package com.bura.chat.screens.addcontact

import com.bura.chat.data.SearchedUser

sealed class AddContactEvent {
    data class AddContactChanged(val value: String): AddContactEvent()
    object SearchUser: AddContactEvent()
    data class AddUserContact(val user: SearchedUser): AddContactEvent()
}