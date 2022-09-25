package com.bura.chat.screens.recentchat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.util.UiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RecentChatViewModel: ViewModel() {

    //var state by mutableStateOf(Recent())
    val uiResponse = MutableStateFlow<UiResponse>(UiResponse.Null)

    fun onEvent(event: RecentChatEvent) {
        when (event) {
            RecentChatEvent.Contacts -> {
                viewModelScope.launch {
                    uiResponse.emit(UiResponse.NavigateContactScreen)
                }
            }
        }
    }
}