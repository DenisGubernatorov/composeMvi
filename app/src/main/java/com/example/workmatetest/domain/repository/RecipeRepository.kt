package com.example.workmatetest.domain.repository

import com.example.workmatetest.domain.model.Result

interface RecipeRepository {
    suspend fun syncRandomRecipes(): Result<Unit>
}