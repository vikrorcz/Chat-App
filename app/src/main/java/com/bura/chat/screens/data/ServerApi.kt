package com.bura.chat.screens.data

import okhttp3.Request
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST


interface ServerApi {

    //@GET
    //suspend fun getMessage(): okhttp3.Response
    @GET("/")
    suspend fun getUser(): Response<User>
   // @GET
   // suspend fun getUser(@Url username: String): Response<User>

    @POST("register")
    fun registerUser(@Body user: User): Call<User>
}