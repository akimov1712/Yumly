package ru.topbun.domain.useCases.follow

import ru.topbun.domain.repository.follow.FollowRepository

class GetFollowingUseCase(
    private val repository: FollowRepository
) {

    suspend operator fun invoke(userId: Int, limit: Int = 20, offset: Int = 0) =
        repository.getFollowing(userId, limit, offset)

}