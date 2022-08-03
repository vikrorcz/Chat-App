package com.bura.chat.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(private val context: Context) {

    //private val sharedPreferences: SharedPreferences = context.getSharedPreferences("chat-prefs", Context.MODE_PRIVATE)
    //lateinit var spEditor: SharedPreferences.Editor

    enum class Prefs {
        rememberme
    }

    fun setPref(pref: Prefs,boolean: Boolean) {
        val sharedPref = context.getSharedPreferences("chat-prefs", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(pref.name, boolean)
            apply()
        }
    }

    fun getPref(pref: Prefs): Boolean {
        val sharedPref = context.getSharedPreferences("chat-prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean(pref.name, false)
    }
}