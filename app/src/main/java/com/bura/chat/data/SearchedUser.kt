package com.bura.chat.screens.viewmodel.ui

data class SearchedUser(val username: String, val email: String) {
    constructor(): this("", "")
}