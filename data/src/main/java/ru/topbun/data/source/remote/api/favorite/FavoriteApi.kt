package ru.topbun.data.source.remote.api.favorite

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.topbun.data.source.remote.dto.favorite.GetFavoriteRequest
import ru.topbun.data.source.remote.dto.recipe.getRecipe.GetRecipeResponse

interface FavoriteApi {

    @POST("/v1/favorite/{id}")
    fun switchFavorite(@Path("id") recipeId: Int): Response<Boolean>


    @GET("/v1/favorite/{id}")
    fun getFavoriteRecipes(@Path("id") userId: Int, @Body body: GetFavoriteRequest): Response<GetRecipeResponse>

}