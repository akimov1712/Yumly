package ru.topbun.data.source.local.config

import android.content.SharedPreferences

class TokenManager(private val config: SharedPreferences){
    fun saveToken(token: String) = config.edit().putString(Config.Properties.TOKEN, token).apply()
    fun getToken() = config.getString(Config.Properties.TOKEN, null)
    fun clearToken() = config.edit().remove(Config.Properties.TOKEN).apply()
}