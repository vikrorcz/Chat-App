package com.bura.chat.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.screens.net.LoginUser
import com.bura.chat.screens.net.RestClient
import com.bura.chat.screens.net.UpdateUserPassword
import com.bura.chat.screens.util.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _currentPassword = MutableStateFlow("")
    val currentPassword = _currentPassword.asStateFlow()

    fun setCurrentPassword(currentPassword: String) {
        _currentPassword.value = currentPassword
    }

    private val _newPassword = MutableStateFlow("")
    val newPassword = _newPassword.asStateFlow()

    fun setNewPassword(newPassword: String) {
        _newPassword.value = newPassword
    }

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    fun changePassword() {
        viewModelScope.launch {
            val response = try {
                RestClient.api.updatePassword(UpdateUserPassword("viktor", currentPassword.value, newPassword.value))
            } catch (e: Exception) {
                _message.emit("Connection to server failed")
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                _message.emit(response.body()!!.message)
            }
        }
    }

}

