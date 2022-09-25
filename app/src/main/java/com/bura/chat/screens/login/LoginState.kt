package com.bura.chat.screens.login

data class LoginState(
    val loginUsername: String = "",
    val loginPassword: String = "",
    val rememberMe: Boolean = false,
)