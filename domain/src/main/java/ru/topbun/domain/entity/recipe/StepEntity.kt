package ru.topbun.domain.entity.recipe

data class StepEntity (
    val id: Int = 0,
    val description: String,
    val previewUrl: String?
)
