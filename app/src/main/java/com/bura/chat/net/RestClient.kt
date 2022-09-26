package com.bura.chat.net

import com.bura.chat.repository.UserPrefsRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface RestClient {

    companion object {
        @Volatile
        private var INSTANCE: ServerApi? = null

        fun getInstance(userPrefsRepository: UserPrefsRepository): ServerApi? {

            val BASE_URL = "http://192.168.254.38:8080/"

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val tokenInterceptor = TokenInterceptor(userPrefsRepository)

            val httpClient: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .build()

            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance =  Retrofit.Builder()
                        .client(httpClient)
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
                        .create(ServerApi::class.java)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}


