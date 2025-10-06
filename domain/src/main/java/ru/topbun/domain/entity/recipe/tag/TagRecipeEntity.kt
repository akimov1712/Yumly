package ru.topbun.domain.entity.recipe.tag

data class TagRecipeEntity(
    val id: Int,
    val type: TagType,
    val name: String,
    val icon: String
)
