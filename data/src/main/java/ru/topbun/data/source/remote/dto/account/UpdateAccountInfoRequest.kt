package ru.topbun.data.source.remote.dto.account

import ru.topbun.domain.entity.account.UpdateAccountInfoEntity

internal data class UpdateAccountInfoRequest(
    val username: String,
    val photoUrl: String?
)

internal fun UpdateAccountInfoEntity.toRequest() = UpdateAccountInfoRequest(username, photoUrl)