package com.bura.chat.net.requests


data class UpdateUserPassword (
    val username: String,
    val oldPassword: String,
    val newPassword: String
)