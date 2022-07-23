package com.bura.chat.screens.data

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {
    private const val BASE_URL: String = "http://192.168.254.39:8080/"

    //192.168.254.39
    //http://0.0.0.0:8080/

    //const val REGISTER_URL: String = "${BASE_URL}register"

   private val gson = GsonBuilder()
       .setLenient()
       .create()

    val api: ServerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ServerApi::class.java)
    }
}