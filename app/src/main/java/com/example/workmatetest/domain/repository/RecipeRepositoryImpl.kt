package com.example.workmatetest.domain.repository

import com.example.workmatetest.data.local.dao.UserDao
import com.example.workmatetest.data.local.entity.RecipeEntity
import com.example.workmatetest.data.remote.SpoonacularApiService
import com.example.workmatetest.domain.model.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val apiService: SpoonacularApiService,
    private val userDao: UserDao
) : RecipeRepository {
    override suspend fun syncRandomRecipes(): Result<Unit> {
        return try {
                val response = apiService.getRandomRecipes()
            if (response.isSuccessful) {
                response.body()?.recipes?.forEach { recipe ->
                    val entity = RecipeEntity(
                        id = recipe.id,
                        title = recipe.title,
                        image = recipe.image,
                        summary = recipe.summary
                    )
                    userDao.insertRecipe(entity)
                }
                Result.Success(Unit)
            } else {
                Result.Error("Ошибка API: ${response.code()} - ${response.message()}\nДанные буду сихронизированы при следующем запуске")
            }
        } catch (e: Exception) {
            Result.Error("Ошибка сети: ${e.message}")
        }
    }
}