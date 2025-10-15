package ru.topbun.data.source.remote.dto.recipe

import ru.topbun.domain.entity.recipe.StepEntity

data class StepDto (
    val id: Int,
    val description: String,
    val previewUrl: String?
){

    fun toEntity() = StepEntity(
        id = id,
        description = description,
        previewUrl = previewUrl
    )

}

internal fun StepEntity.toDto() = StepDto(id, description, previewUrl)