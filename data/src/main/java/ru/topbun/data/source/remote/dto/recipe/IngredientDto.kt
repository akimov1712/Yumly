package ru.topbun.data.source.remote.dto.recipe

import ru.topbun.domain.entity.recipe.IngredientEntity

internal data class IngredientDto(
    val id: Int,
    val name: String,
    val value: String,
){

    fun toEntity() = IngredientEntity(
        id = id,
        name = name,
        value = value
    )

}

internal fun IngredientEntity.toDto() = IngredientDto(id, name, value)
