package com.bura.chat.screens.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ServerApi {

    //@GET
    //suspend fun getMessage(): okhttp3.Response
    @GET("/")
    suspend fun getUser(): Response<LoginUser>
   // @GET
   // suspend fun getUser(@Url username: String): Response<User>


    //we could also use a call but this is replaced by the suspend function
    @POST("register")
    suspend fun registerUser(@Body registerUser: RegisterUser): Response<LoginRegisterResponse> //Call<User>

    //@POST("register")
    //suspend fun registerResponse(@Body loginRegisterResponse: LoginRegisterResponse): Response<LoginRegisterResponse> //Call<User>

    @POST("login")
    suspend fun loginUser(@Body loginUser: LoginUser): Response<LoginRegisterResponse>


}