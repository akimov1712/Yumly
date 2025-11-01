package ru.topbun.domain.repository.config

interface ConfigRepository {
    
    suspend fun getStatusFirstStart(): Boolean
    suspend fun setStatusFirstStart(status: Boolean)

}