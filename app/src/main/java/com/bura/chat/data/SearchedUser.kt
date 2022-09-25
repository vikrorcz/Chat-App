package com.bura.chat.data

data class SearchedUser(val username: String, val email: String) {
    constructor(): this("", "")
}