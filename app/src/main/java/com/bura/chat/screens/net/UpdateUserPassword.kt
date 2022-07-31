package com.bura.chat.screens.net


data class UpdateUserPassword (
    val username: String,
    val oldPassword: String,
    val newPassword: String
)