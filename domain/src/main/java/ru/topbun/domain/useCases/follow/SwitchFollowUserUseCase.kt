package ru.topbun.domain.useCases.follow

import ru.topbun.domain.repository.follow.FollowRepository

class SwitchFollowUserUseCase(
    private val repository: FollowRepository
) {

    suspend operator fun invoke(userId: Int) = repository.switchFollowUser(userId)

}