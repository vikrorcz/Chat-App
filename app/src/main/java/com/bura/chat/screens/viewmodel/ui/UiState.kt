package com.bura.chat.screens.viewmodel.ui

data class UiState(
    val isLoading: Boolean = false,
    val loginUsername: String = "",
    val loginPassword: String = "",
    val registerUsername: String = "",
    val registerEmail: String = "",
    val registerPassword: String = "",
    val loginSuccessful: Boolean = false,
    val rememberMe: Boolean = false,
    //val uiResponse: UiResponse = UiResponse.NULL
    //val uiState: UiState
)