package ru.topbun.data.source.remote.api.recipe

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.topbun.data.source.remote.dto.recipe.getRecipe.GetRecipeResponse
import ru.topbun.data.source.remote.dto.recipe.getRecipe.GetRecipeRequest

interface RecipeApi {

    @POST("/v1/recipe")
    suspend fun getRecipes(@Body body: GetRecipeRequest): Response<GetRecipeResponse>

}