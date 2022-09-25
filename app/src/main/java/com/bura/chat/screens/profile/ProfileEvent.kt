package com.bura.chat.screens.profile

sealed class ProfileEvent {
    object Logout: ProfileEvent()
}