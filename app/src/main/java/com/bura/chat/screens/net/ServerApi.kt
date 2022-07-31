package com.bura.chat.screens.net

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ServerApi {

    //we could also use a call but this is replaced by the suspend function
    @POST("register")
    suspend fun registerUser(@Body registerUser: RegisterUser): Response<LoginRegisterResponse> //Call<User>

    @POST("login")
    suspend fun loginUser(@Body loginUser: LoginUser): Response<LoginRegisterResponse>

    @POST("update")
    suspend fun updatePassword(@Body updateUserPassword: UpdateUserPassword): Response<LoginRegisterResponse>

}