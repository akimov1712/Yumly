package ru.topbun.domain.repository.follow

import ru.topbun.core.common.error.DataError
import ru.topbun.core.common.Result
import ru.topbun.domain.entity.account.ProfileEntity

interface FollowRepository {

    suspend fun switchFollowUser(userId: Int): Result<Boolean, DataError>
    suspend fun getFollowers(userId: Int, limit: Int, offset: Int): Result<List<ProfileEntity>, DataError>
    suspend fun getFollowing(userId: Int, limit: Int, offset: Int): Result<List<ProfileEntity>, DataError>

}