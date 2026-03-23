package ru.topbun.data.source.local.config

import android.content.SharedPreferences
import ru.topbun.data.UnauthorizedException
import androidx.core.content.edit

internal class TokenManager(private val config: CryptConfig){
    fun saveToken(token: String) = config.edit { putString(Config.Properties.TOKEN, token) }
    fun getToken() = getTokenOrNull() ?: throw UnauthorizedException()
    fun getTokenOrNull(): String? = config.getString(Config.Properties.TOKEN, null)
    fun hasToken(): Boolean = !getTokenOrNull().isNullOrBlank()
    fun clearToken() = config.edit { remove(Config.Properties.TOKEN) }
}
