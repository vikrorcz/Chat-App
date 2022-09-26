package com.bura.chat.repository

import com.bura.chat.net.ServerApi
import com.bura.chat.net.requests.LoginUser
import com.bura.chat.net.requests.RegisterUser
import com.bura.chat.net.requests.SearchUser
import com.bura.chat.net.requests.UpdateUserPassword
import com.bura.chat.net.responses.AutoLoginResponse
import com.bura.chat.net.responses.LoginRegisterResponse
import com.bura.chat.net.responses.SearchUserResponse
import com.bura.chat.screens.login.LoginEvent
import retrofit2.Response

class ServerRepository(private val serverApi: ServerApi): ServerApi {

    override suspend fun registerUser(registerUser: RegisterUser): Response<LoginRegisterResponse> {
        return serverApi.registerUser(RegisterUser(registerUser.email, registerUser.username, registerUser.password))
    }

    override suspend fun loginUser(loginUser: LoginUser): Response<LoginRegisterResponse> {
        return serverApi.loginUser(LoginUser(loginUser.username, loginUser.password))
    }

    override suspend fun updatePassword(updateUserPassword: UpdateUserPassword): Response<LoginRegisterResponse> {
        return serverApi.updatePassword(UpdateUserPassword(updateUserPassword.username, updateUserPassword.oldPassword, updateUserPassword.newPassword))
    }

    override suspend fun searchUser(searchUser: SearchUser): Response<SearchUserResponse> {
        return serverApi.searchUser(SearchUser(searchUser.user))
    }

    override suspend fun autoLoginUser(token: String): Response<AutoLoginResponse> {
        return serverApi.autoLoginUser(token)
    }
}