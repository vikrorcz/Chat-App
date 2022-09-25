package com.bura.chat.screens.screen.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.data.UserPreferences
import com.bura.chat.net.RestClient
import com.bura.chat.net.requests.UpdateUserPassword
import com.bura.chat.repository.UserPrefsRepository
import com.bura.chat.screens.viewmodel.ui.UiEvent
import com.bura.chat.screens.viewmodel.ui.UiResponse
import com.bura.chat.screens.viewmodel.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPrefsRepository: UserPrefsRepository,
): ViewModel() {

    var state by mutableStateOf(SettingsState())
    val uiResponse = MutableStateFlow<UiResponse>(UiResponse.Null)

    private fun changePassword() {
        viewModelScope.launch {
            val response = try {
                val restClient = RestClient()
                restClient.api.updatePassword(
                    UpdateUserPassword(userPrefsRepository.getStringPref(UserPreferences.Prefs.username),
                        state.settingsCurrentPassword,
                        state.settingsNewPassword)
                )

            } catch (e: Exception) {
                uiResponse.emit(UiResponse.ConnectionFail)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.message == "Current password is invalid") {
                    uiResponse.emit(UiResponse.ChangePasswordFail)
                    return@launch
                }
                uiResponse.emit(UiResponse.ChangePasswordSuccess)
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.ChangePassword -> {
                changePassword()
            }
            is SettingsEvent.CurrentPasswordChanged -> {
                state = state.copy(settingsCurrentPassword = event.value)
            }
            is SettingsEvent.NewPasswordChanged -> {
                state = state.copy(settingsNewPassword = event.value)
            }
        }
    }
}