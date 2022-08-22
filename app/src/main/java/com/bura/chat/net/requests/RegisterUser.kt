package com.bura.chat.net.requests

data class RegisterUser (
    val email: String,
    val username: String,
    val password: String
)