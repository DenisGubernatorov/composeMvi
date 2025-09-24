

package com.example.workmatetest.domain.usecase

import com.example.workmatetest.domain.model.Result
import com.example.workmatetest.domain.repository.RecipeRepository
import javax.inject.Inject

class SyncRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.syncRandomRecipes()
    }
}