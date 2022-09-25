package com.bura.chat.screens.screen.addcontact

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.data.UserPreferences
import com.bura.chat.data.room.contacts.Contact
import com.bura.chat.net.RestClient
import com.bura.chat.net.requests.SearchUser
import com.bura.chat.repository.ContactRepository
import com.bura.chat.repository.UserPrefsRepository
import com.bura.chat.screens.viewmodel.ui.SearchedUser
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.screens.viewmodel.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddContactViewModel(
    private val userPrefsRepository: UserPrefsRepository,
    private val contactRepository: ContactRepository
): ViewModel() {
    var state by mutableStateOf(AddContactState())
    val uiResponse = MutableStateFlow<UiResponse>(UiResponse.Null)


    private fun searchUser() {
        viewModelScope.launch {
            val response = try {
                val restClient = RestClient()
                restClient.api.searchUser(SearchUser(state.addNewContact))
            } catch (e: Exception) {
                uiResponse.emit(UiResponse.ConnectionFail)
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.message == "User found") {
                    uiResponse.emit(UiResponse.SearchUser(SearchedUser(response.body()!!.username, response.body()!!.email)))
                    println("Username= ${response.body()!!.username} email= ${response.body()!!.email}")
                }

                if (response.body()!!.message == "User not found") {
                    uiResponse.emit(UiResponse.UserNotFound)
                }
            }
        }
    }

    private suspend fun addContact(searchedUser: SearchedUser) {
        with (contactRepository) {
            val contact = this.getContactByName(userPrefsRepository.getStringPref(UserPreferences.Prefs.username), searchedUser.username)

            if (searchedUser.username == userPrefsRepository.getStringPref(UserPreferences.Prefs.username)) {
                uiResponse.emit(UiResponse.CannotAddYourself)
                return
            }

            if (contact != null) {
                uiResponse.emit(UiResponse.ContactAlreadyAdded)
                return
            }

            this.insert(Contact(0, userPrefsRepository.getStringPref(UserPreferences.Prefs.username), searchedUser.username, searchedUser.email))
            uiResponse.emit(UiResponse.ContactSuccessfullyAdded)
        }
    }

    fun onEvent(event: AddContactEvent) {
        when (event) {
            is AddContactEvent.AddContactChanged -> {
                state = state.copy(addNewContact = event.value)
            }

            AddContactEvent.SearchUser -> {
                searchUser()
            }

            is AddContactEvent.AddUserContact -> {
                viewModelScope.launch {
                    addContact(event.user)
                }
            }
        }
    }
}

