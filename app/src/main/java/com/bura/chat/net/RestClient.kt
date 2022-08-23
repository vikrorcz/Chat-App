package com.bura.chat.net

import com.bura.chat.util.TokenInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient(token: String) {
    private val BASE_URL: String = "http://192.168.254.39:8080/"

   private val gson = GsonBuilder()
       .setLenient()
       .create()

    private val tokenInterceptor: TokenInterceptor = TokenInterceptor(token)

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .build()

    val api: ServerApi by lazy {
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ServerApi::class.java)
    }
}


