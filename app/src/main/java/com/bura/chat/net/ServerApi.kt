package com.bura.chat.net

import com.bura.chat.net.requests.LoginUser
import com.bura.chat.net.requests.RegisterUser
import com.bura.chat.net.requests.SearchUser
import com.bura.chat.net.requests.UpdateUserPassword
import com.bura.chat.net.responses.AutoLoginResponse
import com.bura.chat.net.responses.LoginRegisterResponse
import com.bura.chat.net.responses.SearchUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface ServerApi {

    //we could also use a call but this is replaced by the suspend function
    @POST("register")
    suspend fun registerUser(@Body registerUser: RegisterUser): Response<LoginRegisterResponse> //Call<User>

    @POST("login")
    suspend fun loginUser(@Body loginUser: LoginUser): Response<LoginRegisterResponse>

    @POST("update")
    suspend fun updatePassword(@Body updateUserPassword: UpdateUserPassword): Response<LoginRegisterResponse>

    @POST("search-user")
    suspend fun searchUser(@Body searchUser: SearchUser): Response<SearchUserResponse>

    @GET("auto-login")
    suspend fun autoLoginUser(@Header("Authorization") token: String): Response<AutoLoginResponse>
}