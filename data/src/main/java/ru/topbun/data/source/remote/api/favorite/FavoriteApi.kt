package ru.topbun.data.source.remote.api.favorite

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteApi {

    @POST("/v1/favorite/{id}")
    fun switchFavorite(@Path("id") recipeId: Int): Response<Boolean>

}