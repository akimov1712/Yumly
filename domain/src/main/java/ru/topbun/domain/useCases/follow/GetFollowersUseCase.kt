package ru.topbun.domain.useCases.follow

import ru.topbun.domain.repository.follow.FollowRepository

class GetFollowersUseCase(
    private val repository: FollowRepository
) {

    suspend operator fun invoke(userId: Int, limit: Int = 20, offset: Int = 0) =
        repository.getFollowers(userId, limit, offset)

}