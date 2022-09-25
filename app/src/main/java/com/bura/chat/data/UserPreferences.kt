package com.bura.chat.data

import android.content.Context

interface UserPreferences {//(private val context: Context) {

    enum class Prefs {
        rememberme,
        username,
        token
    }

    fun setPref(pref: Prefs,boolean: Boolean)

    fun setPref(pref: Prefs,string: String)

    fun getBooleanPref(pref: Prefs): Boolean

    fun getStringPref(pref: Prefs): String

    /*
    fun setPref(pref: Prefs,boolean: Boolean) {
        val sharedPref = context.getSharedPreferences("chat-prefs", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(pref.name, boolean)
            apply()
        }
    }

    fun setPref(pref: Prefs,string: String) {
        val sharedPref = context.getSharedPreferences("chat-prefs", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(pref.name, string)
            apply()
        }
    }

    fun getBooleanPref(pref: Prefs): Boolean {
        val sharedPref = context.getSharedPreferences("chat-prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean(pref.name, false)
    }

    fun getStringPref(pref: Prefs): String {
        val sharedPref = context.getSharedPreferences("chat-prefs", Context.MODE_PRIVATE)
        return sharedPref.getString(pref.name, "")!!
    }
    */
}