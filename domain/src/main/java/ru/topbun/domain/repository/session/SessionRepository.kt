package ru.topbun.domain.repository.session

interface SessionRepository {

    fun hasSession(): Boolean

    suspend fun clearSession()

}
