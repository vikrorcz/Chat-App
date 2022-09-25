package com.bura.chat.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.data.UserPreferences
import com.bura.chat.repository.UserPrefsRepository
import com.bura.chat.util.UiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPrefsRepository: UserPrefsRepository
): ViewModel() {

    //var state by mutableStateOf(UiState())
    val uiResponse = MutableStateFlow<UiResponse>(UiResponse.Null)

    private fun logout() {
        userPrefsRepository.setPref(UserPreferences.Prefs.rememberme, false)
        viewModelScope.launch {
            uiResponse.emit(UiResponse.NavigateLoginScreen)
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.Logout -> {
                logout()
            }
        }
    }
}