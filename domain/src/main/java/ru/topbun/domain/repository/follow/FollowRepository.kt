package ru.topbun.domain.repository.follow

import ru.topbun.common.DataError
import ru.topbun.common.Result

interface FollowRepository {

    suspend fun switchFollowUser(userId: Int): Result<Boolean, DataError>

}