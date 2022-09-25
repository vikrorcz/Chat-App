package com.bura.chat.screens.screen.login

data class LoginState(
    val loginUsername: String = "",
    val loginPassword: String = "",
    val rememberMe: Boolean = false,
)