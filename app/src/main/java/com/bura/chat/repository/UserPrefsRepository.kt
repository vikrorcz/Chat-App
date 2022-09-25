package com.bura.chat.repository

import android.content.Context
import com.bura.chat.data.UserPreferences
import com.bura.chat.data.UserPreferences.Prefs

class UserPrefsRepository(context: Context): UserPreferences {
    private val sharedPref = context.getSharedPreferences("chat-prefs", Context.MODE_PRIVATE)

    override fun setPref(pref: Prefs, boolean: Boolean) {
        with (sharedPref.edit()) {
            putBoolean(pref.name, boolean)
            apply()
        }
    }

    override fun setPref(pref: Prefs, string: String) {
        with (sharedPref.edit()) {
            putString(pref.name, string)
            apply()
        }
    }

    override fun getBooleanPref(pref: Prefs): Boolean {
        return sharedPref.getBoolean(pref.name, false)
    }

    override fun getStringPref(pref: Prefs): String {
        return sharedPref.getString(pref.name, "")!!
    }
}