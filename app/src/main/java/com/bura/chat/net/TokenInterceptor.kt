package com.bura.chat.net

import com.bura.chat.data.UserPreferences
import com.bura.chat.repository.UserPrefsRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TokenInterceptor(
    private val userPrefsRepository: UserPrefsRepository
) : Interceptor {//class TokenInterceptor(private val token: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest: Request = chain.request().newBuilder()
            .header("Authorization", "Bearer ${userPrefsRepository.getStringPref(UserPreferences.Prefs.token)}")
            .build()
        return chain.proceed(newRequest)
    }
}