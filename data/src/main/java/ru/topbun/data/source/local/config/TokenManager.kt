package ru.topbun.data.source.local.config

import android.content.SharedPreferences

fun SharedPreferences.saveToken(token: String) = edit().putString(Config.Properties.TOKEN, token).apply()
fun SharedPreferences.getToken() = getString(Config.Properties.TOKEN, null)
fun SharedPreferences.clearToken() = edit().remove(Config.Properties.TOKEN).apply()