package com.bura.chat.screens.contacts

sealed class ContactsEvent {
    object AddContact: ContactsEvent()
    data class DeleteUserContact(val name: String): ContactsEvent()
}