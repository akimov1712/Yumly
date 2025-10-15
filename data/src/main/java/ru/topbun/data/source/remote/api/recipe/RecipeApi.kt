package ru.topbun.data.source.remote.api.recipe

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.topbun.data.source.remote.dto.recipe.RecipeDto
import ru.topbun.data.source.remote.dto.recipe.addRecipe.AddRecipeRequest
import ru.topbun.data.source.remote.dto.recipe.getRecipe.GetRecipeResponse
import ru.topbun.data.source.remote.dto.recipe.getRecipe.GetRecipeRequest

interface RecipeApi {

    @POST("/v1/recipe")
    suspend fun getRecipes(@Body body: GetRecipeRequest): Response<GetRecipeResponse>

    @GET("/v1/recipe/{id}")
    suspend fun getRecipeById(@Path("id") recipeId: Int): Response<RecipeDto>

    @POST("/v1/recipe/add")
    suspend fun addRecipe(@Body body: AddRecipeRequest): Response<RecipeDto>

    @DELETE("/v1/recipe/{id}")
    suspend fun deleteRecipe(@Path("id") recipeId: Int): okhttp3.Response

}