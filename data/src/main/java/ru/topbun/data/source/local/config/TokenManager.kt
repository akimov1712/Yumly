package ru.topbun.data.source.local.config

import android.content.SharedPreferences
import ru.topbun.data.UnauthorizedException
import androidx.core.content.edit

class TokenManager(private val config: SharedPreferences){
    fun saveToken(token: String) = config.edit { putString(Config.Properties.TOKEN, token) }
    fun getToken() = config.getString(Config.Properties.TOKEN, null) ?: throw UnauthorizedException()
    fun clearToken() = config.edit { remove(Config.Properties.TOKEN) }
}