package com.bura.chat.screens.screen.profile

import com.bura.chat.screens.viewmodel.ui.UiEvent

sealed class ProfileEvent {
    object Logout: ProfileEvent()
}