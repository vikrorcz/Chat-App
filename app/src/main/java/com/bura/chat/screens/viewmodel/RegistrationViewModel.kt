package com.bura.chat.screens.viewmodel

import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegistrationViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    fun setEmail(email: String) {
        _email.value = email
    }

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    fun setUsername(username: String) {
        _username.value = username
    }

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun setPassword(password: String) {
        _password.value = password
    }

    fun registerAccount() {

    }
}