package ru.topbun.data.source.remote.dto.recipe

import ru.topbun.domain.entity.recipe.tag.TagRecipeEntity
import ru.topbun.domain.entity.recipe.tag.TagType

internal data class TagRecipeDto(
    val id: Int,
    val type: TagType,
    val name: String,
    val icon: String
){

    fun toEntity() = TagRecipeEntity(
        id = id,
        type = type,
        name = name,
        icon = icon
    )

}