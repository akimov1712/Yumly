package ru.topbun.data.repository.follow

import android.content.Context
import ru.topbun.common.HttpStatusCode
import ru.topbun.common.Result
import ru.topbun.common.error.DataError
import ru.topbun.data.exceptionWrapper
import ru.topbun.data.source.remote.api.follow.FollowApi
import ru.topbun.domain.entity.account.ProfileEntity
import ru.topbun.domain.repository.follow.FollowRepository

class FollowRepositoryImpl(
    private val context: Context,
    private val api: FollowApi
): FollowRepository {

    override suspend fun switchFollowUser(userId: Int): Result<Boolean, DataError> =
        context.exceptionWrapper {
            val response = api.switchFollowStatus(userId)
            val result = response.body()
            if (response.isSuccessful && result != null){
                Result.Success(result)
            } else {
                val error = when(response.code()){
                    HttpStatusCode.BAD_REQUEST -> DataError.Network.INVALID_DATA
                    HttpStatusCode.NOT_FOUND -> DataError.Network.NOT_FOUND
                    else -> DataError.Network.SERVER_ERROR
                }
                Result.Error(error)
            }
        }

    override suspend fun getFollowers(
        userId: Int,
        limit: Int,
        offset: Int
    ): Result<List<ProfileEntity>, DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowing(
        userId: Int,
        limit: Int,
        offset: Int
    ): Result<List<ProfileEntity>, DataError> {
        TODO("Not yet implemented")
    }

}