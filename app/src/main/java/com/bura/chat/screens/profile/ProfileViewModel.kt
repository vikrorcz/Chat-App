package com.bura.chat.screens.screen.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.data.UserPreferences
import com.bura.chat.repository.UserPrefsRepository
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.screens.viewmodel.ui.UiState
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