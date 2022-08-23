package com.bura.chat.util

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class TokenInterceptor(private val token: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        //rewrite the request to add bearer token
        val newRequest: Request = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")//.header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}