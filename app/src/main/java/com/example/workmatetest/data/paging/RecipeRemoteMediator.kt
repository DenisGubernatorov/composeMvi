package com.example.workmatetest.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.workmatetest.data.local.db.UserDatabase
import com.example.workmatetest.data.local.entity.RecipeEntity
import com.example.workmatetest.data.remote.SpoonacularApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RecipeRemoteMediator(
    private val apiService: SpoonacularApiService,
    private val database: UserDatabase
) : RemoteMediator<Int, RecipeEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, RecipeEntity>): MediatorResult {
        try {
            if (loadType == LoadType.PREPEND) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            val number = state.config.pageSize
            val response = apiService.getRandomRecipes(number = number)
            if (response.isSuccessful) {
                val newRecipes = response.body()?.recipes ?: emptyList()
                val existingIds = database.userDao().getAllRecipeIds().toSet()
                val uniqueRecipes = newRecipes.filter { !existingIds.contains(it.id) }.map { apiRecipe ->
                    RecipeEntity(
                        id = apiRecipe.id,
                        title = apiRecipe.title,
                        image = apiRecipe.image,
                        summary = apiRecipe.summary
                    )
                }

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.userDao().clearRecipes()
                    }
                    if (uniqueRecipes.isNotEmpty()) {
                        uniqueRecipes.forEach { recipeEntity ->
                            database.userDao().insertRecipe(recipeEntity)
                        }
                    }
                }

                val endOfPagination = uniqueRecipes.size < number / 2
                return MediatorResult.Success(endOfPaginationReached = endOfPagination)
            } else {
                return MediatorResult.Error(Exception(response.message()))
            }
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}