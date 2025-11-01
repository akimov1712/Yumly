package ru.topbun.data.source.local.config

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val config: DefaultConfig) {

    suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        config.edit {
            it[key] = value
        }
    }

    fun <T> read(key: Preferences.Key<T>, defaultValue: T): Flow<T> = config.data
        .map { settings ->
            settings[key] ?: defaultValue
        }


    suspend fun setBoolean(key: String, value: Boolean) = save(booleanPreferencesKey(key), value)
    fun getBoolean(key: String, defaultValue: Boolean = false): Flow<Boolean> =
        read(booleanPreferencesKey(key), defaultValue)

    suspend fun setInteger(key: String, value: Int) = save(intPreferencesKey(key), value)
    fun getInteger(key: String, defaultValue: Int = 0): Flow<Int> =
        read(intPreferencesKey(key), defaultValue)

    suspend fun setFloat(key: String, value: Float) = save(floatPreferencesKey(key), value)
    fun getFloat(key: String, defaultValue: Float = 0f): Flow<Float> =
        read(floatPreferencesKey(key), defaultValue)

    suspend fun setString(key: String, value: String) = save(stringPreferencesKey(key), value)
    fun getString(key: String, defaultValue: String = ""): Flow<String> =
        read(stringPreferencesKey(key), defaultValue)

}