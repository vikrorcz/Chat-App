package com.bura.chat.screens.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bura.chat.screens.data.RestClient
import com.bura.chat.screens.data.LoginUser
import com.bura.chat.screens.data.RegisterUser
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


//import okhttp3.Response

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

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    //fun setMessage(message: String) {
    //    _message.value = message
    //}

    fun registerAccount() {
        viewModelScope.launch {
            val response = try {
                RestClient.api.registerUser(RegisterUser(email.value, username.value, password.value))
            } catch (e: IOException) {
                return@launch
            } catch (e: HttpException) {
                return@launch
            } catch (e: JsonSyntaxException) {
                Log.e("JsonSyntaxException", e.message.toString())
                return@launch
            }

             if (response.isSuccessful && response.body() != null) {
                 Log.e("Response", response.body().toString())

                 _message.emit("Registration successful")
             }

              //Log.e("Call:", call.toString())

              //if (response.body() != null) {
              //    Log.e("Response", response.message())
              //}
        }
    }
}