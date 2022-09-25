package com.bura.chat.screens.settings

sealed class SettingsEvent {
    data class CurrentPasswordChanged(val value: String): SettingsEvent()
    data class NewPasswordChanged(val value: String): SettingsEvent()
    object ChangePassword: SettingsEvent()
}