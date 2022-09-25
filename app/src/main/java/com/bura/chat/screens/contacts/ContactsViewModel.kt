package com.bura.chat.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.data.UserPreferences
import com.bura.chat.data.room.contacts.Contact
import com.bura.chat.repository.ContactRepository
import com.bura.chat.repository.UserPrefsRepository
import com.bura.chat.util.UiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val userPrefsRepository: UserPrefsRepository,
    private val contactRepository: ContactRepository
): ViewModel() {

    //var state by mutableStateOf(Contacts())
    val uiResponse = MutableStateFlow<UiResponse>(UiResponse.Null)

    suspend fun getContactList(): MutableList<Contact> {
        var list : MutableList<Contact>
        with (contactRepository) {
            list = this.getContactList(userPrefsRepository.getStringPref(UserPreferences.Prefs.username))
        }
        return list
    }

    private suspend fun deleteContact(name: String) {
        with (contactRepository) {
            val contact = this.getContactByName(userPrefsRepository.getStringPref(UserPreferences.Prefs.username), name)
            this.delete(contact!!)
            uiResponse.emit(UiResponse.DeleteUserFromList(contact = contact))
        }
    }

    fun onEvent(event: ContactsEvent) {
        when (event) {
            ContactsEvent.AddContact -> {
                viewModelScope.launch {
                    uiResponse.emit(UiResponse.NavigateAddContactScreen)
                }
            }

            is ContactsEvent.DeleteUserContact -> {
                viewModelScope.launch {
                    deleteContact(event.name)
                }
            }
        }
    }
}