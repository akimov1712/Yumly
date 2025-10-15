package ru.topbun.data.source.remote.dto.account

import ru.topbun.domain.entity.account.UpdateAccountInfoEntity

data class UpdateAccountInfoRequest(
    val username: String,
    val photoUrl: String?
)

fun UpdateAccountInfoEntity.toRequest() = UpdateAccountInfoRequest(username, photoUrl)