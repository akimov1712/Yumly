package ru.topbun.data.repository.session

import ru.topbun.data.source.local.config.TokenManager
import ru.topbun.domain.repository.session.SessionRepository

internal class SessionRepositoryImpl(
    private val tokenManager: TokenManager
) : SessionRepository {

    override fun hasSession(): Boolean = tokenManager.hasToken()

}
