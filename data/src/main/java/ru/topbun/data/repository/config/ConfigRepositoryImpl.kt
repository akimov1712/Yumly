package ru.topbun.data.repository.config

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import ru.topbun.data.source.local.config.Config
import ru.topbun.data.source.local.config.DataStoreManager
import ru.topbun.data.source.local.config.DefaultConfig
import ru.topbun.domain.repository.config.ConfigRepository

class ConfigRepositoryImpl(
    private val config: DataStoreManager
): ConfigRepository {

    override suspend fun getStatusFirstStart(): Boolean {
        return config.getBoolean(Config.Properties.IS_FIRST_START, true).firstOrNull() ?: true
    }

    override suspend fun setStatusFirstStart(status: Boolean) {
        config.setBoolean(Config.Properties.IS_FIRST_START, status)
    }
}