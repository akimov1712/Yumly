package ru.topbun.data.source.local.config

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

typealias CryptConfig = SharedPreferences
typealias DefaultConfig = DataStore<Preferences>

internal object Config {

    private const val CRYPT_CONFIG_NAME = "secure_config"
    private const val DEFAULT_CONFIG_NAME = "default_config"

    internal fun createCryptConfig(context: Context): CryptConfig {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            CRYPT_CONFIG_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    val Context.dataStore: DefaultConfig by preferencesDataStore(DEFAULT_CONFIG_NAME)

    object Properties {

        const val TOKEN = "token"

        const val IS_FIRST_START = "is_first_start"

    }

}