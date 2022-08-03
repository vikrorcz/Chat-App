package com.bura.chat.net


data class UpdateUserPassword (
    val username: String,
    val oldPassword: String,
    val newPassword: String
)