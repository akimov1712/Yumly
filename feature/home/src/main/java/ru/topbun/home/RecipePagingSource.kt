package ru.topbun.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.topbun.core.common.Result
import ru.topbun.core.common.error.DataError
import ru.topbun.domain.entity.recipe.RecipeEntity
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeEntity
import ru.topbun.domain.entity.recipe.getRecipe.GetRecipeFilterEntity
import ru.topbun.domain.useCases.recipe.GetRecipeUseCase

internal class RecipePagingSource(
    private val search: String,
    private val recipeFilter: GetRecipeFilterEntity,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val onError: suspend (DataError) -> Unit,
) : PagingSource<Int, RecipeEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeEntity> {
        val offset = params.key ?: 0
        val limit = params.loadSize.coerceAtMost(PAGE_SIZE)
        val result = getRecipeUseCase(
            GetRecipeEntity(
                q = search,
                offset = offset,
                limit = limit,
                recipeFilter = recipeFilter
            )
        )

        return when (result) {
            is Result.Success -> {
                val recipes = result.data
                LoadResult.Page(
                    data = recipes,
                    prevKey = if (offset == 0) null else (offset - limit).coerceAtLeast(0),
                    nextKey = if (recipes.isEmpty()) null else offset + recipes.size
                )
            }

            is Result.Error -> {
                onError(result.error)
                LoadResult.Error(RuntimeException(result.error.toString()))
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecipeEntity>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(PAGE_SIZE) ?: page.nextKey?.minus(PAGE_SIZE)
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
