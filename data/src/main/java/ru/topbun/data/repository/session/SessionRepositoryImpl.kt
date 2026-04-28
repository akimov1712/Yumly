package ru.topbun.data.repository.session

import ru.topbun.data.source.local.config.TokenManager
import ru.topbun.data.source.local.database.favorite.FavoriteRecipeDao
import ru.topbun.data.source.local.database.history.HistoryDao
import ru.topbun.domain.repository.session.SessionRepository

internal class SessionRepositoryImpl(
    private val tokenManager: TokenManager,
    private val favoriteDao: FavoriteRecipeDao,
    private val historyDao: HistoryDao
) : SessionRepository {

    override fun hasSession(): Boolean = tokenManager.hasToken()

    override suspend fun clearSession(){
        tokenManager.clearToken()
        favoriteDao.deleteAll()
        historyDao.deleteAll()
    }

}
