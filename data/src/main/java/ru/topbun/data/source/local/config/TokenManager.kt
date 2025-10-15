package ru.topbun.data.source.local.config

import android.content.SharedPreferences
import ru.topbun.data.UnauthorizedException

class TokenManager(private val config: SharedPreferences){
    fun saveToken(token: String) = config.edit().putString(Config.Properties.TOKEN, token).apply()
    fun getToken() = config.getString(Config.Properties.TOKEN, null) ?: throw UnauthorizedException()
    fun clearToken() = config.edit().remove(Config.Properties.TOKEN).apply()
}